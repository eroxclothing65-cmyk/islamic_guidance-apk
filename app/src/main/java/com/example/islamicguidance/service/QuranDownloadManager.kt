package com.example.islamicguidance.service

import android.content.Context
import com.example.islamicguidance.data.QuranData
import com.example.islamicguidance.data.RecitersData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

data class DownloadProgress(
    val surahNumber: Int,
    val reciterId: String,
    val currentAyah: Int,
    val totalAyahs: Int,
    val progressPercent: Float,
    val isDownloading: Boolean = false,
    val isComplete: Boolean = false,
    val errorMessage: String? = null
)

class QuranDownloadManager(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var downloadJob: Job? = null

    private val _downloadState = MutableStateFlow<Map<String, DownloadProgress>>(emptyMap())
    val downloadState: StateFlow<Map<String, DownloadProgress>> = _downloadState.asStateFlow()

    private val _totalStorageBytes = MutableStateFlow(0L)
    val totalStorageBytes: StateFlow<Long> = _totalStorageBytes.asStateFlow()

    init {
        updateStorageUsage()
    }

    private fun getAudioDirectory(reciterId: String): File {
        val dir = File(context.filesDir, "quran_audio/$reciterId")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun isSurahDownloaded(reciterId: String, surahNumber: Int, totalVerses: Int): Boolean {
        val dir = getAudioDirectory(reciterId)
        val surahFormatted = String.format("%03d", surahNumber)
        // Check if all verses exist and have size > 0
        for (i in 1..totalVerses) {
            val ayahFormatted = String.format("%03d", i)
            val file = File(dir, "$surahFormatted$ayahFormatted.mp3")
            if (!file.exists() || file.length() == 0L) {
                return false
            }
        }
        return true
    }

    fun downloadSurah(surahNumber: Int, totalVerses: Int, reciterId: String) {
        val key = "$reciterId:$surahNumber"
        downloadJob?.cancel()

        _downloadState.value = _downloadState.value + (key to DownloadProgress(
            surahNumber = surahNumber,
            reciterId = reciterId,
            currentAyah = 0,
            totalAyahs = totalVerses,
            progressPercent = 0f,
            isDownloading = true
        ))

        downloadJob = scope.launch {
            val dir = getAudioDirectory(reciterId)
            val surahFormatted = String.format("%03d", surahNumber)

            for (ayah in 1..totalVerses) {
                if (!isActive) break

                val ayahFormatted = String.format("%03d", ayah)
                val targetFile = File(dir, "$surahFormatted$ayahFormatted.mp3")

                if (!targetFile.exists() || targetFile.length() == 0L) {
                    val urlString = RecitersData.getAyahAudioUrl(reciterId, surahNumber, ayah)
                    val success = downloadSingleFile(urlString, targetFile)
                    if (!success) {
                        _downloadState.value = _downloadState.value + (key to DownloadProgress(
                            surahNumber = surahNumber,
                            reciterId = reciterId,
                            currentAyah = ayah - 1,
                            totalAyahs = totalVerses,
                            progressPercent = (ayah - 1).toFloat() / totalVerses.toFloat(),
                            isDownloading = false,
                            errorMessage = "আয়াত $ayah ডাউনলোডে ত্রুটি ঘটেছে"
                        ))
                        return@launch
                    }
                }

                val percent = ayah.toFloat() / totalVerses.toFloat()
                _downloadState.value = _downloadState.value + (key to DownloadProgress(
                    surahNumber = surahNumber,
                    reciterId = reciterId,
                    currentAyah = ayah,
                    totalAyahs = totalVerses,
                    progressPercent = percent,
                    isDownloading = ayah < totalVerses,
                    isComplete = ayah == totalVerses
                ))
            }

            updateStorageUsage()
        }
    }

    private fun downloadSingleFile(urlString: String, destinationFile: File): Boolean {
        var connection: HttpURLConnection? = null
        try {
            val url = URL(urlString)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 8000
            connection.readTimeout = 15000
            connection.requestMethod = "GET"
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return false
            }

            val tempFile = File(destinationFile.parentFile, "${destinationFile.name}.tmp")
            connection.inputStream.use { input ->
                FileOutputStream(tempFile).use { output ->
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                    }
                }
            }

            if (tempFile.exists() && tempFile.length() > 0) {
                if (destinationFile.exists()) destinationFile.delete()
                tempFile.renameTo(destinationFile)
                return true
            }
            return false
        } catch (e: Exception) {
            return false
        } finally {
            connection?.disconnect()
        }
    }

    fun cancelDownload(reciterId: String, surahNumber: Int) {
        val key = "$reciterId:$surahNumber"
        downloadJob?.cancel()
        downloadJob = null
        val current = _downloadState.value[key]
        if (current != null) {
            _downloadState.value = _downloadState.value + (key to current.copy(isDownloading = false))
        }
    }

    fun deleteDownloadedSurah(reciterId: String, surahNumber: Int, totalVerses: Int) {
        scope.launch {
            val dir = getAudioDirectory(reciterId)
            val surahFormatted = String.format("%03d", surahNumber)
            for (i in 1..totalVerses) {
                val ayahFormatted = String.format("%03d", i)
                val file = File(dir, "$surahFormatted$ayahFormatted.mp3")
                if (file.exists()) {
                    file.delete()
                }
            }
            val key = "$reciterId:$surahNumber"
            _downloadState.value = _downloadState.value - key
            updateStorageUsage()
        }
    }

    fun deleteAllDownloads() {
        scope.launch {
            val baseDir = File(context.filesDir, "quran_audio")
            if (baseDir.exists()) {
                baseDir.deleteRecursively()
            }
            _downloadState.value = emptyMap()
            updateStorageUsage()
        }
    }

    fun updateStorageUsage() {
        scope.launch {
            val baseDir = File(context.filesDir, "quran_audio")
            _totalStorageBytes.value = if (baseDir.exists()) getFolderSize(baseDir) else 0L
        }
    }

    private fun getFolderSize(file: File): Long {
        if (!file.exists()) return 0L
        if (file.isFile) return file.length()
        var size = 0L
        file.listFiles()?.forEach { child ->
            size += getFolderSize(child)
        }
        return size
    }

    fun getFormattedStorageUsage(bytes: Long): String {
        val mb = bytes.toDouble() / (1024 * 1024)
        return String.format(java.util.Locale.US, "%.1f MB", mb)
    }
}

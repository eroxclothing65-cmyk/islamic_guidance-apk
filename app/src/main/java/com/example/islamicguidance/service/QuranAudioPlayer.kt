package com.example.islamicguidance.service

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import com.example.islamicguidance.data.QuranData
import com.example.islamicguidance.data.RecitersData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

enum class PlaybackState {
    IDLE, BUFFERING, PLAYING, PAUSED, ERROR
}

data class CurrentAudioTrack(
    val surahNumber: Int = 1,
    val ayahNumber: Int = 1,
    val surahName: String = "আল-ফাতিহা",
    val reciterId: String = "alafasy",
    val reciterName: String = "Mishary Rashid Alafasy",
    val audioUrl: String = ""
)

class QuranAudioPlayer(private val context: Context) : AudioManager.OnAudioFocusChangeListener {

    private var mediaPlayer: MediaPlayer? = null
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private var audioFocusRequest: AudioFocusRequest? = null

    private val playerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var progressJob: Job? = null

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _currentTrack = MutableStateFlow(CurrentAudioTrack())
    val currentTrack: StateFlow<CurrentAudioTrack> = _currentTrack.asStateFlow()

    private val _isRepeatEnabled = MutableStateFlow(false)
    val isRepeatEnabled: StateFlow<Boolean> = _isRepeatEnabled.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0)
    val currentPositionMs: StateFlow<Int> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0)
    val durationMs: StateFlow<Int> = _durationMs.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun playAyah(
        surahNumber: Int,
        ayahNumber: Int,
        surahName: String,
        reciterId: String,
        customUrl: String = ""
    ) {
        val reciter = RecitersData.getReciter(reciterId)
        val audioUrl = if (customUrl.isNotEmpty()) {
            customUrl
        } else {
            // Check if local downloaded file exists
            val localFile = getLocalAudioFile(reciterId, surahNumber, ayahNumber)
            if (localFile.exists() && localFile.length() > 0) {
                localFile.absolutePath
            } else {
                RecitersData.getAyahAudioUrl(reciterId, surahNumber, ayahNumber)
            }
        }

        stopInternal()

        _currentTrack.value = CurrentAudioTrack(
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            surahName = surahName,
            reciterId = reciter.id,
            reciterName = "${reciter.nameBn} (${reciter.nameEn})",
            audioUrl = audioUrl
        )
        _playbackState.value = PlaybackState.BUFFERING
        _errorMessage.value = null

        requestAudioFocus()

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioUrl)
                isLooping = _isRepeatEnabled.value

                setOnPreparedListener { mp ->
                    mp.start()
                    _playbackState.value = PlaybackState.PLAYING
                    _durationMs.value = mp.duration
                    startProgressTracker()
                }

                setOnCompletionListener {
                    if (!_isRepeatEnabled.value) {
                        _playbackState.value = PlaybackState.IDLE
                        _currentPositionMs.value = 0
                        stopProgressTracker()
                    }
                }

                setOnErrorListener { _, what, extra ->
                    _playbackState.value = PlaybackState.ERROR
                    _errorMessage.value = "অডিও প্লে করতে সমস্যা হয়েছে (Error $what, $extra)"
                    stopProgressTracker()
                    true
                }

                prepareAsync()
            }
        } catch (e: Exception) {
            _playbackState.value = PlaybackState.ERROR
            _errorMessage.value = "অডিও প্রস্তুত করা যায়নি: ${e.localizedMessage}"
            stopProgressTracker()
        }
    }

    fun playPreview(reciterId: String) {
        playAyah(
            surahNumber = 1,
            ayahNumber = 1,
            surahName = "আল-ফাতিহা (নমুনা)",
            reciterId = reciterId
        )
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _playbackState.value = PlaybackState.PAUSED
                stopProgressTracker()
            }
        }
    }

    fun resume() {
        requestAudioFocus()
        mediaPlayer?.let {
            it.start()
            _playbackState.value = PlaybackState.PLAYING
            startProgressTracker()
        }
    }

    fun togglePlayPause() {
        when (_playbackState.value) {
            PlaybackState.PLAYING -> pause()
            PlaybackState.PAUSED -> resume()
            else -> {}
        }
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.let {
            it.seekTo(positionMs)
            _currentPositionMs.value = positionMs
        }
    }

    fun stop() {
        stopInternal()
        abandonAudioFocus()
    }

    private fun stopInternal() {
        stopProgressTracker()
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            }
        } catch (e: Exception) {
            // Ignore safe release errors
        } finally {
            mediaPlayer = null
            _playbackState.value = PlaybackState.IDLE
            _currentPositionMs.value = 0
            _durationMs.value = 0
        }
    }

    fun toggleRepeat() {
        val next = !_isRepeatEnabled.value
        _isRepeatEnabled.value = next
        mediaPlayer?.isLooping = next
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressJob = playerScope.launch {
            while (isActive) {
                mediaPlayer?.let { mp ->
                    if (_playbackState.value == PlaybackState.PLAYING) {
                        try {
                            _currentPositionMs.value = mp.currentPosition
                        } catch (e: Exception) {
                            // Safe catch on race condition
                        }
                    }
                }
                delay(300)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                .setOnAudioFocusChangeListener(this)
                .build()
            audioFocusRequest?.let { audioManager?.requestAudioFocus(it) }
        } else {
            @Suppress("DEPRECATION")
            audioManager?.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }
    }

    private fun abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let { audioManager?.abandonAudioFocusRequest(it) }
        } else {
            @Suppress("DEPRECATION")
            audioManager?.abandonAudioFocus(this)
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS,
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                mediaPlayer?.setVolume(0.2f, 0.2f)
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                mediaPlayer?.setVolume(1.0f, 1.0f)
            }
        }
    }

    private fun getLocalAudioFile(reciterId: String, surah: Int, ayah: Int): File {
        val audioDir = File(context.filesDir, "quran_audio/$reciterId")
        val surahFormatted = String.format("%03d", surah)
        val ayahFormatted = String.format("%03d", ayah)
        return File(audioDir, "$surahFormatted$ayahFormatted.mp3")
    }

    fun release() {
        stop()
        playerScope.cancel()
    }
}

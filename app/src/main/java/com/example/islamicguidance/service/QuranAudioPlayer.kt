package com.example.islamicguidance.service

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class PlaybackState {
    IDLE, BUFFERING, PLAYING, PAUSED, ERROR
}

data class CurrentAudioTrack(
    val surahNumber: Int = 0,
    val ayahNumber: Int = 0,
    val surahName: String = "",
    val reciterName: String = "Mishary Rashid Alafasy (মিশারি রশিদ আল-আফাসী)"
)

class QuranAudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _currentTrack = MutableStateFlow(CurrentAudioTrack())
    val currentTrack: StateFlow<CurrentAudioTrack> = _currentTrack.asStateFlow()

    private val _isRepeatEnabled = MutableStateFlow(false)
    val isRepeatEnabled: StateFlow<Boolean> = _isRepeatEnabled.asStateFlow()

    fun playAyah(surahNumber: Int, ayahNumber: Int, surahName: String, url: String) {
        val audioUrl = if (url.isNotEmpty()) {
            url
        } else {
            val surahFormatted = String.format("%03d", surahNumber)
            val ayahFormatted = String.format("%03d", ayahNumber)
            "https://verses.quran.com/Alafasy/mp3/$surahFormatted$ayahFormatted.mp3"
        }

        stop()

        _currentTrack.value = CurrentAudioTrack(surahNumber, ayahNumber, surahName)
        _playbackState.value = PlaybackState.BUFFERING

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
                }
                setOnCompletionListener {
                    if (!_isRepeatEnabled.value) {
                        _playbackState.value = PlaybackState.IDLE
                    }
                }
                setOnErrorListener { _, _, _ ->
                    _playbackState.value = PlaybackState.ERROR
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            _playbackState.value = PlaybackState.ERROR
        }
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _playbackState.value = PlaybackState.PAUSED
            }
        }
    }

    fun resume() {
        mediaPlayer?.let {
            it.start()
            _playbackState.value = PlaybackState.PLAYING
        }
    }

    fun togglePlayPause() {
        when (_playbackState.value) {
            PlaybackState.PLAYING -> pause()
            PlaybackState.PAUSED -> resume()
            else -> {}
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            }
        } catch (e: Exception) {
            // Safe cleanup
        } finally {
            mediaPlayer = null
            _playbackState.value = PlaybackState.IDLE
        }
    }

    fun toggleRepeat() {
        val next = !_isRepeatEnabled.value
        _isRepeatEnabled.value = next
        mediaPlayer?.isLooping = next
    }
}

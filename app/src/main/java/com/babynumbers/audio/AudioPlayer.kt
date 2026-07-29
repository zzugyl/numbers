package com.babynumbers.audio

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    private var onPlayingChanged: ((Boolean) -> Unit)? = null

    fun setOnPlayingChangedListener(listener: (Boolean) -> Unit) {
        onPlayingChanged = listener
    }

    fun playLocalAudio(number: Int, language: String = "zh", onComplete: () -> Unit = {}) {
        try {
            stop()
            onPlayingChanged?.invoke(true)

            val prefix = if (language == "en") "en" else "zh"
            val resourceName = "${prefix}_${number}"
            val resId = context.resources.getIdentifier(resourceName, "raw", context.packageName)

            if (resId == 0) {
                Log.e("AudioPlayer", "Audio resource not found: $resourceName")
                onPlayingChanged?.invoke(false)
                onComplete()
                return
            }

            val afd = context.resources.openRawResourceFd(resId)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                setOnCompletionListener {
                    onPlayingChanged?.invoke(false)
                    onComplete()
                }
                setOnPreparedListener { start() }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Play failed: ${e.message}", e)
            onPlayingChanged?.invoke(false)
            onComplete()
        }
    }

    fun playRawAudio(resId: Int, onComplete: () -> Unit = {}) {
        try {
            stop()
            onPlayingChanged?.invoke(true)
            val afd = context.resources.openRawResourceFd(resId)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                setOnCompletionListener {
                    onPlayingChanged?.invoke(false)
                    onComplete()
                }
                setOnPreparedListener { start() }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Play raw audio failed: ${e.message}", e)
            onPlayingChanged?.invoke(false)
            onComplete()
        }
    }

    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
        onPlayingChanged?.invoke(false)
    }

    fun shutdown() {
        stop()
    }
}

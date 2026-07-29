package com.babynumbers.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var soundPool: SoundPool? = null
    private var isInitialized = false

    // 音效 ID（实际项目中需要添加音效文件到 res/raw）
    private var correctSoundId: Int = 0
    private var wrongSoundId: Int = 0
    private var clickSoundId: Int = 0
    private var celebrationSoundId: Int = 0

    fun init() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()

        // 实际项目中取消注释并添加音效文件
        // correctSoundId = soundPool!!.load(context, R.raw.correct, 1)
        // wrongSoundId = soundPool!!.load(context, R.raw.wrong, 1)
        // clickSoundId = soundPool!!.load(context, R.raw.click, 1)
        // celebrationSoundId = soundPool!!.load(context, R.raw.celebration, 1)

        isInitialized = true
    }

    fun playCorrect() {
        if (!isInitialized || correctSoundId == 0) return
        soundPool?.play(correctSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playWrong() {
        if (!isInitialized || wrongSoundId == 0) return
        soundPool?.play(wrongSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playClick() {
        if (!isInitialized || clickSoundId == 0) return
        soundPool?.play(clickSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playCelebration() {
        if (!isInitialized || celebrationSoundId == 0) return
        soundPool?.play(celebrationSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        isInitialized = false
    }
}

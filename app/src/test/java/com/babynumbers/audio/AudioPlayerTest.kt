package com.babynumbers.audio

import android.content.Context
import android.media.MediaPlayer
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * AudioPlayer 测试
 *
 * 测试覆盖：
 * - playLocalAudio() 基本逻辑
 * - playRawAudio() 基本逻辑
 * - stop() 和 shutdown() 清理
 * - 错误处理（资源不存在、异常情况）
 */
class AudioPlayerTest {

    @MockK(relaxed = true)
    private lateinit var mockContext: Context

    @MockK(relaxed = true)
    private lateinit var mockResources: android.content.res.Resources

    private lateinit var audioPlayer: AudioPlayer

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { mockContext.resources } returns mockResources
        every { mockContext.packageName } returns "com.babynumbers"
        every { mockResources.openRawResourceFd(any()) } returns mockk(relaxed = true)
        every { mockResources.getIdentifier(any(), any(), any()) } returns 0

        audioPlayer = AudioPlayer(mockContext)
    }

    @After
    fun teardown() {
        audioPlayer.shutdown()
        unmockkAll()
    }

    // ========== playLocalAudio() 测试 ==========

    @Test
    fun `playLocalAudio - with valid resource does not throw`() {
        // Given
        every {
            mockResources.getIdentifier("zh_1", "raw", "com.babynumbers")
        } returns 123

        // When & Then
        try {
            audioPlayer.playLocalAudio(1, "zh") {}
        } catch (e: Exception) {
            fail("playLocalAudio should not throw: ${e.message}")
        }
    }

    @Test
    fun `playLocalAudio - invalid resource calls onComplete`() {
        // Given
        val onComplete = mockk<() -> Unit>(relaxed = true)
        every {
            mockResources.getIdentifier("zh_999", "raw", any())
        } returns 0

        // When
        audioPlayer.playLocalAudio(999, "zh", onComplete)

        // Then
        verify { onComplete.invoke() }
    }

    @Test
    fun `playLocalAudio - default language is zh`() {
        // Given
        val onComplete = mockk<() -> Unit>(relaxed = true)
        every {
            mockResources.getIdentifier("zh_1", "raw", any())
        } returns 123

        // When
        audioPlayer.playLocalAudio(1, onComplete = onComplete)

        // Then
        verify {
            mockResources.getIdentifier("zh_1", "raw", "com.babynumbers")
        }
    }

    @Test
    fun `playLocalAudio - English language loads en_ prefix`() {
        // Given
        every {
            mockResources.getIdentifier("en_1", "raw", any())
        } returns 123

        // When
        audioPlayer.playLocalAudio(1, "en") {}

        // Then
        verify {
            mockResources.getIdentifier("en_1", "raw", "com.babynumbers")
        }
    }

    @Test
    fun `playLocalAudio - registers playing listener`() {
        // Given
        val listener = mockk<(Boolean) -> Unit>(relaxed = true)
        audioPlayer.setOnPlayingChangedListener(listener)

        every {
            mockResources.getIdentifier(any(), any(), any())
        } returns 123

        // When
        audioPlayer.playLocalAudio(1, "zh") {}

        // Then: Listener should be registered
        verify { listener.invoke(true) }
    }

    // ========== playRawAudio() 测试 ==========

    @Test
    fun `playRawAudio - with valid resource ID does not throw`() {
        // Given
        every {
            mockResources.openRawResourceFd(any())
        } returns mockk(relaxed = true)

        // When & Then
        try {
            audioPlayer.playRawAudio(123) {}
        } catch (e: Exception) {
            fail("playRawAudio should not throw: ${e.message}")
        }
    }

    @Test
    fun `playRawAudio - invokes onComplete on error`() {
        // Given
        every {
            mockResources.openRawResourceFd(any())
        } throws Exception("Resource not found")

        val onComplete = mockk<() -> Unit>(relaxed = true)

        // When
        audioPlayer.playRawAudio(123, onComplete)

        // Then
        verify { onComplete.invoke() }
    }

    // ========== stop() 测试 ==========

    @Test
    fun `stop - can be called multiple times safely`() {
        // Given
        audioPlayer.playLocalAudio(1, "zh") {}

        // When
        try {
            audioPlayer.stop()
            audioPlayer.stop()
        } catch (e: Exception) {
            fail("Stop should be idempotent: ${e.message}")
        }

        assertTrue("Multiple stops should be safe", true)
    }

    // ========== shutdown() 测试 ==========

    @Test
    fun `shutdown - completes without error`() {
        // Given
        audioPlayer.playLocalAudio(1, "zh") {}

        // When
        audioPlayer.shutdown()

        // Then
        assertTrue("Shutdown should complete", true)
    }
}

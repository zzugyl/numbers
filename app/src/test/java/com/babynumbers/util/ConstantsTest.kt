package com.babynumbers.util

import org.junit.Test
import org.junit.Assert.*

/**
 * Constants 验证测试
 *
 * 测试覆盖：
 * - 所有常量值存在且有效
 * - 阶段配置一致性
 * - 游戏参数范围验证
 * - DataStore 键名唯一性
 * - 语言代码标准化
 */
class ConstantsTest {

    // ========== 应用信息测试 ==========

    @Test
    fun `APP_NAME - is not empty`() {
        assertTrue("APP_NAME should not be empty", Constants.APP_NAME.isNotEmpty())
    }

    @Test
    fun `APP_VERSION - follows semantic versioning`() {
        // Given
        val versionRegex = """\d+\.\d+\.\d+""".toRegex()

        // Then
        assertTrue(
            "APP_VERSION should follow semver (x.y.z)",
            versionRegex.matches(Constants.APP_VERSION)
        )
    }

    // ========== 学习阶段测试 ==========

    @Test
    fun `MAX_STAGES - is reasonable`() {
        assertTrue("MAX_STAGES should be positive", Constants.MAX_STAGES > 0)
        assertTrue("MAX_STAGES should be <= 10", Constants.MAX_STAGES <= 10)
    }

    @Test
    fun `STAGE thresholds - are monotonically increasing`() {
        assertTrue(
            "Stage thresholds should increase",
            Constants.STAGE_1_MAX < Constants.STAGE_2_MAX
        )
        assertTrue(
            "Stage thresholds should increase",
            Constants.STAGE_2_MAX < Constants.STAGE_3_MAX
        )
        assertTrue(
            "Stage thresholds should increase",
            Constants.STAGE_3_MAX < Constants.STAGE_4_MAX
        )
    }

    @Test
    fun `STAGE thresholds - align with NumberData stage logic`() {
        // Given: NumberData stage boundaries
        // Stage 1: 1-10, Stage 2: 11-20, Stage 3: 21-50, Stage 4: 51-100

        // Then: Constants should match
        assertEquals(10, Constants.STAGE_1_MAX)
        assertEquals(20, Constants.STAGE_2_MAX)
        assertEquals(50, Constants.STAGE_3_MAX)
        assertEquals(100, Constants.STAGE_4_MAX)
    }

    // ========== 配对游戏参数测试 ==========

    @Test
    fun `MATCHING_GAME pairs - are monotonically increasing`() {
        assertTrue(
            "Matching game pairs should increase with stage",
            Constants.MATCHING_GAME_STAGE_1_PAIRS < Constants.MATCHING_GAME_STAGE_2_PAIRS
        )
        assertTrue(
            "Matching game pairs should increase with stage",
            Constants.MATCHING_GAME_STAGE_2_PAIRS < Constants.MATCHING_GAME_STAGE_3_PAIRS
        )
        assertTrue(
            "Matching game pairs should increase with stage",
            Constants.MATCHING_GAME_STAGE_3_PAIRS < Constants.MATCHING_GAME_STAGE_4_PAIRS
        )
    }

    @Test
    fun `MATCHING_GAME pairs - are within valid range`() {
        val allPairs = listOf(
            Constants.MATCHING_GAME_STAGE_1_PAIRS,
            Constants.MATCHING_GAME_STAGE_2_PAIRS,
            Constants.MATCHING_GAME_STAGE_3_PAIRS,
            Constants.MATCHING_GAME_STAGE_4_PAIRS
        )

        assertTrue("Min pairs should be >= 2", allPairs.min() >= 2)
        assertTrue("Max pairs should be <= 20", allPairs.max() <= 20)
    }

    // ========== UI 参数测试 ==========

    @Test
    fun `MIN_TOUCH_TARGET_SIZE - meets accessibility standard`() {
        // Given: Android accessibility requires 48dp minimum
        assertTrue(
            "MIN_TOUCH_TARGET_SIZE should be >= 48dp for accessibility",
            Constants.MIN_TOUCH_TARGET_SIZE >= 48
        )
    }

    @Test
    fun `CARD_CORNER_RADIUS - is reasonable`() {
        assertTrue("CARD_CORNER_RADIUS should be positive", Constants.CARD_CORNER_RADIUS > 0)
        assertTrue("CARD_CORNER_RADIUS should be <= 100", Constants.CARD_CORNER_RADIUS <= 100)
    }

    @Test
    fun `CARD_ELEVATION - is non-negative`() {
        assertTrue("CARD_ELEVATION should be >= 0", Constants.CARD_ELEVATION >= 0)
    }

    // ========== DataStore 键测试 ==========

    @Test
    fun `DATASTORE_NAME - is not empty`() {
        assertTrue("DATASTORE_NAME should not be empty", Constants.DATASTORE_NAME.isNotEmpty())
    }

    @Test
    fun `DataStore keys - are unique`() {
        val keys = listOf(
            Constants.KEY_LANGUAGE,
            Constants.KEY_CURRENT_STAGE,
            Constants.KEY_COMPLETED_NUMBERS
        )

        assertEquals("All DataStore keys should be unique", keys.size, keys.toSet().size)
    }

    @Test
    fun `DataStore keys - follow snake_case convention`() {
        val keys = listOf(
            Constants.KEY_LANGUAGE,
            Constants.KEY_CURRENT_STAGE,
            Constants.KEY_COMPLETED_NUMBERS
        )

        keys.forEach { key ->
            assertTrue(
                "Key '$key' should follow snake_case",
                key.matches(Regex("^[a-z][a-z0-9_]*$"))
            )
        }
    }

    // ========== 语言代码测试 ==========

    @Test
    fun `LANGUAGE constants - are not empty`() {
        assertTrue("LANGUAGE_CHINESE should not be empty", Constants.LANGUAGE_CHINESE.isNotEmpty())
        assertTrue("LANGUAGE_ENGLISH should not be empty", Constants.LANGUAGE_ENGLISH.isNotEmpty())
    }

    @Test
    fun `LANGUAGE constants - are different`() {
        assertNotEquals(
            "Language codes should be different",
            Constants.LANGUAGE_CHINESE,
            Constants.LANGUAGE_ENGLISH
        )
    }

    @Test
    fun `LANGUAGE constants - are standard codes`() {
        // Standard ISO 639-1 codes
        assertEquals("zh", Constants.LANGUAGE_CHINESE)
        assertEquals("en", Constants.LANGUAGE_ENGLISH)
    }

    // ========== ASR 和动画参数测试 ==========

    @Test
    fun `ASR_TIMEOUT_MS - is reasonable`() {
        assertTrue("ASR timeout should be > 0", Constants.ASR_TIMEOUT_MS > 0)
        assertTrue("ASR timeout should be <= 30s", Constants.ASR_TIMEOUT_MS <= 30_000L)
    }

    @Test
    fun `ANIMATION durations - are monotonically increasing`() {
        assertTrue(
            "Animation durations should increase",
            Constants.ANIMATION_DURATION_SHORT < Constants.ANIMATION_DURATION_MEDIUM
        )
        assertTrue(
            "Animation durations should increase",
            Constants.ANIMATION_DURATION_MEDIUM < Constants.ANIMATION_DURATION_LONG
        )
    }

    @Test
    fun `ANIMATION durations - are reasonable`() {
        val durations = listOf(
            Constants.ANIMATION_DURATION_SHORT,
            Constants.ANIMATION_DURATION_MEDIUM,
            Constants.ANIMATION_DURATION_LONG
        )

        assertTrue("Min animation should be >= 50ms", durations.min() >= 50)
        assertTrue("Max animation should be <= 2000ms", durations.max() <= 2000)
    }
}

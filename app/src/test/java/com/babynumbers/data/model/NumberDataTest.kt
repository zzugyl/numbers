package com.babynumbers.data.model

import org.junit.Test
import org.junit.Assert.*

/**
 * NumberData 核心逻辑测试
 *
 * 测试覆盖：
 * - 数字数据生成
 * - 中英文名称转换
 * - 阶段划分逻辑
 * - getNumbersForStage() 边界条件
 */
class NumberDataTest {

    // ========== getNumbersForStage() 测试 ==========

    @Test
    fun `getNumbersForStage - Stage 1 returns 1-10`() {
        // Given
        val stage = 1

        // When
        val result = NumberData.getNumbersForStage(stage)

        // Then
        assertEquals(10, result.size)
        assertEquals(1, result[0].number)
        assertEquals(10, result[9].number)
        assertTrue(result.all { it.stage == 1 })
    }

    @Test
    fun `getNumbersForStage - Stage 2 returns 11-20`() {
        // Given
        val stage = 2

        // When
        val result = NumberData.getNumbersForStage(stage)

        // Then
        assertEquals(10, result.size)
        assertEquals(11, result[0].number)
        assertEquals(20, result[9].number)
        assertTrue(result.all { it.stage == 2 })
    }

    @Test
    fun `getNumbersForStage - Stage 3 returns 21-50`() {
        // Given
        val stage = 3

        // When
        val result = NumberData.getNumbersForStage(stage)

        // Then
        assertEquals(30, result.size)
        assertEquals(21, result[0].number)
        assertEquals(50, result[29].number)
        assertTrue(result.all { it.stage == 3 })
    }

    @Test
    fun `getNumbersForStage - Stage 4 returns 51-100`() {
        // Given
        val stage = 4

        // When
        val result = NumberData.getNumbersForStage(stage)

        // Then
        assertEquals(50, result.size)
        assertEquals(51, result[0].number)
        assertEquals(100, result[49].number)
        assertTrue(result.all { it.stage == 4 })
    }

    @Test
    fun `getNumbersForStage - Invalid stage returns empty list`() {
        // Given
        val invalidStages = listOf(0, 5, -1, 100)

        // When & Then
        invalidStages.forEach { stage ->
            val result = NumberData.getNumbersForStage(stage)
            assertTrue("Stage $stage should return empty list", result.isEmpty())
        }
    }

    // ========== fromNumber() 测试 ==========

    @Test
    fun `fromNumber - creates correct NumberData for single digit`() {
        // Given
        val number = 5

        // When
        val result = NumberData.fromNumber(number)

        // Then
        assertEquals(number, result.number)
        assertEquals("五", result.chineseName)
        assertEquals("five", result.englishName)
        assertEquals(1, result.stage) // <= 10
        assertNotNull(result.emoji)
    }

    @Test
    fun `fromNumber - creates correct NumberData for teen number`() {
        // Given
        val number = 15

        // When
        val result = NumberData.fromNumber(number)

        // Then
        assertEquals(number, result.number)
        assertEquals("十五", result.chineseName)
        assertEquals("fifteen", result.englishName)
        assertEquals(2, result.stage) // <= 20
    }

    @Test
    fun `fromNumber - creates correct NumberData for compound number`() {
        // Given
        val number = 35

        // When
        val result = NumberData.fromNumber(number)

        // Then
        assertEquals(number, result.number)
        assertEquals("三十五", result.chineseName)
        assertEquals("thirty-five", result.englishName)
        assertEquals(3, result.stage) // <= 50
    }

    @Test
    fun `fromNumber - creates correct NumberData for 100`() {
        // Given
        val number = 100

        // When
        val result = NumberData.fromNumber(number)

        // Then
        assertEquals(number, result.number)
        assertEquals("一百", result.chineseName)
        assertEquals("one hundred", result.englishName)
        assertEquals(4, result.stage) // > 50
    }

    // ========== 边界值测试 ==========

    @Test
    fun `stage boundaries - number 10 is Stage 1`() {
        val result = NumberData.fromNumber(10)
        assertEquals(1, result.stage)
    }

    @Test
    fun `stage boundaries - number 11 is Stage 2`() {
        val result = NumberData.fromNumber(11)
        assertEquals(2, result.stage)
    }

    @Test
    fun `stage boundaries - number 20 is Stage 2`() {
        val result = NumberData.fromNumber(20)
        assertEquals(2, result.stage)
    }

    @Test
    fun `stage boundaries - number 21 is Stage 3`() {
        val result = NumberData.fromNumber(21)
        assertEquals(3, result.stage)
    }

    @Test
    fun `stage boundaries - number 50 is Stage 3`() {
        val result = NumberData.fromNumber(50)
        assertEquals(3, result.stage)
    }

    @Test
    fun `stage boundaries - number 51 is Stage 4`() {
        val result = NumberData.fromNumber(51)
        assertEquals(4, result.stage)
    }

    @Test
    fun `stage boundaries - number 100 is Stage 4`() {
        val result = NumberData.fromNumber(100)
        assertEquals(4, result.stage)
    }

    // ========== emoji 测试 ==========

    @Test
    fun `fromNumber - emoji is not null or empty`() {
        // Test a few numbers across different stages
        listOf(1, 15, 35, 75).forEach { number ->
            val result = NumberData.fromNumber(number)
            assertNotNull("Emoji for $number should not be null", result.emoji)
            assertTrue("Emoji for $number should not be empty", result.emoji.isNotEmpty())
        }
    }

    @Test
    fun `fromNumber - emoji cycles correctly`() {
        // Given: emojis list has 10 items
        val emojis = listOf("⭐", "🌟", "✨", "💫", "🌈", "🎨", "🎵", "🌸", "🎀", "🦋")

        // When: Create numbers 0-9
        val results = (0..9).map { NumberData.fromNumber(it) }

        // Then: Emojis should match modulo pattern
        results.forEachIndexed { index, data ->
            assertEquals(emojis[index], data.emoji)
        }
    }

    // ========== getAllNumbers() 测试 ==========

    @Test
    fun `getAllNumbers - returns 101 numbers (0-100)`() {
        val result = NumberData.getAllNumbers()
        assertEquals(101, result.size)
    }

    @Test
    fun `getAllNumbers - starts at 0 and ends at 100`() {
        val result = NumberData.getAllNumbers()
        assertEquals(0, result.first().number)
        assertEquals(100, result.last().number)
    }
}

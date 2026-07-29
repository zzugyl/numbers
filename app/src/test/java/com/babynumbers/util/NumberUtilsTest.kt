package com.babynumbers.util

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * NumberUtils 单元测试
 *
 * 覆盖范围：
 * - 中文数字转换（1-100）
 * - 英文数字转换（1-100）
 * - 边界值测试
 */
class NumberUtilsTest {

    @Test
    fun `getChineseNumberName - 个位数`() {
        assertEquals("一", NumberUtils.getChineseNumberName(1))
        assertEquals("二", NumberUtils.getChineseNumberName(2))
        assertEquals("九", NumberUtils.getChineseNumberName(9))
    }

    @Test
    fun `getChineseNumberName - 十位数`() {
        assertEquals("十", NumberUtils.getChineseNumberName(10))
        assertEquals("二十", NumberUtils.getChineseNumberName(20))
        assertEquals("九十", NumberUtils.getChineseNumberName(90))
    }

    @Test
    fun `getChineseNumberName - 十几`() {
        assertEquals("十一", NumberUtils.getChineseNumberName(11))
        assertEquals("十五", NumberUtils.getChineseNumberName(15))
        assertEquals("十九", NumberUtils.getChineseNumberName(19))
    }

    @Test
    fun `getChineseNumberName - compound numbers`() {
        assertEquals("二十一", NumberUtils.getChineseNumberName(21))
        assertEquals("三十五", NumberUtils.getChineseNumberName(35))
        assertEquals("九十九", NumberUtils.getChineseNumberName(99))
    }

    @Test
    fun `getChineseNumberName - 一百`() {
        assertEquals("一百", NumberUtils.getChineseNumberName(100))
    }

    @Test
    fun `getEnglishNumberName - 个位数`() {
        assertEquals("one", NumberUtils.getEnglishNumberName(1))
        assertEquals("five", NumberUtils.getEnglishNumberName(5))
        assertEquals("nine", NumberUtils.getEnglishNumberName(9))
    }

    @Test
    fun `getEnglishNumberName - 十几`() {
        assertEquals("ten", NumberUtils.getEnglishNumberName(10))
        assertEquals("eleven", NumberUtils.getEnglishNumberName(11))
        assertEquals("fifteen", NumberUtils.getEnglishNumberName(15))
        assertEquals("nineteen", NumberUtils.getEnglishNumberName(19))
    }

    @Test
    fun `getEnglishNumberName - 整十`() {
        assertEquals("twenty", NumberUtils.getEnglishNumberName(20))
        assertEquals("thirty", NumberUtils.getEnglishNumberName(30))
        assertEquals("ninety", NumberUtils.getEnglishNumberName(90))
    }

    @Test
    fun `getEnglishNumberName - compound numbers`() {
        assertEquals("twenty-one", NumberUtils.getEnglishNumberName(21))
        assertEquals("thirty-five", NumberUtils.getEnglishNumberName(35))
        assertEquals("ninety-nine", NumberUtils.getEnglishNumberName(99))
    }

    @Test
    fun `getEnglishNumberName - 一百`() {
        assertEquals("one hundred", NumberUtils.getEnglishNumberName(100))
    }
}

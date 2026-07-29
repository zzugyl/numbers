package com.babynumbers.util

/**
 * 数字工具类
 *
 * 提供数字到中英文名称的转换
 */
object NumberUtils {

    private val chineseNames = mapOf(
        0 to "零", 1 to "一", 2 to "二", 3 to "三", 4 to "四",
        5 to "五", 6 to "六", 7 to "七", 8 to "八", 9 to "九",
        10 to "十", 11 to "十一", 12 to "十二", 13 to "十三", 14 to "十四",
        15 to "十五", 16 to "十六", 17 to "十七", 18 to "十八", 19 to "十九",
        20 to "二十", 30 to "三十", 40 to "四十", 50 to "五十",
        60 to "六十", 70 to "七十", 80 to "八十", 90 to "九十", 100 to "一百"
    )

    private val englishNames = mapOf(
        0 to "zero", 1 to "one", 2 to "two", 3 to "three", 4 to "four",
        5 to "five", 6 to "six", 7 to "seven", 8 to "eight", 9 to "nine",
        10 to "ten", 11 to "eleven", 12 to "twelve", 13 to "thirteen", 14 to "fourteen",
        15 to "fifteen", 16 to "sixteen", 17 to "seventeen", 18 to "eighteen", 19 to "nineteen",
        20 to "twenty", 30 to "thirty", 40 to "forty", 50 to "fifty",
        60 to "sixty", 70 to "seventy", 80 to "eighty", 90 to "ninety", 100 to "one hundred"
    )

    /**
     * 获取中文数字名称
     */
    fun getChineseNumberName(number: Int): String {
        if (number in chineseNames) return chineseNames[number] ?: ""

        val tens = number / 10 * 10
        val ones = number % 10
        return "${chineseNames[tens] ?: ""}${chineseNames[ones] ?: ""}"
    }

    /**
     * 获取英文数字名称
     */
    fun getEnglishNumberName(number: Int): String {
        if (number in englishNames) return englishNames[number] ?: ""

        val tens = number / 10 * 10
        val ones = number % 10
        return "${englishNames[tens] ?: ""}-${englishNames[ones] ?: ""}"
    }
}

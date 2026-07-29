package com.babynumbers.data.model

data class NumberData(
    val number: Int,
    val chineseName: String,
    val englishName: String,
    val emoji: String,
    val stage: Int
) {
    companion object {
        private val numberNames = mapOf(
            0 to Pair("零", "zero"),
            1 to Pair("一", "one"),
            2 to Pair("二", "two"),
            3 to Pair("三", "three"),
            4 to Pair("四", "four"),
            5 to Pair("五", "five"),
            6 to Pair("六", "six"),
            7 to Pair("七", "seven"),
            8 to Pair("八", "eight"),
            9 to Pair("九", "nine"),
            10 to Pair("十", "ten"),
            11 to Pair("十一", "eleven"),
            12 to Pair("十二", "twelve"),
            13 to Pair("十三", "thirteen"),
            14 to Pair("十四", "fourteen"),
            15 to Pair("十五", "fifteen"),
            16 to Pair("十六", "sixteen"),
            17 to Pair("十七", "seventeen"),
            18 to Pair("十八", "eighteen"),
            19 to Pair("十九", "nineteen"),
            20 to Pair("二十", "twenty"),
            30 to Pair("三十", "thirty"),
            40 to Pair("四十", "forty"),
            50 to Pair("五十", "fifty"),
            60 to Pair("六十", "sixty"),
            70 to Pair("七十", "seventy"),
            80 to Pair("八十", "eighty"),
            90 to Pair("九十", "ninety"),
            100 to Pair("一百", "one hundred")
        )

        private fun getChineseName(number: Int): String {
            if (number in numberNames) return numberNames[number]!!.first
            if (number < 20) return ""
            
            val tens = number / 10 * 10
            val ones = number % 10
            return if (ones == 0) {
                numberNames[tens]?.first ?: ""
            } else {
                "${numberNames[tens]?.first ?: ""}${numberNames[ones]?.first ?: ""}"
            }
        }

        private fun getEnglishName(number: Int): String {
            if (number in numberNames) return numberNames[number]!!.second
            if (number < 20) return ""
            
            val tens = number / 10 * 10
            val ones = number % 10
            return if (ones == 0) {
                numberNames[tens]?.second ?: ""
            } else {
                "${numberNames[tens]?.second ?: ""}-${numberNames[ones]?.second ?: ""}"
            }
        }

        private fun getStage(number: Int): Int = when {
            number <= 10 -> 1
            number <= 20 -> 2
            number <= 50 -> 3
            else -> 4
        }

        private val emojis = listOf("⭐", "🌟", "✨", "💫", "🌈", "🎨", "🎵", "🌸", "🎀", "🦋")

        fun fromNumber(number: Int): NumberData {
            return NumberData(
                number = number,
                chineseName = getChineseName(number),
                englishName = getEnglishName(number),
                emoji = emojis[number % emojis.size],
                stage = getStage(number)
            )
        }

        fun getNumbersForStage(stage: Int): List<NumberData> {
            return when (stage) {
                1 -> (1..10).map { fromNumber(it) }
                2 -> (11..20).map { fromNumber(it) }
                3 -> (21..50).map { fromNumber(it) }
                4 -> (51..100).map { fromNumber(it) }
                else -> emptyList()
            }
        }

        fun getAllNumbers(): List<NumberData> {
            return (0..100).map { fromNumber(it) }
        }
    }
}

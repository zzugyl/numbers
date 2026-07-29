package com.babynumbers.util

object Constants {
    // 应用信息
    const val APP_NAME = "宝宝学数字"
    const val APP_VERSION = "1.0.0"

    // 学习阶段
    const val MAX_STAGES = 4
    const val STAGE_1_MAX = 10
    const val STAGE_2_MAX = 20
    const val STAGE_3_MAX = 50
    const val STAGE_4_MAX = 100

    // ASR 参数
    const val ASR_TIMEOUT_MS = 5000L

    // 动画参数
    const val ANIMATION_DURATION_SHORT = 200
    const val ANIMATION_DURATION_MEDIUM = 400
    const val ANIMATION_DURATION_LONG = 600

    // 配对游戏参数
    const val MATCHING_GAME_STAGE_1_PAIRS = 3
    const val MATCHING_GAME_STAGE_2_PAIRS = 4
    const val MATCHING_GAME_STAGE_3_PAIRS = 5
    const val MATCHING_GAME_STAGE_4_PAIRS = 6

    // UI 参数
    const val MIN_TOUCH_TARGET_SIZE = 80
    const val CARD_CORNER_RADIUS = 20
    const val CARD_ELEVATION = 4

    // DataStore 键
    const val DATASTORE_NAME = "baby_numbers_settings"
    const val KEY_LANGUAGE = "language"
    const val KEY_CURRENT_STAGE = "current_stage"
    const val KEY_COMPLETED_NUMBERS = "completed_numbers"

    // 语言
    const val LANGUAGE_CHINESE = "zh"
    const val LANGUAGE_ENGLISH = "en"
}

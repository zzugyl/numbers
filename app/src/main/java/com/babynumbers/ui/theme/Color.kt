package com.babynumbers.ui.theme

import androidx.compose.ui.graphics.Color

// 马卡龙配色方案 - 鲜艳版
val CreamWhite = Color(0xFFFFF8F0)      // 浅米白 - 背景色
val MintGreen = Color(0xFF66D9A0)       // 薄荷绿 - 主色调 (更鲜艳)
val SakuraPink = Color(0xFFFF8A80)      // 樱花粉 - 辅助色1 (更鲜艳)
val TaroPurple = Color(0xFFB39DDB)      // 香芋紫 - 辅助色2 (更鲜艳)
val CreamYellow = Color(0xFFFFD54F)     // 奶油黄 - 辅助色3 (更鲜艳)
val SkyBlue = Color(0xFF64B5F6)         // 天空蓝 - 辅助色4 (更鲜艳)
val DeepBrownGray = Color(0xFF4E342E)   // 深棕灰 - 文字色
val PeachOrange = Color(0xFFFFAB91)     // 蜜桃橙 - 强调色 (更鲜艳)

// Material 3 Color Scheme
val Primary = MintGreen
val OnPrimary = DeepBrownGray
val PrimaryContainer = MintGreen.copy(alpha = 0.3f)
val OnPrimaryContainer = DeepBrownGray

val Secondary = SakuraPink
val OnSecondary = DeepBrownGray
val SecondaryContainer = SakuraPink.copy(alpha = 0.3f)
val OnSecondaryContainer = DeepBrownGray

val Tertiary = TaroPurple
val OnTertiary = DeepBrownGray
val TertiaryContainer = TaroPurple.copy(alpha = 0.3f)
val OnTertiaryContainer = DeepBrownGray

val Background = CreamWhite
val OnBackground = DeepBrownGray
val Surface = CreamWhite
val OnSurface = DeepBrownGray
val SurfaceVariant = CreamYellow.copy(alpha = 0.3f)
val OnSurfaceVariant = DeepBrownGray

val Error = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF410002)

val Outline = DeepBrownGray.copy(alpha = 0.5f)
val OutlineVariant = DeepBrownGray.copy(alpha = 0.2f)

package com.babynumbers.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

object ScreenUtils {
    @Composable
    fun getScreenWidth(): Dp {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp.dp
    }

    @Composable
    fun getScreenHeight(): Dp {
        val configuration = LocalConfiguration.current
        return configuration.screenHeightDp.dp
    }

    @Composable
    fun getScreenWidthPx(): Int {
        val configuration = LocalConfiguration.current
        val density = LocalDensity.current
        return with(density) { configuration.screenWidthDp.dp.roundToPx() }
    }

    @Composable
    fun getScreenHeightPx(): Int {
        val configuration = LocalConfiguration.current
        val density = LocalDensity.current
        return with(density) { configuration.screenHeightDp.dp.roundToPx() }
    }

    @Composable
    fun getCardWidth(columns: Int, spacing: Dp = 16.dp): Dp {
        val screenWidth = getScreenWidth()
        return (screenWidth - spacing * (columns + 1)) / columns
    }

    @Composable
    fun getCardHeight(cardWidth: Dp, aspectRatio: Float = 1.2f): Dp {
        return cardWidth * aspectRatio
    }

    @Composable
    fun isTablet(): Boolean {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp >= 600
    }

    @Composable
    fun isLandscape(): Boolean {
        val configuration = LocalConfiguration.current
        return configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    }

    // 基准设计尺寸（以 804x360 横屏为目标）
    private const val BASE_HEIGHT = 360f
    private const val BASE_WIDTH = 804f

    @Composable
    private fun heightScaleFactor(): Float {
        val h = getScreenHeight().value
        return min(h / BASE_HEIGHT, 1.3f) // 不放大超过 1.3x
    }

    @Composable
    fun scaledDp(base: Dp): Dp {
        return (base.value * heightScaleFactor()).dp
    }

    @Composable
    fun scaledSp(base: Float): Float {
        return base * heightScaleFactor()
    }

    @Composable
    fun outerPadding(): Dp = scaledDp(16.dp)

    @Composable
    fun verticalSpacingSmall(): Dp = scaledDp(8.dp)

    @Composable
    fun verticalSpacingMedium(): Dp = scaledDp(16.dp)

    @Composable
    fun verticalSpacingLarge(): Dp = scaledDp(24.dp)
}

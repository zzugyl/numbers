package com.babynumbers.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babynumbers.ui.theme.DeepBrownGray
import com.babynumbers.ui.theme.MintGreen

@Composable
fun AnimatedNumber(
    number: Int,
    isAnimating: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    
    // 弹跳动画
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isAnimating) -20f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    // 缩放动画
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = 0.3f,
            stiffness = 300f
        ),
        label = "scale"
    )
    
    // 透明度动画
    val alpha by animateFloatAsState(
        targetValue = if (isAnimating) 0.8f else 1f,
        animationSpec = tween(300),
        label = "alpha"
    )

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 120.sp),
            color = DeepBrownGray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .scale(scale)
                .offset(y = with(density) { bounce.toDp() })
        )
    }
}

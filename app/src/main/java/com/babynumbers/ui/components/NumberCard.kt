package com.babynumbers.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babynumbers.data.model.NumberData
import com.babynumbers.ui.theme.*

@Composable
fun NumberCard(
    numberData: NumberData,
    isCompleted: Boolean = false,
    isFlipped: Boolean = false,
    isMatched: Boolean = false,
    isNumberCard: Boolean = true,
    cardAspectRatio: Float = 1f,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = 0.3f,
            stiffness = 300f
        ),
        label = "scale"
    )

    val backgroundColor = when {
        isMatched -> MintGreen.copy(alpha = 0.5f)
        isCompleted -> CreamYellow.copy(alpha = 0.3f)
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(cardAspectRatio)
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                if (!isMatched) {
                    isPressed = true
                    onClick()
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isFlipped || isMatched) {
                // 显示数字或图案
                if (isNumberCard) {
                    Text(
                        text = numberData.number.toString(),
                        style = MaterialTheme.typography.displayMedium.copy(fontSize = 68.sp),
                        color = DeepBrownGray,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = numberData.emoji,
                        style = MaterialTheme.typography.displayMedium.copy(fontSize = 68.sp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // 卡片背面
                Text(
                    text = "?",
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 68.sp),
                    color = DeepBrownGray.copy(alpha = 0.3f),
                    textAlign = TextAlign.Center
                )
            }

            // 完成标记
            if (isCompleted) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .background(
                            color = MintGreen,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
        }
    }
}

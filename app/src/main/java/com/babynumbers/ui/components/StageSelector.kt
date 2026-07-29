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
import com.babynumbers.ui.theme.*

@Composable
fun StageSelector(
    stageNumber: Int,
    progress: Float,
    isUnlocked: Boolean,
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

    val stageColor = when (stageNumber) {
        1 -> MintGreen
        2 -> SakuraPink
        3 -> TaroPurple
        4 -> SkyBlue
        else -> MintGreen
    }

    val stageTitle = when (stageNumber) {
        1 -> "第一阶段\n1-10"
        2 -> "第二阶段\n11-20"
        3 -> "第三阶段\n21-50"
        4 -> "第四阶段\n51-100"
        else -> ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                if (isUnlocked) {
                    isPressed = true
                    onClick()
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) stageColor.copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isUnlocked) 4.dp else 0.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stageTitle,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 34.sp),
                    color = if (isUnlocked) DeepBrownGray else Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

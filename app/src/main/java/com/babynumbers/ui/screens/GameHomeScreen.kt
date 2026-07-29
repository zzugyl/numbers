package com.babynumbers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babynumbers.ui.theme.Background
import com.babynumbers.ui.theme.DeepBrownGray
import com.babynumbers.ui.theme.MintGreen
import com.babynumbers.ui.theme.SakuraPink
import com.babynumbers.ui.components.DebouncedBackButton

@Composable
fun GameHomeScreen(
    stageNumber: Int,
    onMatchingGame: () -> Unit,
    onListenGame: () -> Unit,
    onParkingGame: () -> Unit,
    onBack: () -> Unit
) {
    val stageTitle = when (stageNumber) {
        1 -> "第一阶段: 1-10"
        2 -> "第二阶段: 11-20"
        3 -> "第三阶段: 21-50"
        4 -> "第四阶段: 51-100"
        else -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 顶部栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 返回按钮
                DebouncedBackButton(
                    onClick = onBack
                )

                Text(
                    text = "游戏中心",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 56.sp),
                    color = DeepBrownGray,
                    textAlign = TextAlign.Center
                )

                // 占位
                Spacer(modifier = Modifier.size(80.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stageTitle,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 36.sp),
                color = DeepBrownGray.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 游戏卡片
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 配对游戏卡片
                GameCard(
                    title = "配对游戏",
                    emoji = "🃏",
                    color = MintGreen,
                    onClick = onMatchingGame
                )

                // 听音识数卡片
                GameCard(
                    title = "听音识数",
                    emoji = "🔊",
                    color = SakuraPink,
                    onClick = onListenGame
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 第二行游戏卡片
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // 数字停车卡片
                GameCard(
                    title = "数字停车",
                    emoji = "🚗",
                    color = androidx.compose.ui.graphics.Color(0xFF64B5F6),
                    onClick = onParkingGame
                )
            }
        }
    }
}

@Composable
private fun GameCard(
    title: String,
    emoji: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 72.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 36.sp),
                color = DeepBrownGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

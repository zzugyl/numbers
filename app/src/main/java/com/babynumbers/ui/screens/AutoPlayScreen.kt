package com.babynumbers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.babynumbers.data.model.NumberData
import com.babynumbers.ui.theme.Background
import com.babynumbers.ui.theme.DeepBrownGray
import com.babynumbers.ui.theme.MintGreen
import com.babynumbers.viewmodel.LearningViewModel
import kotlinx.coroutines.delay

@Composable
fun AutoPlayScreen(
    stageNumber: Int,
    onBack: () -> Unit,
    viewModel: LearningViewModel = hiltViewModel()
) {
    val numbers = viewModel.getNumbersForStage(stageNumber)
    var currentIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(true) }

    val currentNumber = numbers.getOrNull(currentIndex)
    val stageTitle = when (stageNumber) {
        1 -> "第一阶段: 1-10"
        2 -> "第二阶段: 11-20"
        3 -> "第三阶段: 21-50"
        4 -> "第四阶段: 51-100"
        else -> ""
    }

    // 自动播放逻辑
    var hasCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(currentIndex, isPlaying) {
        if (isPlaying && currentNumber != null && !hasCompleted) {
            // 播放数字发音
            viewModel.speakNumber(currentNumber.number)
            // 等待播放完成后立即下一个
            delay(1500)
            // 移动到下一个
            if (currentIndex < numbers.size - 1) {
                currentIndex++
            } else {
                // 播放完毕，标记并返回
                hasCompleted = true
                isPlaying = false
                onBack()
            }
        }
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
                // 返回按钮 - 带防抖保护
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MintGreen.copy(alpha = 0.7f))
                        .clickable {
                            if (!hasCompleted) {
                                isPlaying = false
                                onBack()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "返回",
                        modifier = Modifier.size(48.dp),
                        tint = DeepBrownGray
                    )
                }

                Text(
                    text = stageTitle,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 68.sp),
                    color = DeepBrownGray,
                    textAlign = TextAlign.Center
                )

                // 进度显示
                Text(
                    text = "${currentIndex + 1}/${numbers.size}",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 68.sp),
                    color = DeepBrownGray,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 进度条
            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / numbers.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 当前数字显示
            if (currentNumber != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // 大数字
                    Text(
                        text = currentNumber.number.toString(),
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 160.sp),
                        color = DeepBrownGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // 英文名称
                    Text(
                        text = currentNumber.englishName,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 72.sp),
                        color = DeepBrownGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

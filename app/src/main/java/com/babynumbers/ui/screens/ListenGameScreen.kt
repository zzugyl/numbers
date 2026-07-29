package com.babynumbers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.babynumbers.ui.theme.Background
import com.babynumbers.ui.theme.DeepBrownGray
import com.babynumbers.ui.theme.MintGreen
import com.babynumbers.ui.theme.SakuraPink
import com.babynumbers.ui.components.DebouncedBackButton
import com.babynumbers.viewmodel.ListenGameViewModel

@Composable
fun ListenGameScreen(
    stageNumber: Int,
    onBack: () -> Unit,
    viewModel: ListenGameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(stageNumber) {
        viewModel.startGame(stageNumber)
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
                    text = "听音识数",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 56.sp),
                    color = DeepBrownGray,
                    textAlign = TextAlign.Center
                )

                // 重新开始按钮
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MintGreen.copy(alpha = 0.7f))
                        .clickable { viewModel.resetGame() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "重新开始",
                        modifier = Modifier.size(48.dp),
                        tint = DeepBrownGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 题目进度
            Text(
                text = "第 ${uiState.questionIndex + 1}/${uiState.totalQuestions} 题",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 36.sp),
                color = DeepBrownGray.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 游戏完成
            if (uiState.isGameComplete) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MintGreen.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "游戏完成！",
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 64.sp),
                            color = DeepBrownGray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "正确: ${uiState.correctCount}/${uiState.totalQuestions}",
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 48.sp),
                            color = DeepBrownGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // 播放提示
                if (uiState.isPlaying) {
                    Text(
                        text = "🔊 正在播放...",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 48.sp),
                        color = SakuraPink,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "请选择正确的数字",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 48.sp),
                        color = DeepBrownGray.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 选项按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    uiState.options.forEach { number ->
                        val isSelected = uiState.selectedAnswer == number
                        val isCorrectAnswer = number == uiState.currentNumber
                        val showResult = uiState.selectedAnswer != null

                        val backgroundColor = when {
                            showResult && isSelected && uiState.isCorrect == true -> MintGreen
                            showResult && isSelected && uiState.isCorrect == false -> Color.Red.copy(alpha = 0.7f)
                            showResult && isCorrectAnswer -> MintGreen.copy(alpha = 0.5f)
                            else -> Color.White
                        }

                        val textColor = when {
                            showResult && isSelected -> Color.White
                            showResult && isCorrectAnswer -> DeepBrownGray
                            else -> DeepBrownGray
                        }

                        Card(
                            modifier = Modifier
                                .size(180.dp, 140.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    if (!showResult) {
                                        viewModel.selectAnswer(number)
                                    }
                                },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = backgroundColor
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = number.toString(),
                                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 80.sp),
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 正确数统计
                Text(
                    text = "正确: ${uiState.correctCount}",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp),
                    color = DeepBrownGray
                )
            }
        }
    }
}

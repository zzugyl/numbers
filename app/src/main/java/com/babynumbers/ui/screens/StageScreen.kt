package com.babynumbers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.babynumbers.ui.components.NumberCard
import com.babynumbers.ui.components.DebouncedBackButton
import com.babynumbers.ui.theme.Background
import com.babynumbers.ui.theme.DeepBrownGray
import com.babynumbers.ui.theme.MintGreen
import com.babynumbers.util.ScreenUtils
import com.babynumbers.viewmodel.LearningViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StageScreen(
    stageNumber: Int,
    onNumberSelected: (Int) -> Unit,
    onGameHomeSelected: () -> Unit,
    onAutoPlay: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: LearningViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val numbers = viewModel.getNumbersForStage(stageNumber)
    val pad = ScreenUtils.outerPadding()
    val vSmall = ScreenUtils.verticalSpacingSmall()
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
            .padding(pad)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 顶部栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 返回按钮 - 大圆形
                DebouncedBackButton(
                    onClick = onBack
                )

                Text(
                    text = stageTitle,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 68.sp),
                    color = DeepBrownGray,
                    textAlign = TextAlign.Center
                )

                // 配对游戏按钮 - 大圆形
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MintGreen.copy(alpha = 0.7f))
                        .clickable { onGameHomeSelected() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "配对游戏",
                        modifier = Modifier.size(48.dp),
                        tint = DeepBrownGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 进度条
            LinearProgressIndicator(
                progress = { viewModel.getStageProgress(stageNumber) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 数字网格
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(vSmall),
                verticalArrangement = Arrangement.spacedBy(vSmall),
                modifier = Modifier.weight(1f)
            ) {
                items(numbers) { numberData ->
                    NumberCard(
                        numberData = numberData,
                        isCompleted = uiState.completedNumbers.contains(numberData.number),
                        isFlipped = true,
                        cardAspectRatio = 1.2f,
                        onClick = {
                            viewModel.selectNumber(numberData)
                            onNumberSelected(numberData.number)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 底部双箭头按钮 - 自动轮播
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MintGreen.copy(alpha = 0.7f))
                        .clickable { onAutoPlay(stageNumber) },
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "自动轮播",
                            modifier = Modifier.size(56.dp),
                            tint = DeepBrownGray
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "自动轮播",
                            modifier = Modifier.size(56.dp),
                            tint = DeepBrownGray
                        )
                    }
                }
            }
        }
    }
}

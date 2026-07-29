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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.babynumbers.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchingGameScreen(
    stageNumber: Int,
    onBack: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pad = ScreenUtils.outerPadding()
    val vSmall = ScreenUtils.verticalSpacingSmall()

    LaunchedEffect(stageNumber) {
        viewModel.startGame(stageNumber)
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
                    text = "配对游戏",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 68.sp),
                    color = DeepBrownGray,
                    textAlign = TextAlign.Center
                )

                // 重新开始按钮 - 大圆形
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

            Spacer(modifier = Modifier.height(vSmall))

            // 游戏统计
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "匹配: ${uiState.matchCount}/${uiState.totalPairs}",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 52.sp),
                    color = DeepBrownGray
                )
                Text(
                    text = "尝试: ${uiState.attempts}",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 52.sp),
                    color = DeepBrownGray
                )
            }

            Spacer(modifier = Modifier.height(vSmall))

            // 游戏完成提示
            if (uiState.isGameComplete) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "恭喜完成！",
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 64.sp),
                            color = DeepBrownGray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "你用了 ${uiState.attempts} 次尝试",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 44.sp),
                            color = DeepBrownGray.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(vSmall))

            // 卡片网格
            val columns = when (stageNumber) {
                1 -> 3
                2 -> 4
                3 -> 4
                4 -> 5
                else -> 3
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                horizontalArrangement = Arrangement.spacedBy(vSmall),
                verticalArrangement = Arrangement.spacedBy(vSmall),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.cards) { card ->
                    NumberCard(
                        numberData = card.number,
                        isFlipped = card.isFlipped,
                        isMatched = card.isMatched,
                        isNumberCard = card.isNumberCard,
                        cardAspectRatio = 2.2f,
                        onClick = { viewModel.onCardClicked(card) }
                    )
                }
            }
        }
    }
}

package com.babynumbers.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.babynumbers.data.model.NumberData
import com.babynumbers.ui.components.AnimatedNumber
import com.babynumbers.ui.components.DebouncedBackButton
import com.babynumbers.ui.theme.Background
import com.babynumbers.ui.theme.DeepBrownGray
import com.babynumbers.ui.theme.MintGreen
import com.babynumbers.util.ScreenUtils
import com.babynumbers.viewmodel.LearningViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberDetailScreen(
    number: Int,
    onBack: () -> Unit,
    viewModel: LearningViewModel = hiltViewModel()
) {
    val numberData = NumberData.fromNumber(number)
    val pad = ScreenUtils.outerPadding()
    val vSmall = ScreenUtils.verticalSpacingSmall()
    val vMedium = ScreenUtils.verticalSpacingMedium()

    var isAnimating by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = 0.3f,
            stiffness = 300f
        ),
        label = "scale"
    )

    val stars = remember(number) {
        (1..minOf(number, 20)).map { "⭐" }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(pad)
    ) {
        // 横屏布局：顶部栏 + 下方左右分栏
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
                    text = "学习数字",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 68.sp),
                    color = DeepBrownGray
                )

                // 完成按钮 - 大圆形
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MintGreen.copy(alpha = 0.7f))
                        .clickable {
                            viewModel.markNumberCompleted(number)
                            onBack()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "完成",
                        modifier = Modifier.size(48.dp),
                        tint = DeepBrownGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(vSmall))

            // 左右分栏：左边数字+名称，右边星星+播放按钮
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左侧：大数字 + 英文名称
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedNumber(
                        number = number,
                        isAnimating = isAnimating,
                        modifier = Modifier.scale(scale)
                    )

                    Spacer(modifier = Modifier.height(vMedium))

                    Text(
                        text = numberData.englishName,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 64.sp),
                        color = DeepBrownGray,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(vMedium))

                // 右侧：星星(仅1-20) + 播放按钮
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // 星星显示（仅1-20显示，换行排列）
                    if (number <= 20) {
                        val starRows = stars.chunked(5)
                        starRows.forEach { row ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(vSmall)
                            ) {
                                row.forEach { star ->
                                    Text(
                                        text = star,
                                        style = MaterialTheme.typography.displaySmall,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(vSmall))
                        }
                        Spacer(modifier = Modifier.height(vMedium))
                    }

                    // 播放发音按钮
                    Button(
                        onClick = {
                            isAnimating = true
                            viewModel.setSpeaking(true)
                            viewModel.speakNumber(number)
                        },
                        modifier = Modifier
                            .size(140.dp)
                            .aspectRatio(1f),
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintGreen
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "播放发音",
                            modifier = Modifier.size(64.dp),
                            tint = DeepBrownGray
                        )
                    }
                }
            }
        }
    }
}

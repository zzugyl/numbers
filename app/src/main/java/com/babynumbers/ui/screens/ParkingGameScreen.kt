package com.babynumbers.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.babynumbers.R
import com.babynumbers.ui.theme.Background
import com.babynumbers.ui.theme.DeepBrownGray
import com.babynumbers.ui.theme.MintGreen
import com.babynumbers.ui.components.DebouncedBackButton
import com.babynumbers.viewmodel.ParkingGameViewModel
import kotlinx.coroutines.delay

private val englishNumbers = mapOf(
    1 to "one", 2 to "two", 3 to "three", 4 to "four", 5 to "five",
    6 to "six", 7 to "seven", 8 to "eight", 9 to "nine", 10 to "ten",
    11 to "eleven", 12 to "twelve", 13 to "thirteen", 14 to "fourteen", 15 to "fifteen",
    16 to "sixteen", 17 to "seventeen", 18 to "eighteen", 19 to "nineteen", 20 to "twenty",
    21 to "twenty-one", 22 to "twenty-two", 23 to "twenty-three", 24 to "twenty-four", 25 to "twenty-five",
    26 to "twenty-six", 27 to "twenty-seven", 28 to "twenty-eight", 29 to "twenty-nine", 30 to "thirty",
    31 to "thirty-one", 32 to "thirty-two", 33 to "thirty-three", 34 to "thirty-four", 35 to "thirty-five",
    36 to "thirty-six", 37 to "thirty-seven", 38 to "thirty-eight", 39 to "thirty-nine", 40 to "forty",
    41 to "forty-one", 42 to "forty-two", 43 to "forty-three", 44 to "forty-four", 45 to "forty-five",
    46 to "forty-six", 47 to "forty-seven", 48 to "forty-eight", 49 to "forty-nine", 50 to "fifty",
    51 to "fifty-one", 52 to "fifty-two", 53 to "fifty-three", 54 to "fifty-four", 55 to "fifty-five",
    56 to "fifty-six", 57 to "fifty-seven", 58 to "fifty-eight", 59 to "fifty-nine", 60 to "sixty",
    61 to "sixty-one", 62 to "sixty-two", 63 to "sixty-three", 64 to "sixty-four", 65 to "sixty-five",
    66 to "sixty-six", 67 to "sixty-seven", 68 to "sixty-eight", 69 to "sixty-nine", 70 to "seventy",
    71 to "seventy-one", 72 to "seventy-two", 73 to "seventy-three", 74 to "seventy-four", 75 to "seventy-five",
    76 to "seventy-six", 77 to "seventy-seven", 78 to "seventy-eight", 79 to "seventy-nine", 80 to "eighty",
    81 to "eighty-one", 82 to "eighty-two", 83 to "eighty-three", 84 to "eighty-four", 85 to "eighty-five",
    86 to "eighty-six", 87 to "eighty-seven", 88 to "eighty-eight", 89 to "eighty-nine", 90 to "ninety",
    91 to "ninety-one", 92 to "ninety-two", 93 to "ninety-three", 94 to "ninety-four", 95 to "ninety-five",
    96 to "ninety-six", 97 to "ninety-seven", 98 to "ninety-eight", 99 to "ninety-nine", 100 to "one hundred"
)

@Composable
fun ParkingGameScreen(
    stageNumber: Int,
    onBack: () -> Unit,
    viewModel: ParkingGameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(stageNumber) {
        viewModel.startGame(stageNumber)
    }

    // 选中的汽车
    var selectedCar by remember { mutableStateOf<com.babynumbers.viewmodel.ParkingCar?>(null) }

    // 完成页面延迟显示
    var showCompleteScreen by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isGameComplete) {
        if (uiState.isGameComplete) {
            // 等待最后一辆车的停车动画完成
            delay(600)
            showCompleteScreen = true
        } else {
            showCompleteScreen = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(12.dp)
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
                DebouncedBackButton(
                    onClick = onBack
                )

                Text(
                    text = "数字停车",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = DeepBrownGray,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MintGreen.copy(alpha = 0.7f))
                        .clickable {
                            viewModel.resetGame()
                            selectedCar = null
                        },
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

            // 进度和提示
            Text(
                text = "已停: ${uiState.parkedCount}/${uiState.totalCars}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = DeepBrownGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (showCompleteScreen) {
                // 游戏完成
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
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "全部停好！",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 80.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = DeepBrownGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // 游戏进行中 - 点击选择模式
                // 提示文字
                Text(
                    text = if (selectedCar != null) {
                        "点击停车位停入 ${englishNumbers[selectedCar?.number]}"
                    } else {
                        "点击选择要停的汽车"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = DeepBrownGray.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 第一排：汽车 - 只显示未停的
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    uiState.cars.filter { !it.isParked }.forEach { car ->
                        val isSelected = selectedCar?.number == car.number
                        val scale by animateFloatAsState(
                            targetValue = if (isSelected) 1.1f else 1f,
                            animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
                            label = "car_scale"
                        )

                        Box(
                            modifier = Modifier
                                .size(220.dp, 180.dp)
                                .scale(scale)
                                .shadow(
                                    elevation = if (isSelected) 12.dp else 6.dp,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Transparent)
                                .clickable {
                                    selectedCar = if (isSelected) null else car
                                    // 播放英语数字
                                    viewModel.speakNumber(car.number)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            // 汽车图片（上方）
                            Image(
                                painter = painterResource(id = car.imageResId),
                                contentDescription = "汽车 ${car.number}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .padding(top = 4.dp, start = 8.dp, end = 8.dp)
                            )
                            // 数字叠加在车底
                            Text(
                                text = englishNumbers[car.number] ?: car.number.toString(),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (isSelected) MintGreen else DeepBrownGray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 8.dp)
                            )
                            // 选中高亮边框 - 强对比
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(DeepBrownGray.copy(alpha = 0.15f))
                                        .border(
                                            width = 4.dp,
                                            color = MintGreen,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                )
                            }
                        }
                    }
                }

                // 道路分隔线
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF5D5D5D))
                )
                Spacer(modifier = Modifier.height(24.dp))

                // 第二排：停车位
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    uiState.spots.forEach { spot ->
                        Box(
                            modifier = Modifier
                                .size(220.dp, 180.dp)
                                .shadow(6.dp, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (spot.isOccupied) MintGreen.copy(alpha = 0.4f)
                                    else Color(0xFFE0E0E0)
                                )
                                .clickable {
                                    if (selectedCar != null && !spot.isOccupied) {
                                        viewModel.parkCar(selectedCar!!.number, uiState.spots.indexOf(spot))
                                        selectedCar = null
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (spot.isOccupied) {
                                // 已停车 - 显示小汽车
                                val parkedCar = uiState.cars.find { it.number == spot.number }
                                if (parkedCar != null) {
                                    Image(
                                        painter = painterResource(id = parkedCar.imageResId),
                                        contentDescription = "已停汽车",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp)
                                            .padding(top = 4.dp, start = 8.dp, end = 8.dp)
                                    )
                                } else {
                                    Text(
                                        text = "✓",
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontSize = 64.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MintGreen
                                    )
                                }
                            } else {
                                // 停车位数字
                                Text(
                                    text = spot.number.toString(),
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 60.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = DeepBrownGray.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

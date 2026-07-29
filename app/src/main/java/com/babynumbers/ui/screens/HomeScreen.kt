package com.babynumbers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.babynumbers.ui.components.LanguageToggle
import com.babynumbers.ui.components.StageSelector
import com.babynumbers.ui.theme.Background
import com.babynumbers.ui.theme.DeepBrownGray
import com.babynumbers.viewmodel.LearningViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStageSelected: (Int) -> Unit,
    viewModel: LearningViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // 标题区域
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "宝宝学数字",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 112.sp),
                    color = DeepBrownGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Baby Numbers",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp),
                    color = DeepBrownGray.copy(alpha = 0.6f)
                )
            }

            // 语言切换
            LanguageToggle(
                currentLanguage = uiState.language,
                onLanguageChanged = { language ->
                    viewModel.setLanguage(language)
                }
            )

            // 阶段选择 - 横向 4 列
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(4) { index ->
                    val stageNumber = index + 1
                    StageSelector(
                        stageNumber = stageNumber,
                        progress = viewModel.getStageProgress(stageNumber),
                        isUnlocked = viewModel.isStageUnlocked(stageNumber),
                        onClick = {
                            if (viewModel.isStageUnlocked(stageNumber)) {
                                onStageSelected(stageNumber)
                            }
                        }
                    )
                }
            }
        }
    }
}

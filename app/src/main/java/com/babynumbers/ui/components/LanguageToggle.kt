package com.babynumbers.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babynumbers.ui.theme.*
import com.babynumbers.util.Constants

@Composable
fun LanguageToggle(
    currentLanguage: String,
    onLanguageChanged: (String) -> Unit
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

    Row(
        modifier = Modifier
            .scale(scale),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 中文按钮 - 水滴圆形
        Box(
            modifier = Modifier
                .size(120.dp)
                .shadow(
                    elevation = if (currentLanguage == Constants.LANGUAGE_CHINESE) 12.dp else 6.dp,
                    shape = CircleShape,
                    ambientColor = MintGreen.copy(alpha = 0.5f),
                    spotColor = MintGreen.copy(alpha = 0.5f)
                )
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = if (currentLanguage == Constants.LANGUAGE_CHINESE)
                            listOf(MintGreen, MintGreen.copy(alpha = 0.7f))
                        else
                            listOf(Color.LightGray.copy(alpha = 0.6f), Color.LightGray.copy(alpha = 0.3f))
                    )
                )
                .clickable {
                    isPressed = true
                    onLanguageChanged(Constants.LANGUAGE_CHINESE)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "中文",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 42.sp),
                color = if (currentLanguage == Constants.LANGUAGE_CHINESE)
                    Color.White
                else
                    Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        // 英文按钮 - 水滴圆形
        Box(
            modifier = Modifier
                .size(120.dp)
                .shadow(
                    elevation = if (currentLanguage == Constants.LANGUAGE_ENGLISH) 12.dp else 6.dp,
                    shape = CircleShape,
                    ambientColor = SakuraPink.copy(alpha = 0.5f),
                    spotColor = SakuraPink.copy(alpha = 0.5f)
                )
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = if (currentLanguage == Constants.LANGUAGE_ENGLISH)
                            listOf(SakuraPink, SakuraPink.copy(alpha = 0.7f))
                        else
                            listOf(Color.LightGray.copy(alpha = 0.6f), Color.LightGray.copy(alpha = 0.3f))
                    )
                )
                .clickable {
                    isPressed = true
                    onLanguageChanged(Constants.LANGUAGE_ENGLISH)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "English",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 36.sp),
                color = if (currentLanguage == Constants.LANGUAGE_ENGLISH)
                    Color.White
                else
                    Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

package com.babynumbers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.babynumbers.ui.theme.DeepBrownGray
import com.babynumbers.ui.theme.MintGreen

/**
 * 防抖返回按钮
 * 防止快速多次点击导致的导航异常
 *
 * @param onClick 返回回调
 * @param debounceTime 防抖时间窗口（毫秒），默认 500ms
 */
@Composable
fun DebouncedBackButton(
    onClick: () -> Unit,
    debounceTime: Long = 500L
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(MintGreen.copy(alpha = 0.7f))
            .clickable {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime > debounceTime) {
                    lastClickTime = currentTime
                    onClick()
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
}

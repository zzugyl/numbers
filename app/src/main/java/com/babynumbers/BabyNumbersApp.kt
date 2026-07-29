package com.babynumbers

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.babynumbers.ui.navigation.NavGraph
import com.babynumbers.ui.theme.BabyNumbersTheme

@Composable
fun BabyNumbersApp() {
    BabyNumbersTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            NavGraph()
        }
    }
}

package com.babynumbers.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babynumbers.R
import com.babynumbers.audio.AudioPlayer
import com.babynumbers.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ParkingCar(
    val number: Int,
    val color: Color,
    val imageResId: Int,
    val emoji: String = "🚗",
    var isParked: Boolean = false
)

data class ParkingSpot(
    val number: Int,
    val isOccupied: Boolean = false
)

data class ParkingGameState(
    val cars: List<ParkingCar> = emptyList(),
    val spots: List<ParkingSpot> = emptyList(),
    val parkedCount: Int = 0,
    val totalCars: Int = 3,
    val isGameComplete: Boolean = false
)

@HiltViewModel
class ParkingGameViewModel @Inject constructor(
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(ParkingGameState())
    val uiState: StateFlow<ParkingGameState> = _uiState.asStateFlow()

    private val carColors = listOf(
        Color(0xFF66D9A0), // MintGreen
        Color(0xFFFF8A80), // SakuraPink
        Color(0xFFB39DDB), // TaroPurple
        Color(0xFFFFD54F), // CreamYellow
        Color(0xFF64B5F6)  // SkyBlue
    )

    private val carImages = listOf(
        R.drawable.car_cyan,
        R.drawable.car_orange,
        R.drawable.car_purple,
        R.drawable.car_red,
        R.drawable.car_yellow
    )

    init {
        // AudioPlayer 无需初始化
    }

    fun startGame(stage: Int) {
        val totalCars = when (stage) {
            1, 2 -> 3
            3, 4 -> 5
            else -> 3
        }

        val numbers = getNumbersForStage(stage).shuffled().take(totalCars)
        val shuffledNumbers = numbers.shuffled()

        val cars = numbers.mapIndexed { index, num ->
            ParkingCar(
                number = num,
                color = carColors[index],
                imageResId = carImages[index]
            )
        }

        val spots = shuffledNumbers.map { ParkingSpot(number = it) }

        _uiState.value = ParkingGameState(
            cars = cars,
            spots = spots,
            totalCars = totalCars
        )
    }

    fun parkCar(carNumber: Int, spotIndex: Int) {
        val currentState = _uiState.value
        val spot = currentState.spots.getOrNull(spotIndex) ?: return

        if (spot.isOccupied) return

        if (carNumber == spot.number) {
            // 正确停放
            val updatedCars = currentState.cars.map {
                if (it.number == carNumber) it.copy(isParked = true) else it
            }
            val updatedSpots = currentState.spots.mapIndexed { index, s ->
                if (index == spotIndex) s.copy(isOccupied = true) else s
            }

            val newParkedCount = currentState.parkedCount + 1
            val isComplete = newParkedCount >= currentState.totalCars

            _uiState.value = currentState.copy(
                cars = updatedCars,
                spots = updatedSpots,
                parkedCount = newParkedCount,
                isGameComplete = isComplete
            )

            // 播放正确音效
            audioPlayer.playRawAudio(R.raw.game_right)

            // 如果游戏完成，播放完成音效
            if (isComplete) {
                viewModelScope.launch {
                    delay(1000)
                    audioPlayer.playRawAudio(R.raw.game_challenge)
                }
            }
        } else {
            // 错误，播放错误音效
            audioPlayer.playRawAudio(R.raw.game_wrong)
        }
    }

    fun resetGame() {
        val currentTotal = _uiState.value.totalCars
        // 根据当前总数推断阶段
        val stage = when (currentTotal) {
            3 -> 1
            5 -> 3
            else -> 1
        }
        startGame(stage)
    }

    fun speakNumber(number: Int) {
        audioPlayer.playLocalAudio(number, Constants.LANGUAGE_ENGLISH)
    }

    private fun getNumbersForStage(stage: Int): List<Int> {
        return when (stage) {
            1 -> (1..10).toList()
            2 -> (11..20).toList()
            3 -> (21..50).toList()
            4 -> (51..100).toList()
            else -> (1..10).toList()
        }
    }
}

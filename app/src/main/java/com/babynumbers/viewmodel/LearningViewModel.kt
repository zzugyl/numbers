package com.babynumbers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babynumbers.audio.AudioPlayer
import com.babynumbers.data.model.NumberData
import com.babynumbers.data.repository.LearningRepository
import com.babynumbers.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LearningUiState(
    val language: String = Constants.LANGUAGE_CHINESE,
    val currentStage: Int = 1,
    val completedNumbers: Set<Int> = emptySet(),
    val currentNumber: NumberData? = null,
    val isSpeaking: Boolean = false,
    val isListening: Boolean = false,
    val isTtsReady: Boolean = false
)

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val repository: LearningRepository,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearningUiState())
    val uiState: StateFlow<LearningUiState> = _uiState.asStateFlow()

    // 已解锁的阶段集合
    private val _unlockedStages = MutableStateFlow<Set<Int>>(setOf(1))
    val unlockedStages: StateFlow<Set<Int>> = _unlockedStages.asStateFlow()

    init {
        // AudioPlayer 无需初始化，直接可用
        _uiState.value = _uiState.value.copy(isTtsReady = true)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.language.collect { language ->
                _uiState.value = _uiState.value.copy(language = language)
            }
        }

        viewModelScope.launch {
            repository.currentStage.collect { stage ->
                _uiState.value = _uiState.value.copy(currentStage = stage)
            }
        }

        viewModelScope.launch {
            repository.completedNumbers.collect { numbers ->
                _uiState.value = _uiState.value.copy(completedNumbers = numbers)
                updateUnlockedStages(numbers)
            }
        }
    }

    private fun updateUnlockedStages(completedNumbers: Set<Int>) {
        val unlocked = mutableSetOf<Int>(1) // Stage 1 总是解锁

        // 检查 Stage 2-4 是否解锁
        for (stage in 2..4) {
            val prevStage = stage - 1
            val prevStageNumbers = when (prevStage) {
                1 -> (1..10).toSet()
                2 -> (11..20).toSet()
                3 -> (21..50).toSet()
                else -> emptySet()
            }

            val completedInPrevStage = completedNumbers.filter { it in prevStageNumbers }.size
            if (completedInPrevStage >= prevStageNumbers.size) {
                unlocked.add(stage)
            }
        }

        _unlockedStages.value = unlocked
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            repository.setLanguage(language)
        }
    }

    fun setCurrentStage(stage: Int) {
        viewModelScope.launch {
            repository.setCurrentStage(stage)
            _uiState.value = _uiState.value.copy(currentStage = stage)
        }
    }

    fun selectNumber(number: NumberData) {
        _uiState.value = _uiState.value.copy(currentNumber = number)
    }

    fun markNumberCompleted(number: Int) {
        viewModelScope.launch {
            repository.markNumberCompleted(number)
        }
    }

    fun setSpeaking(speaking: Boolean) {
        _uiState.value = _uiState.value.copy(isSpeaking = speaking)
    }

    fun speakNumber(number: Int) {
        val language = when (_uiState.value.language) {
            Constants.LANGUAGE_CHINESE -> Constants.LANGUAGE_CHINESE
            Constants.LANGUAGE_ENGLISH -> Constants.LANGUAGE_ENGLISH
            else -> Constants.LANGUAGE_CHINESE
        }
        audioPlayer.playLocalAudio(number, language)
    }

    fun setListening(listening: Boolean) {
        _uiState.value = _uiState.value.copy(isListening = listening)
    }

    fun getNumbersForStage(stage: Int): List<NumberData> {
        return NumberData.getNumbersForStage(stage)
    }

    fun getStageProgress(stage: Int): Float {
        val completed = _uiState.value.completedNumbers.filter { number ->
            when (stage) {
                1 -> number in 1..10
                2 -> number in 11..20
                3 -> number in 21..50
                4 -> number in 51..100
                else -> false
            }
        }.size

        val total = when (stage) {
            1 -> 10
            2 -> 10
            3 -> 30
            4 -> 50
            else -> 0
        }

        return if (total > 0) completed.toFloat() / total else 0f
    }

    fun isStageUnlocked(stage: Int): Boolean {
        return _unlockedStages.value.contains(stage)
    }
}

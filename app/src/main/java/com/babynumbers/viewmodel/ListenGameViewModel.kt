package com.babynumbers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babynumbers.R
import com.babynumbers.audio.AudioPlayer
import com.babynumbers.data.repository.LearningRepository
import com.babynumbers.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListenGameState(
    val currentNumber: Int = 0,
    val options: List<Int> = emptyList(),
    val selectedAnswer: Int? = null,
    val isCorrect: Boolean? = null,
    val isPlaying: Boolean = false,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val correctCount: Int = 0,
    val isGameComplete: Boolean = false,
    val usedNumbers: List<Int> = emptyList()
)

@HiltViewModel
class ListenGameViewModel @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val repository: LearningRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListenGameState())
    val uiState: StateFlow<ListenGameState> = _uiState.asStateFlow()

    private var currentStage: Int = 1
    private var playJob: Job? = null

    init {
        // AudioPlayer 无需初始化
    }

    fun startGame(stage: Int) {
        currentStage = stage
        _uiState.value = ListenGameState()
        generateQuestion()
    }

    private suspend fun getCurrentLanguage(): String {
        return repository.language.first()
    }

    private fun generateQuestion() {
        val numbers = getNumbersForStage(currentStage)
        val availableNumbers = numbers - _uiState.value.usedNumbers
        
        // 如果没有足够数字，重置已使用列表
        if (availableNumbers.size < 3) {
            _uiState.value = _uiState.value.copy(usedNumbers = emptyList())
            return generateQuestion()
        }
        
        val correct = availableNumbers.random()
        val wrong = (availableNumbers - correct).shuffled().take(2)
        val options = (wrong + correct).shuffled()

        _uiState.value = _uiState.value.copy(
            currentNumber = correct,
            options = options,
            selectedAnswer = null,
            isCorrect = null,
            usedNumbers = _uiState.value.usedNumbers + correct
        )

        // 自动播放读音
        playNumberTwice(correct)
    }

    private fun playNumberTwice(number: Int) {
        playJob?.cancel()
        playJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPlaying = true)
            val language = when (getCurrentLanguage()) {
                Constants.LANGUAGE_CHINESE -> Constants.LANGUAGE_CHINESE
                Constants.LANGUAGE_ENGLISH -> Constants.LANGUAGE_ENGLISH
                else -> Constants.LANGUAGE_CHINESE
            }
            audioPlayer.playLocalAudio(number, language)
            delay(1500)
            audioPlayer.playLocalAudio(number, language)
            delay(1500)
            _uiState.value = _uiState.value.copy(isPlaying = false)
        }
    }

    fun selectAnswer(number: Int) {
        if (_uiState.value.selectedAnswer != null) return

        // 停止当前播放并取消协程
        playJob?.cancel()
        audioPlayer.stop()

        val isCorrect = number == _uiState.value.currentNumber

        _uiState.value = _uiState.value.copy(
            selectedAnswer = number,
            isCorrect = isCorrect,
            correctCount = if (isCorrect) _uiState.value.correctCount + 1 else _uiState.value.correctCount
        )

        viewModelScope.launch {
            if (isCorrect) {
                // 播放正确音效
                audioPlayer.playRawAudio(R.raw.game_right)
                delay(2000)
            } else {
                // 播放错误音效
                audioPlayer.playRawAudio(R.raw.game_wrong)
                delay(2000)
            }

            // 下一题或结束
            val newIndex = _uiState.value.questionIndex + 1
            if (newIndex >= _uiState.value.totalQuestions) {
                _uiState.value = _uiState.value.copy(isGameComplete = true)
                // 根据正确率播放不同音效
                val finalCorrectCount = _uiState.value.correctCount
                val resId = when {
                    finalCorrectCount == _uiState.value.totalQuestions -> R.raw.game_challenge
                    finalCorrectCount >= 4 -> R.raw.game_comeon
                    else -> R.raw.game_workhard
                }
                audioPlayer.playRawAudio(resId)
            } else {
                _uiState.value = _uiState.value.copy(questionIndex = newIndex)
                generateQuestion()
            }
        }
    }

    fun resetGame() {
        startGame(currentStage)
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

package com.babynumbers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babynumbers.R
import com.babynumbers.audio.AudioPlayer
import com.babynumbers.data.model.NumberData
import com.babynumbers.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchingCard(
    val id: Int,
    val number: NumberData,
    val isNumberCard: Boolean,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

data class GameUiState(
    val cards: List<MatchingCard> = emptyList(),
    val selectedCards: List<MatchingCard> = emptyList(),
    val matchCount: Int = 0,
    val totalPairs: Int = 0,
    val attempts: Int = 0,
    val isGameComplete: Boolean = false,
    val currentStage: Int = 1
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        // AudioPlayer 无需初始化
    }

    fun startGame(stage: Int) {
        val pairsCount = getPairsCountForStage(stage)
        val (min, max) = getNumberRangeForStage(stage)
        val numbers = (min..max).shuffled().take(pairsCount)
        
        val cards = mutableListOf<MatchingCard>()
        numbers.forEachIndexed { index, number ->
            val numberData = NumberData.fromNumber(number)
            cards.add(MatchingCard(
                id = index * 2,
                number = numberData,
                isNumberCard = true
            ))
            cards.add(MatchingCard(
                id = index * 2 + 1,
                number = numberData,
                isNumberCard = false
            ))
        }
        
        _uiState.value = GameUiState(
            cards = cards.shuffled(),
            totalPairs = pairsCount,
            currentStage = stage
        )
    }

    private fun getPairsCountForStage(stage: Int): Int {
        return when (stage) {
            1 -> Constants.MATCHING_GAME_STAGE_1_PAIRS
            2 -> Constants.MATCHING_GAME_STAGE_2_PAIRS
            3 -> Constants.MATCHING_GAME_STAGE_3_PAIRS
            4 -> Constants.MATCHING_GAME_STAGE_4_PAIRS
            else -> Constants.MATCHING_GAME_STAGE_1_PAIRS
        }
    }

    private fun getNumberRangeForStage(stage: Int): Pair<Int, Int> {
        return when (stage) {
            1 -> 1 to Constants.STAGE_1_MAX
            2 -> Constants.STAGE_1_MAX + 1 to Constants.STAGE_2_MAX
            3 -> Constants.STAGE_2_MAX + 1 to Constants.STAGE_3_MAX
            4 -> Constants.STAGE_3_MAX + 1 to Constants.STAGE_4_MAX
            else -> 1 to Constants.STAGE_1_MAX
        }
    }

    fun onCardClicked(card: MatchingCard) {
        val currentState = _uiState.value
        
        // 如果卡片已翻转或已匹配，忽略点击
        if (card.isFlipped || card.isMatched) return
        
        // 如果已选择两张卡片，忽略点击
        if (currentState.selectedCards.size >= 2) return
        
        // 翻转卡片
        val updatedCards = currentState.cards.map {
            if (it.id == card.id) it.copy(isFlipped = true) else it
        }
        
        val newSelected = currentState.selectedCards + card
        
        _uiState.value = currentState.copy(
            cards = updatedCards,
            selectedCards = newSelected,
            attempts = currentState.attempts + if (newSelected.size == 2) 1 else 0
        )
        
        // 如果选择了两张卡片，检查是否匹配
        if (newSelected.size == 2) {
            checkMatch(newSelected[0], newSelected[1])
        }
    }

    private fun checkMatch(card1: MatchingCard, card2: MatchingCard) {
        val currentState = _uiState.value
        
        // 检查是否是同一个数字的两种卡片（数字卡和图案卡）
        val isMatch = card1.number.number == card2.number.number && 
                     card1.isNumberCard != card2.isNumberCard
        
        if (isMatch) {
            // 匹配成功
            val updatedCards = currentState.cards.map {
                if (it.id == card1.id || it.id == card2.id) {
                    it.copy(isMatched = true)
                } else it
            }
            
            val newMatchCount = currentState.matchCount + 1
            val isGameComplete = newMatchCount == currentState.totalPairs
            
            _uiState.value = currentState.copy(
                cards = updatedCards,
                selectedCards = emptyList(),
                matchCount = newMatchCount,
                isGameComplete = isGameComplete
            )

            // 游戏完成时播放恭喜音效
            if (isGameComplete) {
                viewModelScope.launch {
                    delay(500)
                    audioPlayer.playRawAudio(R.raw.game_challenge)
                }
            }
        } else {
            // 匹配失败，延迟翻回
            viewModelScope.launch {
                delay(Constants.ANIMATION_DURATION_MEDIUM.toLong())
                val updatedCards = _uiState.value.cards.map {
                    if (it.id == card1.id || it.id == card2.id) {
                        it.copy(isFlipped = false)
                    } else it
                }
                _uiState.value = _uiState.value.copy(
                    cards = updatedCards,
                    selectedCards = emptyList()
                )
            }
        }
    }

    fun resetGame() {
        startGame(_uiState.value.currentStage)
    }
}

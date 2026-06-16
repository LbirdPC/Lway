package com.morningsun.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morningsun.app.domain.model.*
import com.morningsun.app.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val recordRepository: HabitRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                habitRepository.getAllHabits(),
                recordRepository.getTodayRecords(),
                recordRepository.getCurrentStreak()
            ) { habits, todayRecords, streak ->
                HomeUiState(
                    habits = habits,
                    todayRecords = todayRecords,
                    currentStreak = streak,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}

data class HomeUiState(
    val habits: List<Habit> = emptyList(),
    val todayRecords: List<HabitRecord> = emptyList(),
    val currentStreak: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val recordRepository: HabitRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitsUiState())
    val uiState: StateFlow<HabitsUiState> = _uiState.asStateFlow()

    init {
        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            habitRepository.getAllHabits().collect { habits ->
                _uiState.update { it.copy(habits = habits, isLoading = false) }
            }
        }
    }

    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            habitRepository.insertHabit(habit)
        }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch {
            habitRepository.deleteHabit(id)
        }
    }
}

data class HabitsUiState(
    val habits: List<Habit> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class CheckInViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val recordRepository: HabitRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInUiState())
    val uiState: StateFlow<CheckInUiState> = _uiState.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                habitRepository.getAllHabits(),
                recordRepository.getTodayRecords()
            ) { habits, records ->
                CheckInUiState(
                    habits = habits,
                    todayRecords = records,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun startTimer(habitId: Long) {
        _timerState.value = TimerState(
            habitId = habitId,
            isRunning = true,
            startTime = System.currentTimeMillis()
        )
    }

    fun stopTimer() {
        _timerState.update {
            val elapsed = (System.currentTimeMillis() - it.startTime) / 1000 / 60
            it.copy(isRunning = false, elapsedMinutes = elapsed.toInt())
        }
    }

    fun saveRecord(habitId: Long, durationMinutes: Int, note: String? = null) {
        viewModelScope.launch {
            val record = HabitRecord(
                habitId = habitId,
                date = LocalDate.now(),
                durationMinutes = durationMinutes,
                isCompleted = true
            )
            recordRepository.insertRecord(record)
            _timerState.value = TimerState()
        }
    }
}

data class CheckInUiState(
    val habits: List<Habit> = emptyList(),
    val todayRecords: List<HabitRecord> = emptyList(),
    val isLoading: Boolean = true
)

data class TimerState(
    val habitId: Long = 0,
    val isRunning: Boolean = false,
    val startTime: Long = 0,
    val elapsedMinutes: Int = 0
)

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    init {
        loadDiaries()
    }

    private fun loadDiaries() {
        viewModelScope.launch {
            diaryRepository.getAllDiaries().collect { diaries ->
                _uiState.update { it.copy(diaries = diaries, isLoading = false) }
            }
        }
    }

    fun addDiary(diary: DiaryEntry) {
        viewModelScope.launch {
            diaryRepository.insertDiary(diary)
        }
    }

    fun deleteDiary(id: Long) {
        viewModelScope.launch {
            diaryRepository.deleteDiary(id)
        }
    }
}

data class DiaryUiState(
    val diaries: List<DiaryEntry> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val recordRepository: HabitRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            val year = LocalDate.now().year
            combine(
                habitRepository.getAllHabits(),
                recordRepository.getYearlyHeatmap(year),
                recordRepository.getCurrentStreak()
            ) { habits, heatmap, streak ->
                StatisticsUiState(
                    habits = habits,
                    yearlyHeatmap = heatmap,
                    currentStreak = streak,
                    selectedYear = year,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}

data class StatisticsUiState(
    val habits: List<Habit> = emptyList(),
    val yearlyHeatmap: List<YearlyHeatmap> = emptyList(),
    val currentStreak: Int = 0,
    val selectedYear: Int = LocalDate.now().year,
    val isLoading: Boolean = true
)

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState: StateFlow<AchievementsUiState> = _uiState.asStateFlow()

    init {
        loadAchievements()
    }

    private fun loadAchievements() {
        viewModelScope.launch {
            achievementRepository.getAllAchievements().collect { achievements ->
                _uiState.update {
                    it.copy(
                        achievements = achievements,
                        unlockedCount = achievements.count { a -> a.unlockedAt != null },
                        isLoading = false
                    )
                }
            }
        }
    }
}

data class AchievementsUiState(
    val achievements: List<Achievement> = emptyList(),
    val unlockedCount: Int = 0,
    val isLoading: Boolean = true
)

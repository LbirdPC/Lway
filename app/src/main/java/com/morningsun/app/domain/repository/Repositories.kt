package com.morningsun.app.domain.repository

import com.morningsun.app.domain.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    fun getHabitById(id: Long): Flow<Habit?>
    fun getHabitsByCategory(category: HabitCategory): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(id: Long)
}

interface HabitRecordRepository {
    fun getRecordsByHabitId(habitId: Long): Flow<List<HabitRecord>>
    fun getRecordsByDate(date: LocalDate): Flow<List<HabitRecord>>
    fun getRecordsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<HabitRecord>>
    fun getTodayRecords(): Flow<List<HabitRecord>>
    suspend fun insertRecord(record: HabitRecord): Long
    suspend fun updateRecord(record: HabitRecord)
    suspend fun deleteRecord(id: Long)
    fun getTotalDaysByHabitId(habitId: Long): Flow<Int>
    fun getTotalMinutesByHabitId(habitId: Long): Flow<Int>
    fun getYearlyHeatmap(year: Int): Flow<List<YearlyHeatmap>>
    fun getCurrentStreak(): Flow<Int>
}

interface DiaryRepository {
    fun getAllDiaries(): Flow<List<DiaryEntry>>
    fun getDiariesByDate(date: LocalDate): Flow<List<DiaryEntry>>
    fun getDiariesByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DiaryEntry>>
    fun getDiaryById(id: Long): Flow<DiaryEntry?>
    suspend fun insertDiary(diary: DiaryEntry): Long
    suspend fun updateDiary(diary: DiaryEntry)
    suspend fun deleteDiary(id: Long)
}

interface AchievementRepository {
    fun getAllAchievements(): Flow<List<Achievement>>
    fun getUnlockedAchievements(): Flow<List<Achievement>>
    suspend fun unlockAchievement(id: String)
    suspend fun updateProgress(id: String, progress: Int)
}

interface UserRepository {
    fun getCurrentUser(): Flow<UserProfile?>
    suspend fun updateUser(profile: UserProfile)
    suspend fun signInWithEmail(email: String, password: String): Result<UserProfile>
    suspend fun signInWithPhone(phone: String, password: String): Result<UserProfile>
    suspend fun signUpWithEmail(email: String, password: String): Result<UserProfile>
    suspend fun signOut()
    suspend fun syncToCloud()
    suspend fun restoreFromCloud()
}

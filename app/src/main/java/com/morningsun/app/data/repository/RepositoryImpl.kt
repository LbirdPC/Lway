package com.morningsun.app.data.repository

import com.morningsun.app.domain.model.Achievement
import com.morningsun.app.domain.model.DiaryEntry
import com.morningsun.app.domain.model.Habit
import com.morningsun.app.domain.model.HabitCategory
import com.morningsun.app.domain.model.HabitRecord
import com.morningsun.app.domain.model.UserProfile
import com.morningsun.app.domain.model.YearlyHeatmap
import com.morningsun.app.domain.repository.AchievementRepository
import com.morningsun.app.domain.repository.DiaryRepository
import com.morningsun.app.domain.repository.HabitRecordRepository
import com.morningsun.app.domain.repository.HabitRepository
import com.morningsun.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepositoryImpl @Inject constructor() : HabitRepository {

    private val habits = MutableStateFlow(sampleHabits())

    override fun getAllHabits(): Flow<List<Habit>> = habits.asStateFlow()

    override fun getHabitById(id: Long): Flow<Habit?> =
        habits.map { items -> items.find { it.id == id } }

    override fun getHabitsByCategory(category: HabitCategory): Flow<List<Habit>> =
        habits.map { items -> items.filter { it.category == category } }

    override suspend fun insertHabit(habit: Habit): Long {
        val newId = habits.value.maxOfOrNull(Habit::id)?.plus(1) ?: 1L
        habits.value = habits.value + habit.copy(id = newId)
        return newId
    }

    override suspend fun updateHabit(habit: Habit) {
        habits.value = habits.value.map { existing ->
            if (existing.id == habit.id) habit else existing
        }
    }

    override suspend fun deleteHabit(id: Long) {
        habits.value = habits.value.filterNot { it.id == id }
    }

    private fun sampleHabits(): List<Habit> = listOf(
        Habit(id = 1, name = "Posture Reset", category = HabitCategory.POSTURE, targetMinutes = 15),
        Habit(id = 2, name = "Trading Review", category = HabitCategory.TRADING, targetMinutes = 30),
        Habit(id = 3, name = "Sleep Routine", category = HabitCategory.SLEEP, targetMinutes = 20),
        Habit(id = 4, name = "Workout", category = HabitCategory.EXERCISE, targetMinutes = 45),
        Habit(id = 5, name = "Reading", category = HabitCategory.READING, targetMinutes = 25)
    )
}

@Singleton
class HabitRecordRepositoryImpl @Inject constructor() : HabitRecordRepository {

    private val records = MutableStateFlow(sampleRecords())

    override fun getRecordsByHabitId(habitId: Long): Flow<List<HabitRecord>> =
        records.map { items -> items.filter { it.habitId == habitId } }

    override fun getRecordsByDate(date: LocalDate): Flow<List<HabitRecord>> =
        records.map { items -> items.filter { it.date == date } }

    override fun getRecordsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<HabitRecord>> =
        records.map { items ->
            items.filter { !it.date.isBefore(startDate) && !it.date.isAfter(endDate) }
        }

    override fun getTodayRecords(): Flow<List<HabitRecord>> =
        records.map { items -> items.filter { it.date == LocalDate.now() } }

    override suspend fun insertRecord(record: HabitRecord): Long {
        val newId = records.value.maxOfOrNull(HabitRecord::id)?.plus(1) ?: 1L
        records.value = records.value + record.copy(id = newId)
        return newId
    }

    override suspend fun updateRecord(record: HabitRecord) {
        records.value = records.value.map { existing ->
            if (existing.id == record.id) record else existing
        }
    }

    override suspend fun deleteRecord(id: Long) {
        records.value = records.value.filterNot { it.id == id }
    }

    override fun getTotalDaysByHabitId(habitId: Long): Flow<Int> =
        records.map { items -> items.count { it.habitId == habitId && it.isCompleted } }

    override fun getTotalMinutesByHabitId(habitId: Long): Flow<Int> =
        records.map { items -> items.filter { it.habitId == habitId }.sumOf(HabitRecord::durationMinutes) }

    override fun getYearlyHeatmap(year: Int): Flow<List<YearlyHeatmap>> =
        records.map { items ->
            items
                .filter { it.date.year == year }
                .groupBy(HabitRecord::date)
                .map { (date, dayRecords) ->
                    YearlyHeatmap(
                        date = date,
                        intensity = minOf(dayRecords.sumOf(HabitRecord::durationMinutes) / 30, 4),
                        habitsCompleted = dayRecords.count(HabitRecord::isCompleted),
                        totalMinutes = dayRecords.sumOf(HabitRecord::durationMinutes)
                    )
                }
                .sortedBy(YearlyHeatmap::date)
        }

    override fun getCurrentStreak(): Flow<Int> =
        records.map { items ->
            var streak = 0
            var currentDate = LocalDate.now()
            while (items.any { it.date == currentDate && it.isCompleted }) {
                streak++
                currentDate = currentDate.minusDays(1)
            }
            streak
        }

    private fun sampleRecords(): List<HabitRecord> {
        val today = LocalDate.now()
        return listOf(
            HabitRecord(id = 1, habitId = 1, date = today, durationMinutes = 15, isCompleted = true),
            HabitRecord(id = 2, habitId = 2, date = today.minusDays(1), durationMinutes = 25, isCompleted = true),
            HabitRecord(id = 3, habitId = 4, date = today.minusDays(2), durationMinutes = 40, isCompleted = true),
            HabitRecord(id = 4, habitId = 5, date = today, durationMinutes = 20, isCompleted = true)
        )
    }
}

@Singleton
class DiaryRepositoryImpl @Inject constructor() : DiaryRepository {

    private val diaries = MutableStateFlow(
        listOf(
            DiaryEntry(
                id = 1,
                date = LocalDate.now(),
                title = "Small Wins",
                content = "Finished the key habits for today and kept good momentum.",
                mood = "Calm",
                tags = listOf("habit", "review")
            )
        )
    )

    override fun getAllDiaries(): Flow<List<DiaryEntry>> = diaries.asStateFlow()

    override fun getDiariesByDate(date: LocalDate): Flow<List<DiaryEntry>> =
        diaries.map { items -> items.filter { it.date == date } }

    override fun getDiariesByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<DiaryEntry>> =
        diaries.map { items ->
            items.filter { !it.date.isBefore(startDate) && !it.date.isAfter(endDate) }
        }

    override fun getDiaryById(id: Long): Flow<DiaryEntry?> =
        diaries.map { items -> items.find { it.id == id } }

    override suspend fun insertDiary(diary: DiaryEntry): Long {
        val newId = diaries.value.maxOfOrNull(DiaryEntry::id)?.plus(1) ?: 1L
        diaries.value = diaries.value + diary.copy(id = newId)
        return newId
    }

    override suspend fun updateDiary(diary: DiaryEntry) {
        diaries.value = diaries.value.map { existing ->
            if (existing.id == diary.id) diary else existing
        }
    }

    override suspend fun deleteDiary(id: Long) {
        diaries.value = diaries.value.filterNot { it.id == id }
    }
}

@Singleton
class AchievementRepositoryImpl @Inject constructor() : AchievementRepository {

    private val achievements = MutableStateFlow(
        listOf(
            Achievement(
                id = "first-checkin",
                name = "First Check-in",
                description = "Complete your first habit.",
                iconRes = "trophy",
                unlockedAt = LocalDateTime.now().minusDays(3),
                progress = 1,
                targetValue = 1
            ),
            Achievement(
                id = "reading-streak",
                name = "Reading Streak",
                description = "Read for 7 days.",
                iconRes = "book",
                progress = 3,
                targetValue = 7,
                category = HabitCategory.READING
            )
        )
    )

    override fun getAllAchievements(): Flow<List<Achievement>> = achievements.asStateFlow()

    override fun getUnlockedAchievements(): Flow<List<Achievement>> =
        achievements.map { items -> items.filter { it.unlockedAt != null } }

    override suspend fun unlockAchievement(id: String) {
        achievements.value = achievements.value.map { achievement ->
            if (achievement.id == id) achievement.copy(unlockedAt = LocalDateTime.now()) else achievement
        }
    }

    override suspend fun updateProgress(id: String, progress: Int) {
        achievements.value = achievements.value.map { achievement ->
            if (achievement.id == id) achievement.copy(progress = progress) else achievement
        }
    }
}

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {

    private val currentUser = MutableStateFlow<UserProfile?>(null)

    override fun getCurrentUser(): Flow<UserProfile?> = currentUser.asStateFlow()

    override suspend fun updateUser(profile: UserProfile) {
        currentUser.value = profile
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<UserProfile> {
        val user = UserProfile(id = email, email = email, nickname = "MorningSun User")
        currentUser.value = user
        return Result.success(user)
    }

    override suspend fun signInWithPhone(phone: String, password: String): Result<UserProfile> {
        val user = UserProfile(id = phone, phone = phone, nickname = "MorningSun User")
        currentUser.value = user
        return Result.success(user)
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<UserProfile> {
        val user = UserProfile(id = email, email = email, nickname = "MorningSun User")
        currentUser.value = user
        return Result.success(user)
    }

    override suspend fun signOut() {
        currentUser.value = null
    }

    override suspend fun syncToCloud() {
        // Stub implementation for local development.
    }

    override suspend fun restoreFromCloud() {
        // Stub implementation for local development.
    }
}

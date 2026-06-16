package com.morningsun.app.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

enum class HabitCategory {
    POSTURE,
    TRADING,
    SLEEP,
    EXERCISE,
    READING
}

data class Habit(
    val id: Long = 0,
    val name: String,
    val category: HabitCategory,
    val targetMinutes: Int = 30,
    val iconRes: String = "",
    val colorHex: String = "#1A5F5A",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isActive: Boolean = true
)

data class HabitRecord(
    val id: Long = 0,
    val habitId: Long,
    val date: LocalDate,
    val durationMinutes: Int = 0,
    val sleepTime: String? = null,
    val wakeTime: String? = null,
    val note: String? = null,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class DiaryEntry(
    val id: Long = 0,
    val date: LocalDate,
    val title: String,
    val content: String,
    val mood: String? = null,
    val imageUrls: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val relatedHabitIds: List<Long> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val iconRes: String,
    val unlockedAt: LocalDateTime? = null,
    val progress: Int = 0,
    val targetValue: Int = 1,
    val category: HabitCategory? = null
)

data class UserProfile(
    val id: String = "",
    val email: String? = null,
    val phone: String? = null,
    val nickname: String = "MorningSun User",
    val avatarUrl: String? = null,
    val totalDaysActive: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class DailyStats(
    val date: LocalDate,
    val habitsCompleted: Int,
    val totalHabits: Int,
    val totalMinutes: Int,
    val streakDays: Int
)

data class YearlyHeatmap(
    val date: LocalDate,
    val intensity: Int,
    val habitsCompleted: Int,
    val totalMinutes: Int
)

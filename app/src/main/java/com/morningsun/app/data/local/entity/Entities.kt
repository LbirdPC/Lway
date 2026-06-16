package com.morningsun.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val targetMinutes: Int,
    val iconRes: String,
    val colorHex: String,
    val createdAt: String,
    val isActive: Boolean
)

@Entity(tableName = "habit_records")
data class HabitRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val habitId: Long,
    val date: String,
    val durationMinutes: Int,
    val sleepTime: String?,
    val wakeTime: String?,
    val note: String?,
    val isCompleted: Boolean,
    val createdAt: String
)

@Entity(tableName = "diary_entries")
data class DiaryEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val title: String,
    val content: String,
    val mood: String?,
    val imageUrls: String,
    val tags: String,
    val relatedHabitIds: String,
    val createdAt: String,
    val updatedAt: String
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val iconRes: String,
    val unlockedAt: String?,
    val progress: Int,
    val targetValue: Int,
    val category: String?
)

package com.morningsun.app.data.local

import com.morningsun.app.data.local.entity.*
import com.morningsun.app.domain.model.*
import org.json.JSONArray
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Mappers {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Habit mappers
    fun HabitEntity.toDomain() = Habit(
        id = id,
        name = name,
        category = HabitCategory.valueOf(category),
        targetMinutes = targetMinutes,
        iconRes = iconRes,
        colorHex = colorHex,
        createdAt = LocalDateTime.parse(createdAt, dateTimeFormatter),
        isActive = isActive
    )

    fun Habit.toEntity() = HabitEntity(
        id = id,
        name = name,
        category = category.name,
        targetMinutes = targetMinutes,
        iconRes = iconRes,
        colorHex = colorHex,
        createdAt = createdAt.format(dateTimeFormatter),
        isActive = isActive
    )

    // HabitRecord mappers
    fun HabitRecordEntity.toDomain() = HabitRecord(
        id = id,
        habitId = habitId,
        date = LocalDate.parse(date, dateFormatter),
        durationMinutes = durationMinutes,
        sleepTime = sleepTime,
        wakeTime = wakeTime,
        note = note,
        isCompleted = isCompleted,
        createdAt = LocalDateTime.parse(createdAt, dateTimeFormatter)
    )

    fun HabitRecord.toEntity() = HabitRecordEntity(
        id = id,
        habitId = habitId,
        date = date.format(dateFormatter),
        durationMinutes = durationMinutes,
        sleepTime = sleepTime,
        wakeTime = wakeTime,
        note = note,
        isCompleted = isCompleted,
        createdAt = createdAt.format(dateTimeFormatter)
    )

    // DiaryEntry mappers
    fun DiaryEntryEntity.toDomain() = DiaryEntry(
        id = id,
        date = LocalDate.parse(date, dateFormatter),
        title = title,
        content = content,
        mood = mood,
        imageUrls = parseJsonArray(imageUrls),
        tags = parseJsonArray(tags),
        relatedHabitIds = parseJsonArray(relatedHabitIds).map { it.toLong() },
        createdAt = LocalDateTime.parse(createdAt, dateTimeFormatter),
        updatedAt = LocalDateTime.parse(updatedAt, dateTimeFormatter)
    )

    fun DiaryEntry.toEntity() = DiaryEntryEntity(
        id = id,
        date = date.format(dateFormatter),
        title = title,
        content = content,
        mood = mood,
        imageUrls = toJsonArray(imageUrls),
        tags = toJsonArray(tags),
        relatedHabitIds = toJsonArray(relatedHabitIds.map { it.toString() }),
        createdAt = createdAt.format(dateTimeFormatter),
        updatedAt = updatedAt.format(dateTimeFormatter)
    )

    // Achievement mappers
    fun AchievementEntity.toDomain() = Achievement(
        id = id,
        name = name,
        description = description,
        iconRes = iconRes,
        unlockedAt = unlockedAt?.let { LocalDateTime.parse(it, dateTimeFormatter) },
        progress = progress,
        targetValue = targetValue,
        category = category?.let { HabitCategory.valueOf(it) }
    )

    fun Achievement.toEntity() = AchievementEntity(
        id = id,
        name = name,
        description = description,
        iconRes = iconRes,
        unlockedAt = unlockedAt?.format(dateTimeFormatter),
        progress = progress,
        targetValue = targetValue,
        category = category?.name
    )

    private fun parseJsonArray(json: String): List<String> {
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { array.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun toJsonArray(list: List<String>): String {
        return JSONArray(list).toString()
    }
}

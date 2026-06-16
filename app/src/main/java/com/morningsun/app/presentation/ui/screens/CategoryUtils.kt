package com.morningsun.app.presentation.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.morningsun.app.domain.model.HabitCategory

fun getCategoryIcon(category: HabitCategory): ImageVector = when (category) {
    HabitCategory.POSTURE -> Icons.Default.Accessibility
    HabitCategory.TRADING -> Icons.Default.TrendingUp
    HabitCategory.SLEEP -> Icons.Default.Bedtime
    HabitCategory.EXERCISE -> Icons.Default.FitnessCenter
    HabitCategory.READING -> Icons.Default.MenuBook
}

fun getCategoryName(category: HabitCategory): String = when (category) {
    HabitCategory.POSTURE -> "Posture"
    HabitCategory.TRADING -> "Trading"
    HabitCategory.SLEEP -> "Sleep"
    HabitCategory.EXERCISE -> "Exercise"
    HabitCategory.READING -> "Reading"
}

fun getCategoryColor(category: HabitCategory): String = when (category) {
    HabitCategory.POSTURE -> "#4ECDC4"
    HabitCategory.TRADING -> "#FF6B6B"
    HabitCategory.SLEEP -> "#9B59B6"
    HabitCategory.EXERCISE -> "#E67E22"
    HabitCategory.READING -> "#3498DB"
}

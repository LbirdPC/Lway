package com.morningsun.app.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.morningsun.app.domain.model.HabitCategory
import com.morningsun.app.presentation.localization.LocalAppStrings

fun getCategoryIcon(category: HabitCategory): ImageVector = when (category) {
    HabitCategory.POSTURE -> Icons.Default.Accessibility
    HabitCategory.TRADING -> Icons.Default.TrendingUp
    HabitCategory.SLEEP -> Icons.Default.Bedtime
    HabitCategory.EXERCISE -> Icons.Default.FitnessCenter
    HabitCategory.READING -> Icons.Default.MenuBook
}

@Composable
fun getCategoryName(category: HabitCategory): String = when (category) {
    HabitCategory.POSTURE -> LocalAppStrings.current.categoryPosture
    HabitCategory.TRADING -> LocalAppStrings.current.categoryTrading
    HabitCategory.SLEEP -> LocalAppStrings.current.categorySleep
    HabitCategory.EXERCISE -> LocalAppStrings.current.categoryExercise
    HabitCategory.READING -> LocalAppStrings.current.categoryReading
}

fun getCategoryColor(category: HabitCategory): String = when (category) {
    HabitCategory.POSTURE -> "#4ECDC4"
    HabitCategory.TRADING -> "#FF6B6B"
    HabitCategory.SLEEP -> "#9B59B6"
    HabitCategory.EXERCISE -> "#E67E22"
    HabitCategory.READING -> "#3498DB"
}

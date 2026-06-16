package com.morningsun.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.morningsun.app.presentation.localization.AppStrings

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Habits : Screen("habits", "Habits", Icons.Filled.CheckCircle, Icons.Outlined.CheckCircle)
    data object CheckIn : Screen("checkin", "Check In", Icons.Filled.PlayCircle, Icons.Outlined.PlayCircle)
    data object Diary : Screen("diary", "Diary", Icons.Filled.Article, Icons.Outlined.Article)
    data object Profile : Screen("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
    data object HabitDetail : Screen("habit/{habitId}", "Habit Detail", Icons.Filled.Info, Icons.Outlined.Info) {
        fun createRoute(habitId: Long) = "habit/$habitId"
    }
    data object DiaryDetail : Screen("diary/{diaryId}", "Diary Detail", Icons.Filled.Article, Icons.Outlined.Article) {
        fun createRoute(diaryId: Long) = "diary/$diaryId"
    }
    data object Statistics : Screen("statistics", "Statistics", Icons.Filled.BarChart, Icons.Outlined.BarChart)
    data object Achievements : Screen("achievements", "Achievements", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents)

    companion object {
        val bottomNavItems = listOf(Home, Habits, CheckIn, Diary, Profile)
    }
}

fun Screen.localizedTitle(strings: AppStrings): String = when (this) {
    Screen.Home -> strings.navHome
    Screen.Habits -> strings.navHabits
    Screen.CheckIn -> strings.navCheckIn
    Screen.Diary -> strings.navDiary
    Screen.Profile -> strings.navProfile
    Screen.HabitDetail -> strings.habitDetail
    Screen.DiaryDetail -> strings.diaryDetail
    Screen.Statistics -> strings.statistics
    Screen.Achievements -> strings.achievements
}

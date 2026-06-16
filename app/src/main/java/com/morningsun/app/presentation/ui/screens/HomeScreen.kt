package com.morningsun.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.morningsun.app.domain.model.Habit
import com.morningsun.app.domain.model.HabitCategory
import com.morningsun.app.domain.model.HabitRecord
import com.morningsun.app.presentation.localization.appStrings
import com.morningsun.app.presentation.ui.theme.Accent
import com.morningsun.app.presentation.ui.theme.OnPrimary
import com.morningsun.app.presentation.ui.theme.PostureColor
import com.morningsun.app.presentation.ui.theme.Primary
import com.morningsun.app.presentation.ui.theme.ReadingColor
import com.morningsun.app.presentation.ui.theme.Secondary
import com.morningsun.app.presentation.ui.theme.SleepColor
import com.morningsun.app.presentation.ui.theme.TradingColor
import com.morningsun.app.presentation.ui.theme.ExerciseColor
import com.morningsun.app.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToStatistics: () -> Unit,
    onNavigateToAchievements: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = appStrings()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = strings.homeTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = strings.homeSubtitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = Primary
                    )
                }
                IconButton(onClick = onNavigateToAchievements) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = strings.achievements, tint = Secondary)
                }
            }
        }

        item {
            StreakCard(streak = uiState.currentStreak)
        }

        item {
            TodayProgressCard(habits = uiState.habits, records = uiState.todayRecords)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateToAchievements,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PlayCircle, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(strings.achievements)
                }
                Button(
                    onClick = onNavigateToStatistics,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.BarChart, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(strings.statistics)
                }
            }
        }

        item {
            Text(
                text = strings.todaysHabits,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        items(uiState.habits.take(5)) { habit ->
            HabitQuickCard(
                habit = habit,
                isCompleted = uiState.todayRecords.any { it.habitId == habit.id && it.isCompleted }
            )
        }
    }
}

@Composable
fun StreakCard(streak: Int) {
    val strings = appStrings()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = if (streak > 0) Secondary else Color.Gray
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(
                    text = "$streak ${strings.days}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = OnPrimary
                )
                Text(
                    text = strings.currentStreak,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnPrimary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun TodayProgressCard(
    habits: List<Habit>,
    records: List<HabitRecord>
) {
    val strings = appStrings()
    val completed = records.count { it.isCompleted }
    val total = habits.size
    val progress = if (total > 0) completed.toFloat() / total else 0f

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(strings.todaysProgress, style = MaterialTheme.typography.titleMedium)
                Text("$completed / $total", style = MaterialTheme.typography.titleMedium, color = Primary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Accent,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
fun HabitQuickCard(
    habit: Habit,
    isCompleted: Boolean
) {
    val strings = appStrings()
    val categoryColor = when (habit.category) {
        HabitCategory.POSTURE -> PostureColor
        HabitCategory.TRADING -> TradingColor
        HabitCategory.SLEEP -> SleepColor
        HabitCategory.EXERCISE -> ExerciseColor
        HabitCategory.READING -> ReadingColor
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(categoryColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.Timer,
                    contentDescription = null,
                    tint = if (isCompleted) Accent else categoryColor
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = habit.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = getCategoryName(habit.category),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = strings.completed,
                    tint = Accent
                )
            }
        }
    }
}

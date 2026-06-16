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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.morningsun.app.domain.model.HabitCategory
import com.morningsun.app.presentation.ui.theme.Accent
import com.morningsun.app.presentation.ui.theme.ExerciseColor
import com.morningsun.app.presentation.ui.theme.PostureColor
import com.morningsun.app.presentation.ui.theme.Primary
import com.morningsun.app.presentation.ui.theme.ReadingColor
import com.morningsun.app.presentation.ui.theme.Secondary
import com.morningsun.app.presentation.ui.theme.SleepColor
import com.morningsun.app.presentation.ui.theme.TradingColor
import com.morningsun.app.presentation.viewmodel.DiaryViewModel
import com.morningsun.app.presentation.viewmodel.HomeViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: Long,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val habit = uiState.habits.find { it.id == habitId }

    val categoryColor = when (habit?.category) {
        HabitCategory.POSTURE -> PostureColor
        HabitCategory.TRADING -> TradingColor
        HabitCategory.SLEEP -> SleepColor
        HabitCategory.EXERCISE -> ExerciseColor
        HabitCategory.READING -> ReadingColor
        null -> Primary
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(habit?.name ?: "Habit Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (habit == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Habit not found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(categoryColor.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getCategoryIcon(habit.category),
                                    contentDescription = null,
                                    tint = categoryColor,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(habit.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Text(getCategoryName(habit.category), style = MaterialTheme.typography.bodyMedium, color = categoryColor)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Target ${habit.targetMinutes} min / day",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                item {
                    Text("Today's Status", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }

                val todayRecord = uiState.todayRecords.find { it.habitId == habitId }
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (todayRecord?.isCompleted == true) Icons.Default.CheckCircle else Icons.Default.Timer,
                                contentDescription = null,
                                tint = if (todayRecord?.isCompleted == true) Accent else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Column {
                                Text(
                                    if (todayRecord?.isCompleted == true) "Completed" else "Not checked in yet",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                if (todayRecord != null) {
                                    Text(
                                        "Logged ${todayRecord.durationMinutes} minutes today",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryDetailScreen(
    diaryId: Long,
    viewModel: DiaryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val diary = uiState.diaries.find { it.id == diaryId }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy MMM dd, EEE") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diary Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (diary == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Diary entry not found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(diary.date.format(dateFormatter), style = MaterialTheme.typography.labelMedium, color = Primary)
                }
                item {
                    Text(diary.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
                diary.mood?.let { mood ->
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Mood: ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(mood, style = MaterialTheme.typography.bodyMedium, color = getMoodColor(mood))
                        }
                    }
                }
                if (diary.tags.isNotEmpty()) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            diary.tags.forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .background(Secondary.copy(alpha = 0.2f), shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("#$tag", style = MaterialTheme.typography.labelMedium, color = Secondary)
                                }
                            }
                        }
                    }
                }
                item { Divider() }
                item {
                    Text(diary.content, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

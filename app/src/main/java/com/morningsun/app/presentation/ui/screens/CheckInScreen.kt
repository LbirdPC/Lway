package com.morningsun.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.morningsun.app.domain.model.HabitCategory
import com.morningsun.app.presentation.localization.appStrings
import com.morningsun.app.presentation.ui.theme.Accent
import com.morningsun.app.presentation.ui.theme.ExerciseColor
import com.morningsun.app.presentation.ui.theme.OnPrimary
import com.morningsun.app.presentation.ui.theme.PostureColor
import com.morningsun.app.presentation.ui.theme.Primary
import com.morningsun.app.presentation.ui.theme.ReadingColor
import com.morningsun.app.presentation.ui.theme.Secondary
import com.morningsun.app.presentation.ui.theme.SleepColor
import com.morningsun.app.presentation.ui.theme.TradingColor
import com.morningsun.app.presentation.viewmodel.CheckInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    viewModel: CheckInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val strings = appStrings()
    var selectedHabitId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(strings.checkInTitle) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (timerState.isRunning) Primary else MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val elapsedSeconds = if (timerState.isRunning) {
                        ((System.currentTimeMillis() - timerState.startTime) / 1000).toInt()
                    } else {
                        timerState.elapsedMinutes * 60
                    }
                    val minutes = elapsedSeconds / 60
                    val seconds = elapsedSeconds % 60

                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 64.sp,
                        color = if (timerState.isRunning) OnPrimary else MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (timerState.isRunning) {
                        Button(
                            onClick = { viewModel.stopTimer() },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Secondary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = null)
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(strings.stop)
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { selectedHabitId?.let(viewModel::startTimer) },
                                modifier = Modifier.weight(1f),
                                enabled = selectedHabitId != null
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(strings.start)
                            }

                            if (timerState.elapsedMinutes > 0) {
                                Button(
                                    onClick = {
                                        selectedHabitId?.let {
                                            viewModel.saveRecord(it, timerState.elapsedMinutes)
                                            selectedHabitId = null
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Accent)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(strings.save)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(strings.chooseHabit, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.habits) { habit ->
                    val isSelected = selectedHabitId == habit.id
                    val isCompleted = uiState.todayRecords.any { it.habitId == habit.id && it.isCompleted }
                    val categoryColor = when (habit.category) {
                        HabitCategory.POSTURE -> PostureColor
                        HabitCategory.TRADING -> TradingColor
                        HabitCategory.SLEEP -> SleepColor
                        HabitCategory.EXERCISE -> ExerciseColor
                        HabitCategory.READING -> ReadingColor
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedHabitId = if (isSelected) null else habit.id },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) categoryColor.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
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
                                    imageVector = getCategoryIcon(habit.category),
                                    contentDescription = null,
                                    tint = categoryColor
                                )
                            }
                            Spacer(modifier = Modifier.size(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(habit.name, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    getCategoryName(habit.category),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (isCompleted) {
                                Icon(Icons.Default.CheckCircle, contentDescription = strings.completed, tint = Accent)
                            }
                            if (isSelected) {
                                Spacer(modifier = Modifier.size(8.dp))
                                Icon(Icons.Default.Timer, contentDescription = strings.selected, tint = categoryColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

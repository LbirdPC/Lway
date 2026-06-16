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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.morningsun.app.domain.model.Achievement
import com.morningsun.app.presentation.localization.appStrings
import com.morningsun.app.presentation.ui.theme.Accent
import com.morningsun.app.presentation.ui.theme.Primary
import com.morningsun.app.presentation.ui.theme.Secondary
import com.morningsun.app.presentation.viewmodel.AchievementsViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    viewModel: AchievementsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = appStrings()
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.achievementsTitle) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = strings.back)
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Secondary.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Column {
                                Text(
                                    text = strings.unlocked.format(uiState.unlockedCount, uiState.achievements.size),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = strings.keepGoing,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                item {
                    Text(strings.achievementList, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }

                items(uiState.achievements) { achievement ->
                    AchievementCard(achievement = achievement, dateFormatter = dateFormatter)
                }
            }
        }
    }
}

@Composable
fun AchievementCard(
    achievement: Achievement,
    dateFormatter: DateTimeFormatter
) {
    val strings = appStrings()
    val isUnlocked = achievement.unlockedAt != null

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Accent.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
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
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) Secondary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getAchievementIcon(achievement.iconRes),
                    contentDescription = null,
                    tint = if (isUnlocked) Secondary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = achievement.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (isUnlocked && achievement.unlockedAt != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = strings.unlockedOn.format(achievement.unlockedAt.format(dateFormatter)),
                        style = MaterialTheme.typography.labelSmall,
                        color = Accent
                    )
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = achievement.progress.toFloat() / achievement.targetValue.coerceAtLeast(1),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = Primary.copy(alpha = 0.5f),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = "${achievement.progress} / ${achievement.targetValue}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isUnlocked) {
                Icon(Icons.Default.CheckCircle, contentDescription = strings.unlocked.format(1, 1), tint = Accent)
            }
        }
    }
}

fun getAchievementIcon(iconRes: String): ImageVector = when (iconRes) {
    "fire" -> Icons.Default.LocalFireDepartment
    "star" -> Icons.Default.Star
    "book" -> Icons.Default.MenuBook
    "fitness" -> Icons.Default.FitnessCenter
    "moon" -> Icons.Default.Bedtime
    "trending" -> Icons.Default.TrendingUp
    "accessibility" -> Icons.Default.Accessibility
    "trophy" -> Icons.Default.EmojiEvents
    else -> Icons.Default.Star
}

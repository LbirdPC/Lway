package com.morningsun.app.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.morningsun.app.domain.model.LanguageMode
import com.morningsun.app.domain.model.ThemeMode
import com.morningsun.app.presentation.localization.appStrings
import com.morningsun.app.presentation.ui.theme.Accent
import com.morningsun.app.presentation.ui.theme.Primary
import com.morningsun.app.presentation.ui.theme.Secondary
import com.morningsun.app.presentation.viewmodel.HomeViewModel

@Composable
fun ProfileScreen(
    languageMode: LanguageMode,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    onLanguageModeChange: (LanguageMode) -> Unit,
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
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(strings.appUser, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(strings.slogan, style = MaterialTheme.typography.bodyMedium, color = Primary)
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = "${uiState.currentStreak}",
                    label = strings.streak,
                    icon = Icons.Default.LocalFireDepartment,
                    color = Secondary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = "${uiState.todayRecords.size}",
                    label = strings.today,
                    icon = Icons.Default.CheckCircle,
                    color = Accent
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = "${uiState.habits.size}",
                    label = strings.habits,
                    icon = Icons.Default.List,
                    color = Primary
                )
            }
        }

        item {
            SettingsPanel(
                languageMode = languageMode,
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange,
                onLanguageModeChange = onLanguageModeChange
            )
        }

        item {
            Text(strings.features, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }

        item {
            ProfileMenuItem(Icons.Default.BarChart, strings.yearlyStatistics, strings.reviewHeatmap, onNavigateToStatistics)
        }
        item {
            ProfileMenuItem(Icons.Default.EmojiEvents, strings.achievements, strings.seeMilestones, onNavigateToAchievements)
        }
        item {
            ProfileMenuItem(Icons.Default.Cloud, strings.cloudSync, strings.cloudSyncSubtitle) { }
        }
        item {
            ProfileMenuItem(Icons.Default.FileDownload, strings.exportData, strings.exportDataSubtitle) { }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsPanel(
    languageMode: LanguageMode,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    onLanguageModeChange: (LanguageMode) -> Unit
) {
    val strings = appStrings()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = Primary)
                Spacer(modifier = Modifier.size(12.dp))
                Column {
                    Text(strings.settings, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(strings.settingsSubtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(strings.appearance, style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = themeMode == ThemeMode.LIGHT,
                        onClick = { onThemeModeChange(ThemeMode.LIGHT) },
                        label = { Text(strings.light) },
                        leadingIcon = { Icon(Icons.Default.LightMode, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    FilterChip(
                        selected = themeMode == ThemeMode.DARK,
                        onClick = { onThemeModeChange(ThemeMode.DARK) },
                        label = { Text(strings.dark) },
                        leadingIcon = { Icon(Icons.Default.DarkMode, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(strings.language, style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = languageMode == LanguageMode.EN,
                        onClick = { onLanguageModeChange(LanguageMode.EN) },
                        label = { Text(strings.english) },
                        leadingIcon = { Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    FilterChip(
                        selected = languageMode == LanguageMode.ZH,
                        onClick = { onLanguageModeChange(LanguageMode.ZH) },
                        label = { Text(strings.chinese) },
                        leadingIcon = { Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.size(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

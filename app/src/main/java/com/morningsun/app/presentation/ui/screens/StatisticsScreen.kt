package com.morningsun.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.morningsun.app.domain.model.YearlyHeatmap
import com.morningsun.app.presentation.localization.appStrings
import com.morningsun.app.presentation.ui.theme.HeatmapLevel0
import com.morningsun.app.presentation.ui.theme.HeatmapLevel1
import com.morningsun.app.presentation.ui.theme.HeatmapLevel2
import com.morningsun.app.presentation.ui.theme.HeatmapLevel3
import com.morningsun.app.presentation.ui.theme.HeatmapLevel4
import com.morningsun.app.presentation.ui.theme.OnPrimary
import com.morningsun.app.presentation.ui.theme.Primary
import com.morningsun.app.presentation.viewmodel.StatisticsViewModel
import java.time.LocalDate
import java.time.Month

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = appStrings()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.yearlyStatistics) },
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Primary)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatisticSummaryItem("${uiState.currentStreak}", strings.streak)
                            StatisticSummaryItem("${uiState.yearlyHeatmap.size}", strings.days)
                            StatisticSummaryItem("${uiState.yearlyHeatmap.sumOf { it.totalMinutes } / 60}h", strings.hours)
                        }
                    }
                }

                item {
                    Text(
                        text = "${uiState.selectedYear} ${strings.heatmap}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                item {
                    HeatmapCard(
                        year = uiState.selectedYear,
                        heatmapData = uiState.yearlyHeatmap.associateBy(YearlyHeatmap::date)
                    )
                }

                item {
                    MonthStatsCard(heatmapData = uiState.yearlyHeatmap)
                }
            }
        }
    }
}

@Composable
private fun StatisticSummaryItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = OnPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = OnPrimary.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun HeatmapCard(
    year: Int,
    heatmapData: Map<LocalDate, YearlyHeatmap>
) {
    val strings = appStrings()
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach { month ->
                    Text(
                        text = month,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val firstDay = LocalDate.of(year, 1, 1)
            val lastDay = LocalDate.of(year, 12, 31)
            val startDayOfWeek = firstDay.dayOfWeek.value % 7
            val weeks = mutableListOf<List<LocalDate?>>()
            var currentWeek = MutableList(startDayOfWeek) { null as LocalDate? }
            var currentDate = firstDay

            while (!currentDate.isAfter(lastDay)) {
                currentWeek.add(currentDate)
                if (currentWeek.size == 7) {
                    weeks.add(currentWeek)
                    currentWeek = mutableListOf()
                }
                currentDate = currentDate.plusDays(1)
            }
            if (currentWeek.isNotEmpty()) {
                while (currentWeek.size < 7) currentWeek.add(null)
                weeks.add(currentWeek)
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                weeks.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        week.forEach { date ->
                            if (date != null) {
                                val intensity = heatmapData[date]?.intensity ?: 0
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(getHeatmapColor(intensity))
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(strings.low, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.size(4.dp))
                listOf(0, 1, 2, 3, 4).forEach { level ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(getHeatmapColor(level))
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                }
                Text(strings.high, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun MonthStatsCard(
    heatmapData: List<YearlyHeatmap>
) {
    val strings = appStrings()
    val monthlyData = heatmapData.groupBy { it.date.month }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(strings.monthlySummary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))

            Month.entries.forEach { month ->
                val monthData = monthlyData[month].orEmpty()
                val days = monthData.size
                val hours = monthData.sumOf { it.totalMinutes } / 60

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${month.value}", style = MaterialTheme.typography.bodyMedium)
                    Row {
                        Text("$days ${strings.days}", style = MaterialTheme.typography.bodyMedium, color = Primary)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text("${hours}h", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

fun getHeatmapColor(intensity: Int) = when (intensity) {
    0 -> HeatmapLevel0
    1 -> HeatmapLevel1
    2 -> HeatmapLevel2
    3 -> HeatmapLevel3
    4 -> HeatmapLevel4
    else -> HeatmapLevel0
}

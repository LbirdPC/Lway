package com.morningsun.app.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private const val SPLASH_FADE_MS = 500
private const val SPLASH_HOLD_MS = 1500L

@Composable
fun LwaySplashScreen(
    onFinished: () -> Unit
) {
    var contentVisible by remember { mutableStateOf(false) }
    var screenVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        contentVisible = true
        delay(SPLASH_HOLD_MS)
        contentVisible = false
        screenVisible = false
        delay(SPLASH_FADE_MS.toLong())
        onFinished()
    }

    AnimatedVisibility(
        visible = screenVisible,
        enter = fadeIn(animationSpec = tween(SPLASH_FADE_MS)),
        exit = fadeOut(animationSpec = tween(SPLASH_FADE_MS))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(SPLASH_FADE_MS)),
                exit = fadeOut(animationSpec = tween(SPLASH_FADE_MS)),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Lway",
                        style = TextStyle(
                            color = Color(0xFFF2F2F2),
                            fontSize = 68.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 16.sp,
                            textAlign = TextAlign.Center
                        ),
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Life is a long road. Walk it your way.",
                        color = Color(0xFFD7D7D9),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.4.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "\u4EBA\u751F\u8DEF\u6F2B\u6F2B\uFF0C\u8D70\u4F60\u81EA\u5DF1\u7684\u8DEF\u3002",
                        color = Color(0xFFB8B8BD),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.2.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

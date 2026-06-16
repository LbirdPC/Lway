package com.morningsun.app.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
                Text(
                    text = "Lway",
                    style = TextStyle(
                        color = Color(0xFFE8E8E8),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 8.sp
                    )
                )
            }

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(SPLASH_FADE_MS)),
                exit = fadeOut(animationSpec = tween(SPLASH_FADE_MS)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 32.dp, top = 0.dp, end = 32.dp, bottom = 148.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Life is a long road. Walk it your way.",
                        color = Color(0xFF8E8E93),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 1.2.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "人生路漫漫，走你自己的路。",
                        color = Color(0xFF8E8E93),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

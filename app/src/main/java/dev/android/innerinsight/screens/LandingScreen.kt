package dev.android.innerinsight.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.android.innerinsight.components.Dot
import dev.android.innerinsight.components.LogoImage
import dev.android.innerinsight.components.LogoText
import dev.android.innerinsight.router.Screen
import dev.android.innerinsight.ui.theme.InnerInsightTheme
import kotlinx.coroutines.delay

@Composable
fun LandingScreen(navController: NavHostController) {
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceColorAtElevation(50.dp),
            MaterialTheme.colorScheme.primaryContainer,
        ),
        tileMode = TileMode.Mirror
    )
    LandingScreenContent(
        navController,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    )
}

@Composable
private fun LandingScreenContent(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(0.6f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LandingScreenImage()
            LandingScreenText(modifier = Modifier.padding(10.dp))
        }
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxSize()
                .wrapContentSize(Alignment.BottomCenter)
                .padding(vertical = 16.dp, horizontal = 8.dp),
        ) {
            DotAnimation(navController)
        }
    }
}

@Composable
private fun LandingScreenImage(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LogoImage(
                modifier = Modifier
                    .size(160.dp)
            )
            Dot(
                modifier = Modifier
                    .size(36.dp)
                    .padding(top = 18.dp),
            )
        }
    }
}

@Composable
private fun LandingScreenText(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LogoText(fontSize = 45.sp, letterSpacing = 0.9.sp)
        }
    }
}

@Composable
private fun DotAnimation(navController: NavHostController) {
    val dotAnimation = remember {
        Animatable(0f)
    }
    var dotCount by remember {
        mutableIntStateOf(0)
    }
    var dotLineCycles by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(350)
            dotAnimation.animateTo(dotAnimation.value + 1)
            dotCount++
            if (dotCount > 5) { // show five dots in a row and clear it
                dotCount = 0
                dotLineCycles++
            }
            if (dotLineCycles == 3) { // stops recycling of dots when it recycles for three times
                navController.navigate(Screen.HomeScreen.route)
                break
            }
        }
    }
    for (i in 0 until dotCount) {
        Dot(
            modifier = Modifier
                .size(22.dp)
                .padding(6.dp)
                .offset(x = i * 20.dp - (dotCount.dp * 20 / 2 - 6.dp))
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
fun LandingScreenPreview() {
    val navController = rememberNavController()
    InnerInsightTheme {
        LandingScreen(navController)
    }
}
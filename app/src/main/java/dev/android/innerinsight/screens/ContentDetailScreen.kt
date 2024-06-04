package dev.android.innerinsight.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.android.innerinsight.R
import dev.android.innerinsight.components.ScrollToTop
import dev.android.innerinsight.data.Data
import dev.android.innerinsight.ui.theme.Avenir
import dev.android.innerinsight.ui.theme.InnerInsightTheme
import dev.android.innerinsight.ui.theme.Poppins
import kotlinx.coroutines.launch

@Composable
fun ContentDetailScreen(day: Int, navigateBackwards: () -> Unit) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val visible by remember { derivedStateOf { scrollState.value > 388 } }
    // Log.d("scrollState", "ContentDetailScreen: ${scrollState.value} ${scrollState.maxValue} ")
    ContentDetailScreenContent(
        day = day,
        navigateBackwards = navigateBackwards,
        scrollState = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 12.dp)
    )

    AnimatedVisibility(visible) {
        ScrollToTop(
            onClick = {
                with(coroutineScope) {
                    launch {
                        scrollState.animateScrollTo(
                            value = 0,
                            animationSpec = tween(
                                durationMillis = 500,
                                delayMillis = 200,
                                easing = EaseIn
                            )
                        )
                    }
                }
            },
            modifier = Modifier
        )
    }
}

@Composable
private fun ContentDetailScreenContent(
    day: Int, navigateBackwards: () -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dailyContent = Data.getDailyContent(context)[day]
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxSize(),
        ) {
            Box {
                ContentImage(
                    context = context,
                    imagePath = dailyContent.imagePath,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                )
                Day(
                    day = day,
                    modifier = Modifier
                        .fillMaxSize()
                        .absoluteOffset(
                            y = dimensionResource(id = R.dimen.size_of_day_circle) / 2,
                            x = (-12).dp
                        )
                )
                BackStackNavigation(
                    navigateBackwards = navigateBackwards,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 12.dp)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .height((dimensionResource(id = R.dimen.size_of_day_circle) / 2) + 8.dp)
                .background(MaterialTheme.colorScheme.background)
        )

        Column(
            modifier = Modifier
                .weight(0.7f)
                .verticalScroll(scrollState)
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .padding(top = 8.dp)
                .safeContentPadding()
        ) {
            val color = MaterialTheme.colorScheme.tertiary
            Quote(
                quote = dailyContent.quote,
                modifier = Modifier
                    .clip(RoundedCornerShape(12))
                    .padding(4.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .drawBehind {
                        val strokeWidth = 14.dp.toPx()
                        drawLine(
                            color = color,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = strokeWidth
                        )
                    }
                    .padding(16.dp)
            )
            Mindfulness(
                mindfulnessMessage = dailyContent.mindfulnessTip,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 24.dp)
                    .clip(RoundedCornerShape(12))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(16.dp)
            )
            GratitudePrompt(
                gratitudePrompt = dailyContent.gratitudePrompt,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 28.dp)
                    .clip(RoundedCornerShape(12))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(16.dp)
            )
            SelfCompassionReminder(
                selfCompassionReminder = dailyContent.selfCompassionReminder,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 28.dp)
                    .clip(RoundedCornerShape(12))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(16.dp)
            )
            MindfulEatingTip(
                mindfulEatingTip = dailyContent.mindfulEatingTip,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 28.dp)
                    .clip(RoundedCornerShape(12))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp)
            )
            Affirmation(
                affirmation = dailyContent.affirmation,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 28.dp)
                    .clip(RoundedCornerShape(12))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(16.dp)
            )
            ReflectionPrompt(
                reflectionPrompt = dailyContent.reflectionPrompt,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 28.dp)
                    .clip(RoundedCornerShape(12))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(16.dp)
            )
            NatureConnection(
                natureConnection = dailyContent.natureConnection,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 28.dp)
                    .clip(RoundedCornerShape(12))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun BackStackNavigation(navigateBackwards: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        IconButton(
            onClick = navigateBackwards,
            modifier = Modifier
                .size(26.dp)
                .shadow(elevation = 60.dp, shape = RoundedCornerShape(40))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Navigate backwards",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(26.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
                    .shadow(elevation = 40.dp, shape = RoundedCornerShape(40))
            )
        }
    }
}

@Composable
private fun ContentImage(
    context: Context,
    imagePath: String,
    modifier: Modifier = Modifier
) {
    val image = Data.getImage(context, imagePath)
    Image(
        bitmap = image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
private fun Day(day: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        Surface(
            shape = CircleShape,
            tonalElevation = 6.dp,
            shadowElevation = 10.dp,
            color = MaterialTheme.colorScheme.surfaceBright,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_of_day_circle))
        ) {
            val dayValue = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Avenir
                    )
                ) {
                    append("Day\n")
                }
                withStyle(
                    SpanStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins
                    )
                ) {
                    append(day.plus(1).toString())
                }
            }
            Text(
                text = dayValue,
                fontSize = 36.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp,
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
private fun Quote(quote: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column {
            Text(
                text = "Quote",
                style = TextStyle(
                    fontFamily = Avenir,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )
            val dailyQuote = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.W500,
                    )
                ) {
                    append("\u201C")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 18.sp,
                    )
                ) {
                    append(quote)
                }
            }
            Text(
                text = dailyQuote,
                style = TextStyle(
                    fontFamily = Poppins,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 30.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun Mindfulness(mindfulnessMessage: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column {
            Title(title = stringResource(id = R.string.mindfulness_tip))
            Subtext(
                subtext = mindfulnessMessage,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun GratitudePrompt(gratitudePrompt: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column {
            Title(title = stringResource(id = R.string.gratitude_prompt))
            Subtext(
                subtext = gratitudePrompt,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun SelfCompassionReminder(selfCompassionReminder: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column {
            Title(title = stringResource(id = R.string.self_compassion_reminder))
            Subtext(
                subtext = selfCompassionReminder,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun MindfulEatingTip(mindfulEatingTip: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column {
            Title(title = stringResource(id = R.string.mindful_eating_tip))
            Subtext(
                subtext = mindfulEatingTip,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun Affirmation(affirmation: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column {
            Title(title = stringResource(id = R.string.affirmation))
            Subtext(
                subtext = affirmation,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun ReflectionPrompt(reflectionPrompt: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column {
            Title(title = stringResource(id = R.string.reflection_prompt))
            Subtext(
                subtext = reflectionPrompt,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun NatureConnection(natureConnection: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column {
            Title(title = stringResource(id = R.string.nature_connection))
            Subtext(
                subtext = natureConnection,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun Title(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = TextStyle(
            fontFamily = Avenir,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.ExtraBold,
            ),
        modifier = modifier
    )
}

@Composable
private fun Subtext(subtext: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text = subtext,
        style = TextStyle(
            fontFamily = Poppins,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = color
        ),
        modifier = modifier
            .padding(top = 12.dp)
            .alpha(0.85f)
    )
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_7
)
fun ContentDetailScreenPreview() {
    InnerInsightTheme(darkTheme = true) {
        ContentDetailScreen(day = 0) {}
    }
}
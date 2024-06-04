package dev.android.innerinsight.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.android.innerinsight.R
import dev.android.innerinsight.components.Dot
import dev.android.innerinsight.components.LogoImage
import dev.android.innerinsight.components.LogoText
import dev.android.innerinsight.components.ScrollToTop
import dev.android.innerinsight.data.DailyItem
import dev.android.innerinsight.data.Data
import dev.android.innerinsight.router.Screen
import dev.android.innerinsight.ui.theme.Avenir
import dev.android.innerinsight.ui.theme.InnerInsightTheme
import dev.android.innerinsight.ui.theme.Poppins
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dailyContent = Data.getDailyContent(context)
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalContext.current as? Activity
    val listState = rememberLazyListState()
    val visible by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }


    BackHandler {
        showDialog = true
    }
    AnimatedVisibility(showDialog) {
        CloseAlertDialog(
            onDismiss = { showDialog = false },
            onConfirmButtonClicked = {
                coroutineScope.launch { activity?.finish() }
            }
        )
    }

    HomeScreenContent(
        context = context,
        dailyContent = dailyContent,
        navController = navController,
        lazyListState = listState
    )

    AnimatedVisibility(visible) {
        ScrollToTop(
            onClick = {
                with(coroutineScope) {
                    launch {
                        listState.animateScrollToItem(index = 0)
                    }
                }
            },
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    context: Context,
    dailyContent: List<DailyItem>,
    navController: NavHostController,
    lazyListState: LazyListState
) {
    // to monitor items scroll
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var isCardClicked by remember { mutableStateOf(false) }
    // card background color change on clicked
    val color by animateColorAsState(
        targetValue =
        if (!isCardClicked) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.tertiaryContainer,
        label = "card background",
        // animationSpec = tween(durationMillis = 350, easing = LinearEasing)
    )
    val indication = rememberRipple(
        bounded = true,
        color = MaterialTheme.colorScheme.tertiaryContainer
    )
    val interactionSource = remember { MutableInteractionSource() }



    HomeScreenContentArrangement(
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentPadding = paddingValues,
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(dailyContent, key = { it.day }) { dailyItem ->
                    ContentItemCard(
                        dailyItems = dailyItem,
                        context = context,
                        color = color,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .size(500.dp, 266.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = indication,
                                onClickLabel = "Show detail",
                                role = Role.Button,
                                onClick = {
                                    isCardClicked = true
                                    navController.navigate(
                                        "${
                                            Screen.ContentDetailScreen.route
                                        }/${
                                            dailyItem.day - 1
                                        }"
                                    )

                                    isCardClicked = false
                                }
                            )
                            .focusable()
                            .onFocusEvent { focusState ->
                                isCardClicked = focusState.isFocused
                            }
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContentArrangement(
    content: @Composable (PaddingValues) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            HomeScreenTopAppBar(
                scrollBehavior,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 12.dp, bottomStart = 12.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(bottom = 8.dp)
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LogoImage(
                    modifier = Modifier
                        .size(28.dp)
                )
                Spacer(modifier = Modifier.padding(end = 2.dp))
                Dot(modifier = Modifier.size(6.dp))
                Spacer(modifier = Modifier.padding(end = 2.dp))
                LogoText(fontSize = 20.sp, letterSpacing = 0.6.sp)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        )
    )
}

@Composable
private fun ContentItemCard(
    dailyItems: DailyItem,
    context: Context,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(
            containerColor = color,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 16.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
        ) {
            // card text
            ContentItemCardText(
                dailyItems = dailyItems,
                modifier = Modifier
                    .weight(0.5f)
            )
            Spacer(modifier = Modifier.weight(0.1f))
            // card image
            ContentItemCardImage(
                context = context,
                dailyItems = dailyItems,
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun ContentItemCardText(
    dailyItems: DailyItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Day " + dailyItems.day,
            style = TextStyle(
                fontWeight = FontWeight.W600,
                fontFamily = Avenir,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.onSecondary,
                    blurRadius = 0.5f
                )
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .background(
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(50)
                )
                .shadow(elevation = 0.5.dp, shape = RoundedCornerShape(50))
                .padding(horizontal = 14.dp, vertical = 10.dp)
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
                append(dailyItems.quote)
            }
        }
        Text(
            text = dailyQuote,
            style = TextStyle(
                fontFamily = Poppins,
                lineHeight = 24.sp,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            overflow = TextOverflow.Ellipsis
        )
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun ContentItemCardImage(
    dailyItems: DailyItem,
    context: Context,
    modifier: Modifier = Modifier
) {
    val image = Data.getImage(context, dailyItems.imagePath)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Image(
            bitmap = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .fillMaxSize()
        )
    }
}

@Composable
private fun CloseAlertDialog(
    onDismiss: () -> Unit,
    onConfirmButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirmButtonClicked) {
                AlertDialogTextButton(text = stringResource(id = R.string.text_button_confirm_text))
            }
        },
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(painter = painterResource(id = R.drawable.help), contentDescription = "")
                Text(
                    text = stringResource(id = R.string.exit_app_title),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W800,
                        fontFamily = Avenir
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        },
        text = {
            Text(
                text = stringResource(id = R.string.exit_app_text),
                style = TextStyle(
                    fontFamily = Poppins,
                    fontSize = 16.sp
                ),
                modifier = Modifier.alpha(0.9f)
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                AlertDialogTextButton(text = stringResource(id = R.string.text_button_dismiss_text))
            }
        },
        tonalElevation = 10.dp,
    )
}

@Composable
private fun AlertDialogTextButton(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
fun HomeScreenPreview() {
    InnerInsightTheme(darkTheme = false) {
        val navController = rememberNavController()
        HomeScreen(navController)
    }
}

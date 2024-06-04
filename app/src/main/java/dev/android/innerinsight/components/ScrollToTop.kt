package dev.android.innerinsight.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.android.innerinsight.ui.theme.InnerInsightTheme

@Composable
fun ScrollToTop(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomEnd)
            .absoluteOffset(x = (-20).dp, y = (-60).dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            onClick = onClick,
            tonalElevation = 20.dp,
            shadowElevation = 2.dp,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceBright,
            modifier = Modifier
                .size(50.dp)
        ) {}
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .rotate(90f)
                .scale(1.4f)
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_7,
    name = "scroll to top button"
)
fun ScrollToTopPreview() {
    InnerInsightTheme(darkTheme = false) {
        ScrollToTop(
            onClick = {},
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.BottomEnd)
                .absoluteOffset(x = (-20).dp, y = (-20).dp)
            // .hoverable()
        )
    }
}
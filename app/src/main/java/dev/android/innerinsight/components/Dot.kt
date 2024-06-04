package dev.android.innerinsight.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import dev.android.innerinsight.R

@Composable
fun Dot(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.dot),
        contentDescription = null,
        modifier = modifier,
        tint = MaterialTheme.colorScheme.primary
    )
}
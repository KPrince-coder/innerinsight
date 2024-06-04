package dev.android.innerinsight.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.android.innerinsight.R
import dev.android.innerinsight.ui.theme.Poppins

@Composable
fun LogoText(fontSize: TextUnit, letterSpacing: TextUnit) {
    Text(
        text = stringResource(id = R.string.app_name),
        color = MaterialTheme.colorScheme.primary,
        style = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = fontSize,
            letterSpacing = letterSpacing,
            lineHeight = 30.sp,
            shadow = Shadow(Color.Black, blurRadius = 0.5f)
        )
    )
}
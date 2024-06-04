package dev.android.innerinsight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import dev.android.innerinsight.router.ScreenManager
import dev.android.innerinsight.ui.theme.InnerInsightTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InnerInsightTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // CompositionLocalProvider(
                    //     LocalDailyContent provides remember {
                    //         mutableStateOf(Data.getDailyContent(this))
                    //     }
                    // ) {
                    //     ScreenManager()
                    // }
                    ScreenManager()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_7)
fun Preview() {
    InnerInsightTheme (darkTheme = true){
        ScreenManager()
    }
}
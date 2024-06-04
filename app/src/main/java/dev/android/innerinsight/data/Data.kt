package dev.android.innerinsight.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException

@Immutable
@Serializable
data class DailyContent(
    @SerialName("daily_content") val dailyItems: List<DailyItem>
)

@Serializable
data class DailyItem(
    val day: Int,
    val quote: String,
    @SerialName("mindfulness_tip") val mindfulnessTip: String,
    @SerialName("gratitude_prompt") val gratitudePrompt: String,
    @SerialName("self_compassion_reminder") val selfCompassionReminder: String,
    @SerialName("mindful_eating_tip") val mindfulEatingTip: String,
    val affirmation: String,
    @SerialName("reflection_prompt") val reflectionPrompt: String,
    @SerialName("nature_connection") val natureConnection: String,
    @SerialName("image_path") val imagePath: String
)

object Data {
    private fun readJsonContent(context: Context): DailyContent? {
        return try {
            val file =
                context.assets.open("app_data.json")
                    .bufferedReader()
                    .use { it.readText() }
            // Returns if no error is detected
            Json.decodeFromString<DailyContent>(file)
        } catch (e: IOException) {
            // Handle file not found or other IO exceptions
            Log.e("File Error", "Error reading file: ${e.message}")
            null
        } catch (e: SerializationException) {
            // Handle JSON parsing errors
            Log.e("Deserialization Error", "Error decoding JSON: ${e.message}")
            null
        }
    }

    fun getDailyContent(context: Context): List<DailyItem> {
        return readJsonContent(context)?.dailyItems ?: emptyList()
    }

    fun getImage(context: Context, imagePath: String): ImageBitmap {
        val bitmapImage = BitmapFactory.decodeStream(
            context.assets.open(imagePath)
        )
        // returning a scaled down version of image to prevent memory out of bounds exception
        return Bitmap.createScaledBitmap(
            bitmapImage,
            bitmapImage.width / 10,
            bitmapImage.height / 10,
            true
        ).asImageBitmap()
    }
}

// Todo: check whether the code below is workable
// val LocalDailyContent = compositionLocalOf {
//     mutableStateOf<List<DailyItem>>(emptyList())
// }
@Composable
@Preview(
    showBackground = true,
    device = Devices.PIXEL_7,
    showSystemUi = true
)
fun PreviewData() {
    val context = LocalContext.current
    val data = Data.getDailyContent(context)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = "Day " + data[0].day.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(text = data[0].quote, fontSize = 20.sp)

            // val g = BitmapFactory.Options().run{
            //     this.inSampleSize
            // }

            val image = Data.getImage(context,data[16].imagePath)
            // val l = Bitmap.createScaledBitmap(
            //     image, image.width/5 , image.height/5,true)
            // Image(bitmap = image.asImageBitmap(), contentDescription = null)
            Image(bitmap = image, contentDescription = null)

        //     Text(image.allocationByteCount.toString(), fontSize = 24.sp)
        //     Text(l.allocationByteCount.toString(), fontSize = 24.sp)
        }
    }
}
package ai.travel.app.home.ui

import ai.travel.app.R
import ai.travel.app.home.ApiState
import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.base64ToByteArray
import ai.travel.app.navigation.Screens
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import ai.travel.app.utils.ProfileImage
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream

@Composable
fun ExpandedTopBarHomeScreen(
    imageUrl: String,

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
//            .height(EXPANDED_TOP_BAR_HEIGHT)
            .background(appGradient)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage(
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .size(100.dp)
                        .border(
                            brush = appGradient, shape = CircleShape, width = 1.dp
                        )
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Welcome to ",
                    color = textColor,
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "AI Travel Manager",
                    color = textColor,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                )
            }

        }
        Divider(thickness = 1.dp, color = textColor.copy(0.5f), modifier = Modifier.padding(vertical =12.dp))

    }
}
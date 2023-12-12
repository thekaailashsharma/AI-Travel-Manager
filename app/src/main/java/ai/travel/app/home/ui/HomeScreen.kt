package ai.travel.app.home.ui

import ai.travel.app.home.ApiState
import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.base64ToByteArray
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.lightText
import ai.travel.app.utils.ProfileImage
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val state = viewModel.imageState.collectAsState()
        val data = viewModel.data.collectAsState()
        val geoData = viewModel.geoCodesData.collectAsState()
        val trips = viewModel.allTrips.collectAsState(initial = emptyList())
        var location by remember { mutableStateOf("Mumbai") }
        var noOfDays by remember { mutableStateOf("3 days") }
        var budget by remember { mutableStateOf("1000Rs") }

        when (state.value) {
            is ApiState.Error -> {
                Text(text = "Error", color = lightText)
            }

            is ApiState.Loaded -> {

                LazyColumn {

                    item {
                        Text(text = "Loading GeoCodes", color = lightText)
                    }

                    items(data.value.size) { index ->
                        Text(
                            text = data.value[index].toString(),
                            color = lightText,
                            fontSize = 12.sp
                        )
                    }
                    item {
                        Text(
                            text = (state.value as ApiState.Loaded).data.candidates?.get(0)?.output
                                ?: ""
                        )
                    }
                }
            }

            ApiState.Loading -> {
                AnimatedVisibility(visible = state.value !is ApiState.Loaded) {
                    CircularProgressIndicator(
                        color = lightText,
                    )
                }
            }

            ApiState.NotStarted -> {
                AnimatedVisibility(visible = state.value !is ApiState.Loaded) {
                    Text(text = "Home Screen", color = lightText)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {
                        viewModel.updateMessage(
                            "Generate a guided tour plan for a trip to " +
                                    "[LOCATION] for [NUMBER_OF_DAYS] days, considering various factors " +
                                    "such as [BUDGET_RANGE], preferred activities, accommodations, " +
                                    "transportation, and any specific preferences.1. Destination: " +
                                    "[$location] 2. Duration: [$noOfDays] days 3. Budget: [$budget] 4. " +
                                    "Activities: [TEMPLES, Sea] 5. Accommodations: [AC] 6. " +
                                    "Transportation: [Bus, Car] 7. Special Preferences: [None]." +
                                    "Provide a comprehensive guided tour plan that includes " +
                                    "recommended activities, places to visit, estimated costs, " +
                                    "suitable accommodations, transportation options, and any other " +
                                    "relevant information. Please consider the specified factors to " +
                                    "tailor the plan accordingly. GIVE OUTPUT IN THE FORMAT I ASKED ONLY. " +
                                    "DO NOT CHANGE THE output FORMAT. DO NOT. DO NOT change the FORMAT. " +
                                    "Format is Day1 Morning: 1. Location (Latitude, Longitude) 2. Name:" +
                                    " Name of Location 3. Budget, 4. Some additional notes. " +
                                    "Similarly for afternoon & evening. Generate same output for all days" ,
                            location = location,
                            noOfDays = noOfDays
                        )
                        viewModel.getApiData()
                    }) {
                        Text(text = "Fetch Data")
                    }
                }
            }

            ApiState.ReceivedGeoCodes -> {
                LazyColumn {

                    items(geoData.value) { newData ->
                        Text(
                            text = "Place: ${newData.name}, Value: (${newData.geoCode?.latitude}, ${newData.geoCode?.longitude})",
                            color = lightText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            ApiState.ReceivedPhotoId -> {
                Text(text = "Received Photo Id", color = lightText)
            }

            ApiState.ReceivedPlaceId -> {
                Text(text = "Received Place Id", color = lightText)
            }

            ApiState.ReceivedPhoto -> {
                LazyColumn {

                    items(trips.value) { newData ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Place: ${newData?.name}",
                                color = lightText,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            newData?.photoBase64?.let {
                                convertImageByteArrayToBitmap(base64ToByteArray(it))?.asImageBitmap()
                                    ?.let { it1 ->
                                        Image(
                                            bitmap = it1,
                                            contentDescription = "some useful description",
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape),
                                        )
                                    }
                            }



                        }
                    }
                }

            }

            else -> {}
        }


    }

}

@Composable
fun PfScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {
        Text(text = "Profile Screen", color = lightText)

    }

}

@Composable
fun RtScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {
        Text(text = "Routes Screen", color = lightText)

    }

}

fun convertImageByteArrayToBitmap(imageData: ByteArray?): Bitmap? {
    return imageData?.size?.let { BitmapFactory.decodeByteArray(imageData, 0, it) }
}
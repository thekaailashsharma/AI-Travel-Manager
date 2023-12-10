package ai.travel.app.mapsSearch.ui

import ai.travel.app.home.ApiState
import ai.travel.app.home.base64ToByteArray
import ai.travel.app.home.ui.convertImageByteArrayToBitmap
import ai.travel.app.mapsSearch.MapsSearchViewModel
import ai.travel.app.newTrip.NewTripScreen
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import android.app.appsearch.SearchResults
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mapbox_map.PreviewMap
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsSearchScreen(viewModel: MapsSearchViewModel,navController: NavController) {
    val query = viewModel.query.collectAsState()
    val address = viewModel.addresses.collectAsState()
    val imageState = viewModel.imageState.collectAsState()
    val searchResponse = viewModel.searchResponse.collectAsState()
    val photoId = viewModel.photoId.collectAsState()
    val modalSheetStates = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = false
        )
    )
    val coroutineScope = rememberCoroutineScope()
    println("Search Responsessss: ${searchResponse.value}")
    println("Image State: ${imageState.value}")
    Box(modifier = Modifier.fillMaxSize()) {
        PreviewMap(
            modifier = Modifier.fillMaxSize(),
            latitude = viewModel.latitude,
            longitude = viewModel.longitude,
            isClicked = viewModel.isClicked,
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
            Column {
                MapsSearchBar(
                    mutableText = query.value,
                    onValueChange = {
                        viewModel.setQuery(it)
                    },
                    viewModel = viewModel,
                    onTrailingClick = {
                        viewModel.setQuery(TextFieldValue(""))
                    },
                    navController = navController
                )
                Spacer(modifier = Modifier.height(10.dp))
                AnimatedVisibility(
                    visible = imageState.value !is ApiState.NotStarted && imageState.value !is ApiState.ReceivedPhoto,
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        color = lightText
                    )
                }

            }
        }

        AnimatedVisibility(
            visible = imageState.value is ApiState.ReceivedPhoto,
            enter = slideInVertically(initialOffsetY = {
                it
            }),
            exit = slideOutVertically(targetOffsetY = {
                it
            })
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                SearchResults(viewModel = viewModel)
            }
        }
    }
}


@Composable
fun SearchResults(viewModel: MapsSearchViewModel) {
    val searchResponse = viewModel.searchResponse.collectAsState()
    searchResponse.value?.let { response ->
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.34f),
//            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
//            elevation = CardDefaults.cardElevation(7.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = Color.Transparent.copy(0.8f),
//            ),
//            border = BorderStroke(
//                width = 0.5.dp,
//                color = textColor
//            )
//        ) {
//            Column() {
                LazyRow(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.34f),
                    contentPadding = PaddingValues(30.dp)
                ) {
                    items(response.photos) { photo ->
                        convertImageByteArrayToBitmap(photo)?.asImageBitmap()
                            ?.let { it1 ->
                                Image(
                                    bitmap = it1,
                                    contentDescription = "some useful description",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .padding(horizontal = 10.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                    }
                }
            }
//        }
//    }
}



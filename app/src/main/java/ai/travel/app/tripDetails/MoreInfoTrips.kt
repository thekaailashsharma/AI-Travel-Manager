package ai.travel.app.tripDetails

import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.base64ToByteArray
import ai.travel.app.home.ui.convertImageByteArrayToBitmap
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import ai.travel.app.utils.dashedBorder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreInfoTrips(viewModel: HomeViewModel, paddingValues: PaddingValues) {
    val cardData1 = listOf(
        GridCardData(
            topText = "Day",
            bottomText = viewModel.currentDay.value,
            icon = Icons.Filled.Public
        ),
        GridCardData(
            topText = "Time",
            bottomText = viewModel.currentTimeOfDay.value,
            icon = Icons.Filled.Public
        ),
    )
    val coroutineScope = rememberCoroutineScope()
    val modalSheetStates = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = false
        )
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.88f)
            .background(appGradient)
            .padding(paddingValues)
    ) {

        println("MoreInfoTripssss: trips.value = ${viewModel.currentNewDestination.value}")

        var dayTrips =
            viewModel.getMoreInfo(
                destination = viewModel.currentNewDestination.value
            ).collectAsState(initial = emptyList())



        if (dayTrips.value.isEmpty()) {
            CircularProgressIndicator()
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                dayTrips.value[0]?.photoBase64?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.34f),
                        shape = RoundedCornerShape(0.dp),
                        elevation = CardDefaults.cardElevation(7.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        convertImageByteArrayToBitmap(base64ToByteArray(it))?.asImageBitmap()
                            ?.let { it1 ->
                                Image(
                                    bitmap = it1,
                                    contentDescription = "some useful description",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .drawWithCache {
                                            val gradient = Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(0.8f)
                                                ),
                                                startY = size.height / 5.5f,
                                                endY = size.height
                                            )
                                            onDrawWithContent {
                                                drawContent()
                                                drawRect(
                                                    gradient,
                                                    blendMode = BlendMode.Multiply
                                                )
                                            }
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }
                    }     //IMAGE

                    Text(text = "Mumbai")
                 /////////////
                    

                }

            }
        }
    }
}

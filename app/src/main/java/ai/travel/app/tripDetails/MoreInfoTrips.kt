package ai.travel.app.tripDetails

import ai.travel.app.R
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Bookmark
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex


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
            .verticalScroll(rememberScrollState())
            .fillMaxHeight(0.95f)
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
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    ) {

                        Row (modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically){

                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp)
                            )

                        }
                        Spacer(modifier = Modifier.height(95.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 40.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = viewModel.currentDestination.value,
                                color = textColor,
                                fontSize = 45.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(
                                    start = 20.dp,
                                    top = 20.dp,
                                    bottom = 8.dp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            LazyRow {
                                items(6) { item ->


                                    Card(modifier = Modifier.clip(RoundedCornerShape(15.dp))) {
                                        Text(
                                            text = "Hotels",
                                            modifier = Modifier.padding(all = 10.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))


                                }
                            }
                        }



                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .zIndex(5f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            Card(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(135.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {

                                    Image(
                                        painter = painterResource(id = R.drawable.dummycard),
                                        contentDescription = "",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth()
                                                .align(Alignment.BottomCenter)
                                        ) {
                                            Card(modifier = Modifier.clip(RoundedCornerShape(15.dp))) {

                                                Column (modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)){
                                                    Text(text = "Hotels", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                                    Row(
                                                        modifier = Modifier,
                                                        horizontalArrangement = Arrangement.Center,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(text = "2.9")
                                                        Row(
                                                            modifier = Modifier,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Star,
                                                                contentDescription = null,
                                                                modifier = Modifier.padding(1.dp).size(18.dp)
                                                            )
                                                            Icon(
                                                                imageVector = Icons.Default.Star,
                                                                contentDescription = null,
                                                                modifier = Modifier.padding(1.dp).size(18.dp)
                                                            )
                                                            Icon(
                                                                imageVector = Icons.Default.Star,
                                                                contentDescription = null,
                                                                modifier = Modifier.padding(1.dp).size(18.dp)
                                                            )
                                                            Icon(
                                                                imageVector = Icons.Default.Star,
                                                                contentDescription = null,
                                                                modifier = Modifier.padding(1.dp).size(18.dp)
                                                            )
                                                            Icon(
                                                                imageVector = Icons.Default.Star,
                                                                contentDescription = null,
                                                                modifier = Modifier.padding(1.dp).size(18.dp)
                                                            )

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }



                            }

                        }



                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .padding(end = 15.dp, top = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "More Info",fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Icon(
                                imageVector = Icons.Outlined.Bookmark,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)) {
                            Text(
                                text = "Lorem Ipsum is simply dummy text of the printing " +
                                        "and typesetting industry." +
                                        " Lorem Ipsum has been the industry's"
                            )
                        }

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically) {

                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.padding(end= 8.dp).size(25.dp)
                            )
                            Text(
                                text = "Mumbai, Maharashtra 400070",
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Text(text = "Ratings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "2.9")
                                Spacer(modifier = Modifier.width(10.dp))
                                Row(
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(40.dp))


                    }

                }

            }
        }
    }
}


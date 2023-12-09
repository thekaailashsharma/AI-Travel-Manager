package ai.travel.app.tripDetails

import ai.travel.app.R
import ai.travel.app.database.travel.TripsEntity
import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.base64ToByteArray
import ai.travel.app.home.ui.CollapsedTopBarDetailsScreen
import ai.travel.app.home.ui.CollapsedTopBarHomeScreen
import ai.travel.app.home.ui.convertImageByteArrayToBitmap
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.bottomBarBackground
import ai.travel.app.ui.theme.bottomBarBorder
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import android.graphics.Point
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mapbox_map.MapBoxMap
import com.example.mapbox_map.MapBoxPoint
import com.example.mapbox_map.MapItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TripDetailsScreen(
    viewModel: HomeViewModel,
    paddingValues: PaddingValues,
    navController: NavController,
) {
    val cardData1 = listOf(
        GridCardData(
            topText = "Country",
            bottomText = "India",
            icon = Icons.Filled.Public
        ),
        GridCardData(
            topText = "City",
            bottomText = viewModel.currentDestination.value,
            icon = Icons.Filled.Public
        ),
    )
    val cardData2 = listOf(
        GridCardData(
            topText = "Ratings",
            bottomText = (1..10).random().toString(),
            icon = Icons.Filled.Wallet
        ),
        GridCardData(
            topText = "Days",
            bottomText = "3",
            icon = Icons.Filled.CalendarToday
        ),
    )
    val coroutineScope = rememberCoroutineScope()
    val modalSheetStates = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = false
        )
    )
    var currentPoint = remember { mutableStateOf<MapBoxPoint?>(null) }
    val collapseThreshold = remember {
        mutableFloatStateOf(0.25f)
    }
    val listState = rememberLazyListState()
    val isCollapsed = remember(listState) {
        derivedStateOf {
            val firstVisibleItemOffset = listState.firstVisibleItemScrollOffset
            val firstItemHeight = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
            val totalOffset =
                (firstVisibleItemOffset.toFloat() / firstItemHeight.toFloat())

            totalOffset > collapseThreshold.floatValue
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            MoreInfoTrips(viewModel = viewModel, paddingValues = paddingValues)
        },
        sheetContainerColor = CardBackground,
        scaffoldState = modalSheetStates,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetPeekHeight = 0.dp,
    ) {
        Scaffold(
            topBar = {
                CollapsedTopBarDetailsScreen(
                    isCollapsed = isCollapsed.value,
                    text = viewModel.currentDestination.value,
                    navController = navController
                )
            },
        ) { padding ->
            println(padding)
            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(appGradient)
                        .padding(paddingValues)
                ) {

                    val trips = viewModel.getCurrentTrip(viewModel.currentDestination.value)
                        .collectAsState(initial = emptyList())
                    val days = viewModel.uniqueDays(viewModel.currentDestination.value)
                        .collectAsState(initial = emptyList())
                    val currentDay = remember { mutableStateOf("1") }
                    var point: Point? by remember {
                        mutableStateOf(null)
                    }
                    var dayTrips =
                        viewModel.getTrips(currentDay.value, viewModel.currentDestination.value)
                            .collectAsState(initial = emptyList())

                    val newItems = remember {
                        mutableStateListOf<TripsEntity?>()
                    }
                    LaunchedEffect(key1 = viewModel.getCurrentTrip(viewModel.currentDestination.value)) {
                        viewModel.getCurrentTrip(viewModel.currentDestination.value).collectLatest {
                            if (it.isNotEmpty() && viewModel.currentDestination.value != "") {
                                newItems.clear()
                                newItems.addAll(
                                    extractTripsByDestination(
                                        it,
                                        viewModel.currentDestination.value
                                    )
                                )
                            }
                        }
                    }

                    if (trips.value.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = lightText)
                        }
                    } else {
                        trips.value[0]?.photoBase64?.let {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                                state = listState
                            ) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.TopStart
                                    ) {
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
                                                                val gradient =
                                                                    Brush.verticalGradient(
                                                                        colors = listOf(
                                                                            Color.Transparent,
                                                                            Color.Black.copy(
                                                                                0.8f
                                                                            )
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
                                                        contentScale = ContentScale.FillBounds
                                                    )
                                                }
                                        }

                                        Column(modifier = Modifier.fillMaxSize()) {
                                            Icon(
                                                imageVector = Icons.Filled.Close,
                                                contentDescription = "Arrow Back",
                                                tint = lightText,
                                                modifier = Modifier
                                                    .padding(start = 15.dp, top = 10.dp)
                                                    .size(25.dp)
                                                    .clickable {
                                                        navController.popBackStack()
                                                    }
                                            )
                                            Spacer(modifier = Modifier.height(70.dp))
                                            Text(
                                                text = viewModel.currentDestination.value,
                                                color = textColor,
                                                fontSize = 35.sp,
                                                modifier = Modifier.padding(
                                                    start = 20.dp,
                                                    top = 20.dp,
                                                    bottom = 8.dp
                                                )
                                            )


                                            LazyVerticalGrid(
                                                columns = GridCells.Fixed(2),
                                                modifier = Modifier.height(200.dp)
                                            ) {
                                                items(cardData1) {
                                                    GridCard(
                                                        topText = it.topText,
                                                        bottomText = it.bottomText,
                                                        icon = it.icon
                                                    )
                                                }

                                                items(cardData2) {
                                                    GridCard(
                                                        topText = it.topText,
                                                        bottomText = it.bottomText,
                                                        icon = it.icon
                                                    )
                                                }

                                            }

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .padding(
                                                        start = 12.dp,
                                                        top = 0.dp,
                                                        bottom = 0.dp,
                                                        end = 12.dp
                                                    )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Map,
                                                    contentDescription = "topText",
                                                    tint = lightText,
                                                    modifier = Modifier.size(30.dp)
                                                )
                                                Spacer(modifier = Modifier.width(7.dp))
                                                Text(
                                                    text = "Map View",
                                                    color = textColor,
                                                    fontSize = 25.sp,
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(20.dp))

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(start = 12.dp, end = 12.dp)
                                                    .height(200.dp)
                                                    .shadow(10.dp),
                                                shape = RoundedCornerShape(10.dp),
                                                elevation = CardDefaults.cardElevation(40.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color.Transparent
                                                ),
                                            ) {
                                                MapBoxMap(
                                                    onPointChange = { point ->

                                                    },
                                                    points = dayTrips.value.map {
                                                        MapItem(
                                                            image = R.drawable.app_icon,
                                                            latitude = it?.latitude ?: 0.0,
                                                            longitude = it?.longitude ?: 0.0,
                                                            location = it?.destination ?: "",
                                                            time = it?.timeOfDay ?: "",
                                                        )
                                                    },
                                                    currentPoint = currentPoint.apply {
                                                        value = MapBoxPoint(
                                                            latitude = dayTrips.value[0]?.latitude
                                                                ?: 0.0,
                                                            longitude = dayTrips.value[0]?.longitude
                                                                ?: 0.0,
                                                            zoom = 15.0
                                                        )
                                                    },
                                                    latitude = dayTrips.value[0]?.latitude
                                                        ?: 0.0,
                                                    longitude = dayTrips.value[0]?.longitude
                                                        ?: 0.0,
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(20.dp))

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .padding(
                                                        start = 12.dp,
                                                        top = 0.dp,
                                                        bottom = 0.dp,
                                                        end = 12.dp
                                                    )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.LocationOn,
                                                    contentDescription = "topText",
                                                    tint = lightText,
                                                    modifier = Modifier.size(30.dp)
                                                )
                                                Spacer(modifier = Modifier.width(7.dp))
                                                Text(
                                                    text = "Your Schedule",
                                                    color = textColor,
                                                    fontSize = 25.sp,
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(20.dp))
                                        }
                                    }
                                }

                                item {
                                    LazyRow {
                                        items(days.value) { day ->
                                            Card(
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (day == currentDay.value) lightText else bottomBarBackground,
                                                ),
                                                border = BorderStroke(1.dp, bottomBarBorder),
                                                shape = RoundedCornerShape(20.dp),
                                                elevation = CardDefaults.cardElevation(0.dp),
                                                modifier = Modifier
                                                    .width(120.dp)
                                                    .padding(
                                                        start = 12.dp,
                                                        top = 0.dp,
                                                        bottom = 12.dp,
                                                        end = 12.dp
                                                    )
                                                    .clickable(
                                                        interactionSource = MutableInteractionSource(),
                                                        indication = null
                                                    ) {
                                                        coroutineScope.launch {
                                                            currentDay.value = day ?: "1"
                                                        }
                                                    }

                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            start = 0.dp,
                                                            top = 10.dp,
                                                            bottom = 10.dp
                                                        )
                                                ) {
                                                    Text(
                                                        text = "Day $day",
                                                        color = textColor,
                                                        fontSize = 18.sp,
                                                        modifier = Modifier.padding(start = 2.dp)
                                                    )
                                                }
                                            }


                                        }
                                    }
                                }

                                items(dayTrips.value) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 0.dp,
                                                top = 0.dp,
                                                bottom = 12.dp,
                                                end = 12.dp
                                            ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(0.3f),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Top
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.WbSunny,
                                                contentDescription = "topText",
                                                tint = lightText,
                                                modifier = Modifier.size(25.dp)
                                            )
                                            Spacer(modifier = Modifier.height(5.dp))
                                            VerticalDashedDivider(
                                                color = lightText,
                                                height = 100,
                                                dashWidth = 14f,
                                                gapWidth = 10f
                                            )
                                        }

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            horizontalAlignment = Alignment.Start,
                                            verticalArrangement = Arrangement.Top
                                        ) {
                                            Text(
                                                text = it?.timeOfDay ?: "",
                                                color = textColor,
                                                fontSize = 25.sp,
                                                modifier = Modifier
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = it?.name ?: "",
                                                color = textColor,
                                                fontSize = 13.sp,
                                                modifier = Modifier,
                                                softWrap = true
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Filled.Wallet,
                                                    contentDescription = "topText",
                                                    tint = lightText,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(7.dp))
                                                Text(
                                                    text = it?.budget ?: "",
                                                    color = textColor,
                                                    fontSize = 12.sp,
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Button(
                                                    onClick = {
                                                        viewModel.currentDay.value =
                                                            currentDay.value
                                                        viewModel.currentTimeOfDay.value =
                                                            it?.timeOfDay ?: ""
                                                        viewModel.currentNewDestination.value =
                                                            it?.name ?: ""
                                                        coroutineScope.launch {
                                                            modalSheetStates.bottomSheetState.expand()
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = lightText,
                                                        contentColor = textColor
                                                    )
                                                ) {
                                                    Text(
                                                        text = "More Info",
                                                        color = textColor,
                                                        fontSize = 12.sp,
                                                    )

                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Button(
                                                    onClick = { /*TODO*/ },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = lightText,
                                                        contentColor = textColor
                                                    )
                                                ) {
                                                    Text(
                                                        text = "Navigate",
                                                        color = textColor,
                                                        fontSize = 12.sp,
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
            }
        }
    }
}

fun extractTripsByDestination(items: List<TripsEntity?>, destination: String): List<TripsEntity?> {
    return items.filter { it?.destination == destination }
}


data class GridCardData(
    val topText: String,
    val bottomText: String,
    val icon: ImageVector,
)

@Composable
fun GridCard(
    topText: String,
    bottomText: String,
    icon: ImageVector,
) {
    Card(
        modifier = Modifier
            .padding(start = 12.dp, top = 0.dp, bottom = 12.dp, end = 12.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = bottomBarBackground
        ),
        border = BorderStroke(1.dp, bottomBarBorder)

    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = topText,
                    tint = lightText,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = topText,
                    color = textColor,
                    fontSize = 12.sp,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = bottomText,
                color = textColor,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 2.dp)
            )

        }
    }
}
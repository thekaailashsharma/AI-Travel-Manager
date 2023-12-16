package ai.travel.app.tripDetails

import ai.travel.app.R
import ai.travel.app.database.travel.TripsEntity
import ai.travel.app.home.CustomMarker
import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.base64ToByteArray
import ai.travel.app.home.ui.CollapsedTopBarDetailsScreen
import ai.travel.app.home.ui.CollapsedTopBarHomeScreen
import ai.travel.app.home.ui.convertImageByteArrayToBitmap
import ai.travel.app.navigation.Screens
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.borderBrush
import ai.travel.app.ui.theme.bottomBarBackground
import ai.travel.app.ui.theme.bottomBarBorder
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.monteSB
import ai.travel.app.ui.theme.textColor
import android.graphics.Point
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.mapbox_map.MapBoxMap
import com.example.mapbox_map.MapBoxPoint
import com.example.mapbox_map.MapItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


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


    val currentSlot = viewModel.currentTime
        .collectAsState(initial = TimeSlot.MORNING)

    val progress = viewModel.progress.collectAsState(initial = 0.0f)

    LaunchedEffect(key1 = currentSlot.value) {
        viewModel.calculateTimeSlotUpdates()
    }


    val collapseThreshold = remember {
        mutableFloatStateOf(0.25f)
    }
    var isReorderVisible by remember {
        mutableStateOf(false)
    }

    var isDeleteSheetOpen = remember {
        mutableStateOf(false)
    }
    var isDeleteUndo = remember {
        mutableStateOf(false)
    }
    var isDeleteClicked = remember {
        mutableStateOf(false)
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
    val totalBudget = viewModel.totalBudget(viewModel.currentDestination.value)
        .collectAsState(initial = emptyList())

    val remainingBudget = viewModel.remainingBudget.collectAsState(initial = 0.0)

    val trips = viewModel.getCurrentTrip(viewModel.currentDestination.value)
        .collectAsState(initial = emptyList())
    val days = viewModel.uniqueDays(viewModel.currentDestination.value)
        .collectAsState(initial = emptyList())
    val currentDay = remember { mutableStateOf("1") }

    val departureDate =
        viewModel.getDepartureDate(currentDay.value, viewModel.currentDestination.value)
            .collectAsState(initial = emptyList())

    val arrivalDate = viewModel.getArrivalDate(currentDay.value, viewModel.currentDestination.value)
        .collectAsState(initial = emptyList())


    var dayTrips =
        viewModel.getTrips(currentDay.value, viewModel.currentDestination.value)
            .collectAsState(initial = emptyList())

    LaunchedEffect(key1 = Unit) {
        viewModel.extractBudgetValue(viewModel.currentDestination.value)
        viewModel.calculateTimeSlotUpdates()
        viewModel.calculateProgress()
    }

    LaunchedEffect(key1 = dayTrips.value) {
        viewModel.extractBudgetValue(viewModel.currentDestination.value)
    }
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val delete = SwipeAction(
        icon = {
            val currenanim by rememberLottieComposition(
                spec = LottieCompositionSpec.Asset("delete.json")
            )
            LottieAnimation(
                composition = currenanim,
                iterations = 1,
                contentScale = ContentScale.Crop,
                speed = 0.85f,
                modifier = Modifier
                    .size(125.dp)
            )
        },
        background = Color(0xFFFF5F52),
        isUndo = isDeleteUndo.value,
        onSwipe = {
            isDeleteSheetOpen.value = true
        }
    )

    BottomSheetScaffold(
        sheetContent = {
            if (isReorderVisible) {
                ReorderLists(viewModel = viewModel, paddingValues = paddingValues)
            } else {
                MoreInfoTrips(viewModel = viewModel, paddingValues = paddingValues)
            }
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
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(appGradient)
                            .padding(paddingValues)
                    ) {
                        val newItems = remember {
                            mutableStateListOf<TripsEntity?>()
                        }
                        LaunchedEffect(key1 = viewModel.getCurrentTrip(viewModel.currentDestination.value)) {
                            viewModel.getCurrentTrip(viewModel.currentDestination.value)
                                .collectLatest {
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

                        if (trips.value.isEmpty() || days.value.isEmpty()
                            && remainingBudget.value == 0.0
                            && totalBudget.value.isEmpty() && departureDate.value.isEmpty()
                            && arrivalDate.value.isEmpty()
                        ) {
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
                                        .fillMaxSize()
                                        .then(
                                            if (isDeleteSheetOpen.value) Modifier.blur(10.dp) else Modifier
                                        ),
                                    state = listState
                                ) {
                                    item {
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
                                            Spacer(modifier = Modifier.height(10.dp))
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
                                                            contentScale = ContentScale.FillWidth
                                                        )
                                                    }
                                            }

                                            Text(
                                                text = viewModel.currentDestination.value,
                                                color = textColor,
                                                fontSize = 35.sp,
                                                modifier = Modifier.padding(
                                                    start = 20.dp,
                                                    top = 20.dp,
                                                    bottom = 8.dp
                                                ),
                                                overflow = TextOverflow.Ellipsis,
                                            )


                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = CardBackground
                                                ),
                                                shape = RoundedCornerShape(16.dp),
                                                elevation = CardDefaults.cardElevation(
                                                    10.dp
                                                )
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(
                                                            16.dp
                                                        ),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.CalendarToday,
                                                        contentDescription = "topText",
                                                        tint = lightText,
                                                        modifier = Modifier.size(25.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(7.dp))
                                                    Text(
                                                        text = departureDate.value.get(0)
                                                            ?: "",
                                                        color = textColor,
                                                        fontSize = 13.sp,
                                                    )
                                                    Spacer(modifier = Modifier.width(20.dp))
                                                    Icon(
                                                        imageVector = Icons.Filled.Navigation,
                                                        contentDescription = "topText",
                                                        tint = lightText,
                                                        modifier = Modifier
                                                            .size(25.dp)
                                                            .rotate(90f)
                                                    )
                                                    Spacer(modifier = Modifier.width(20.dp))
                                                    Icon(
                                                        imageVector = Icons.Filled.CalendarToday,
                                                        contentDescription = "topText",
                                                        tint = lightText,
                                                        modifier = Modifier.size(25.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(7.dp))
                                                    Text(
                                                        text = arrivalDate.value.get(0)
                                                            ?: "",
                                                        color = textColor,
                                                        fontSize = 13.sp,
                                                        softWrap = true,
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(20.dp))
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
                                                    ),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row {
                                                    Icon(
                                                        imageVector = Icons.Filled.LocationOn,
                                                        contentDescription = "topText",
                                                        tint = lightText,
                                                        modifier = Modifier.size(30.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(7.dp))
                                                    Text(
                                                        text = "Your Itinerary",
                                                        color = textColor,
                                                        fontSize = 25.sp,
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(end = 12.dp)
                                                        .clickable(
                                                            interactionSource = MutableInteractionSource(),
                                                            indication = null
                                                        ) {
                                                            coroutineScope.launch {
                                                                isReorderVisible = true
                                                                modalSheetStates.bottomSheetState.expand()
                                                            }
                                                        },
                                                    horizontalArrangement = Arrangement.End
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Reorder,
                                                        contentDescription = "topText",
                                                        tint = lightText,
                                                        modifier = Modifier.size(25.dp)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(20.dp))
                                        }
                                    }

                                item {
                                    Column {
                                        Spacer(modifier = Modifier.height(20.dp))

                                        LazyRow {
                                            items(days.value) { day ->
                                                Card(
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = if (day == currentDay.value) lightText else Color.Transparent,
                                                    ),
                                                    border = BorderStroke(
                                                        1.dp,
                                                        brush = borderBrush
                                                    ),
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
                                }

                                itemsIndexed(dayTrips.value) { index, it ->
                                    SwipeableActionsBox(
                                        endActions = listOf(delete),
                                        backgroundUntilSwipeThreshold = Color(0xFF4792ff),
                                        swipeThreshold = (screenWidth / 2.5).dp
                                    ) {

                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Spacer(modifier = Modifier.height(20.dp))
                                            if (index == 0) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 10.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = it?.departureTime ?: "",
                                                        color = textColor,
                                                        fontSize = 12.sp,
                                                        modifier = Modifier
                                                            .padding(start = 10.dp, end = 10.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    LinearProgressIndicator(
                                                        progress = progress.value,
                                                        color = Color(0xFF78FA7D),
                                                        modifier = Modifier
                                                            .fillMaxWidth(0.75f)
                                                            .padding(start = 10.dp, end = 10.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(
                                                        text = it?.arrivalTime ?: "",
                                                        color = textColor,
                                                        fontSize = 12.sp,
                                                        modifier = Modifier
                                                            .padding(start = 10.dp, end = 10.dp)
                                                    )

                                                }
                                            }

                                            Spacer(modifier = Modifier.height(20.dp))

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 10.dp)
                                                    .then(
                                                        if (currentSlot.value?.ordinal == index) Modifier
                                                            .shadow(10.dp) else Modifier
                                                    ),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color.Transparent,
                                                ),
                                                shape = RoundedCornerShape(16.dp),
                                                elevation = CardDefaults.cardElevation(0.dp),
                                                border = BorderStroke(
                                                    0.5.dp,
                                                    color = if (currentSlot.value?.ordinal == index)
                                                        bottomBarBorder else Color.Transparent
                                                )
                                            ) {
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
                                                        modifier = Modifier.fillMaxWidth(
                                                            0.3f
                                                        ),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        if (index != 0) {
                                                            Spacer(
                                                                modifier = Modifier.height(
                                                                    5.dp
                                                                )
                                                            )
                                                            Icon(
                                                                imageVector = Icons.Filled.Timer,
                                                                contentDescription = "topText",
                                                                tint = textColor,
                                                                modifier = Modifier
                                                                    .size(35.dp)
                                                                    .background(
                                                                        Color(0xFF007c6e),
                                                                        shape = CircleShape
                                                                    )
                                                                    .padding(5.dp)

                                                            )
                                                            Spacer(
                                                                modifier = Modifier.height(
                                                                    5.dp
                                                                )
                                                            )
                                                            VerticalDashedDivider(
                                                                color = if (currentSlot.value?.ordinal == index)
                                                                    Color(0xFF78FA7D) else lightText,
                                                                height = 40,
                                                                dashWidth = 14f,
                                                                gapWidth = 10f
                                                            )
                                                            Spacer(
                                                                modifier = Modifier.height(
                                                                    15.dp
                                                                )
                                                            )
                                                        }
                                                        CustomMarker(text = (index + 1).toString())
                                                        Spacer(modifier = Modifier.height(5.dp))
                                                        VerticalDashedDivider(
                                                            color = if (currentSlot.value?.ordinal == index)
                                                                Color(0xFF78FA7D) else lightText,
                                                            height = 100,
                                                            dashWidth = 14f,
                                                            gapWidth = 10f
                                                        )

                                                    }

                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth(0.7f)
                                                            .weight(1f),
                                                        horizontalAlignment = Alignment.Start,
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        if (index != 0) {
                                                            Spacer(
                                                                modifier = Modifier.height(
                                                                    15.dp
                                                                )
                                                            )
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Spacer(
                                                                    modifier = Modifier.width(
                                                                        7.dp
                                                                    )
                                                                )
                                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                                    Icon(
                                                                        imageVector = Icons.Filled.DirectionsCar,
                                                                        contentDescription = "topText",
                                                                        tint = Color(
                                                                            0xFF6588bf
                                                                        ),
                                                                        modifier = Modifier.size(
                                                                            25.dp
                                                                        )
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.width(
                                                                            7.dp
                                                                        )
                                                                    )
                                                                    Text(
                                                                        text = dayTrips.value[index]?.distance
                                                                            ?: "",
                                                                        color = textColor,
                                                                        fontSize = 13.sp,
                                                                    )
                                                                }
                                                                Spacer(
                                                                    modifier = Modifier.width(
                                                                        7.dp
                                                                    )
                                                                )
                                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                                    Icon(
                                                                        imageVector = Icons.Filled.AvTimer,
                                                                        contentDescription = "topText",
                                                                        tint = Color(
                                                                            0xFF6588bf
                                                                        ),
                                                                        modifier = Modifier.size(
                                                                            25.dp
                                                                        )
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.width(
                                                                            7.dp
                                                                        )
                                                                    )
                                                                    Text(
                                                                        text = dayTrips.value[index]?.duration
                                                                            ?: "",
                                                                        color = textColor,
                                                                        fontSize = 13.sp,
                                                                    )
                                                                }
                                                            }
                                                            Spacer(
                                                                modifier = Modifier.height(
                                                                    55.dp
                                                                )
                                                            )
                                                        }
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(horizontal = 10.dp),
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text(
                                                                text = if (index == 0) "Morning" else if (index == 1) "Afternoon" else "Evening",
                                                                color = textColor,
                                                                fontSize = 25.sp,
                                                                modifier = Modifier
                                                            )
                                                            if (currentSlot.value?.ordinal == index) {
                                                                Row(
                                                                    verticalAlignment = Alignment.CenterVertically,
                                                                    modifier = Modifier.padding(
                                                                        end = 15.dp
                                                                    )
                                                                ) {
                                                                    Icon(
                                                                        imageVector = Icons.Filled.DonutLarge,
                                                                        contentDescription = "topText",
                                                                        tint = Color(
                                                                            0xFF78FA7D
                                                                        ),
                                                                        modifier = Modifier.size(
                                                                            20.dp
                                                                        )
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.width(
                                                                            7.dp
                                                                        )
                                                                    )
                                                                    Text(
                                                                        text = "Explore",
                                                                        color = Color(
                                                                            0xFF78FA7D
                                                                        ),
                                                                        fontSize = 12.sp,
                                                                    )
                                                                }
                                                            }
                                                        }
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
                                                            Spacer(
                                                                modifier = Modifier.width(
                                                                    7.dp
                                                                )
                                                            )
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
                                                                        isReorderVisible =
                                                                            false
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
                                                            Spacer(
                                                                modifier = Modifier.width(
                                                                    10.dp
                                                                )
                                                            )
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

                                item {
                                    Spacer(modifier = Modifier.height(40.dp))
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 10.dp,
                                                end = 15.dp,
                                                bottom = 10.dp,
                                                top = 10.dp
                                            ),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.Transparent,
                                            contentColor = textColor
                                        ),
                                        elevation = CardDefaults.cardElevation(0.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(
                                            0.5.dp,
                                            color = bottomBarBorder.copy(0.5f)
                                        )
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    modifier = Modifier,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Language,
                                                        contentDescription = "topText",
                                                        tint = Color(0xFF6588bf),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(7.dp))
                                                    Text(
                                                        text = "Language",
                                                        color = Color(0xFF6588bf),
                                                        fontSize = 12.sp,
                                                    )
                                                }
                                                Text(
                                                    text = "English",
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                )
                                            }
                                            VerticalDashedDivider(
                                                color = lightText,
                                                height = 15,
                                                dashWidth = 14f,
                                                gapWidth = 10f,
                                                modifier = Modifier.rotate(90f)
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    modifier = Modifier,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.WbSunny,
                                                        contentDescription = "topText",
                                                        tint = Color(0xFF6588bf),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(7.dp))
                                                    Text(
                                                        text = "Weather",
                                                        color = Color(0xFF6588bf),
                                                        fontSize = 12.sp,
                                                    )
                                                }
                                                Text(
                                                    text = "Sunny",
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                )
                                            }
                                            VerticalDashedDivider(
                                                color = lightText,
                                                height = 15,
                                                dashWidth = 14f,
                                                gapWidth = 10f,
                                                modifier = Modifier.rotate(90f)
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    modifier = Modifier,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.AccountBalanceWallet,
                                                        contentDescription = "topText",
                                                        tint = Color(0xFF6588bf),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(7.dp))
                                                    Text(
                                                        text = "Total Budget",
                                                        color = Color(0xFF6588bf),
                                                        fontSize = 12.sp,
                                                    )
                                                }
                                                Text(
                                                    text = "Rs ${totalBudget.value[0]}",
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                )
                                            }
                                            VerticalDashedDivider(
                                                color = lightText,
                                                height = 15,
                                                dashWidth = 14f,
                                                gapWidth = 10f,
                                                modifier = Modifier.rotate(90f)
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    modifier = Modifier,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.MoneyOff,
                                                        contentDescription = "topText",
                                                        tint = Color(0xFF6588bf),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(7.dp))
                                                    Text(
                                                        text = "Used Budget",
                                                        color = Color(0xFF6588bf),
                                                        fontSize = 12.sp,
                                                    )
                                                }
                                                Text(
                                                    text = "Rs ${remainingBudget.value}",
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                )
                                            }
                                            VerticalDashedDivider(
                                                color = lightText,
                                                height = 15,
                                                dashWidth = 14f,
                                                gapWidth = 10f,
                                                modifier = Modifier.rotate(90f)
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    modifier = Modifier,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Money,
                                                        contentDescription = "topText",
                                                        tint = Color(0xFF6588bf),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(7.dp))
                                                    Text(
                                                        text = "Available Budget",
                                                        color = Color(0xFF6588bf),
                                                        fontSize = 12.sp,
                                                    )
                                                }
                                                Text(
                                                    text = "Rs ${
                                                        (totalBudget.value[0]?.minus(
                                                            remainingBudget.value
                                                        )) ?: 0
                                                    }",
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                )
                                            }
                                        }
                                    }
                                }

                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 16.dp),
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Spacer(modifier = Modifier.height(20.dp))
                                        Text(
                                            text = "Wishing You",
                                            color = textColor.copy(0.75f),
                                            fontSize = 23.sp,
                                            fontFamily = monteSB,
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "A Very Happy ❤️ Journey",
                                            color = textColor.copy(0.65f),
                                            fontSize = 13.sp,
                                            fontFamily = monteSB,
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }

                                }
                            }

                        }

                    }
                }

                AnimatedVisibility(
                    visible = isDeleteSheetOpen.value,
                    enter = slideInVertically(
                        initialOffsetY = {
                            it
                        }, animationSpec = tween(
                            durationMillis = 50,
                        )
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = {
                            -it
                        }, animationSpec = tween(
                            durationMillis = 80,
                        )
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        DeleteBottomSheet(
                            isDeleteSuccess = isDeleteSheetOpen,
                            isDeleteUndo = isDeleteUndo,
                            isDeleteClicked = isDeleteClicked,
                        )
                    }
                }
                AnimatedVisibility(
                    visible = isDeleteClicked.value,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 50,
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 50,
                        )
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val currenanim by rememberLottieComposition(
                            spec = LottieCompositionSpec.Asset("boom.json")
                        )
                        LottieAnimation(
                            composition = currenanim,
                            iterations = 1,
                            contentScale = ContentScale.Crop,
                            speed = 0.85f,
                            modifier = Modifier
                                .size(200.dp)
                        )
                    }
                    LaunchedEffect(key1 = isDeleteClicked) {
                        if (isDeleteClicked.value) {
                            delay(2000)
                            isDeleteClicked.value = false
                            isDeleteSheetOpen.value = false
                            isDeleteUndo.value = false
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
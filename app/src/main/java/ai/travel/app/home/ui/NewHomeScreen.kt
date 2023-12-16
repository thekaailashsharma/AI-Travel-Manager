package ai.travel.app.home.ui

import ai.travel.app.home.ApiState
import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.base64ToByteArray
import ai.travel.app.newTrip.NewTripScreen
import ai.travel.app.newTrip.NewTripViewModel
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewHomeScreen(
    viewModel: HomeViewModel,
    bottomBarPadding: PaddingValues,
    newTripViewModel: NewTripViewModel,
    isBottomBarVisible: MutableState<Boolean>,
    navController: NavController
) {

    val modalSheetStates = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = false
        )
    )
    val listState = rememberLazyListState()
    val collapseThreshold = remember {
        mutableFloatStateOf(0.5f)
    }

    val state = viewModel.imageState.collectAsState()

    println("MyStateeeeee: ${state.value}")



//    LaunchedEffect(modalSheetStates.bottomSheetState) {
//        snapshotFlow { modalSheetStates.bottomSheetState.isVisible }.collect { isVisible ->
//            isBottomBarVisible.value = !isVisible
//        }
//    }


    val isCollapsed =  remember(listState) {
        derivedStateOf {
            val firstVisibleItemOffset = listState.firstVisibleItemScrollOffset
            val firstItemHeight = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
            val totalOffset =
                (firstVisibleItemOffset.toFloat() / firstItemHeight.toFloat())

            totalOffset > collapseThreshold.floatValue
        }
    }
    val trips = viewModel.allTrips.collectAsState(initial = emptyList())
    BottomSheetScaffold(
        sheetContent = {
//            NewTripScreen(
//                viewModel = newTripViewModel,
//                sheetScaffoldState = modalSheetStates,
//                homeViewModel = viewModel
//            )
        },
        sheetContainerColor = CardBackground,
        scaffoldState = modalSheetStates,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetPeekHeight = 0.dp,
    ) {
        Scaffold(
            topBar = {
//                CollapsedTopBarHomeScreen(
//                    imageUrl = "https://lh3.googleusercontent.com/a/ACg8ocLRSg1ANIUVzU42MCsMSsHnHvu_MeSrh7lLkADF4zZptKg=s576-c-no",
//                    isCollapsed = isCollapsed.value,
//                    scroll = listState,
//                )
            },
        ) { padding ->
            println(padding)
            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(appGradient)
                        .padding(padding)
                        .padding(bottomBarPadding),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(appGradient)
                                .then(
                                    if (viewModel.isAnimationVisible.value)
                                        Modifier.blur(10.dp)
                                    else Modifier
                                ),
                            state = listState
                        ) {

                            item {
                                ExpandedTopBarHomeScreen(
                                    imageUrl = "https://lh3.googleusercontent.com/a/ACg8ocLRSg1ANIUVzU42MCsMSsHnHvu_MeSrh7lLkADF4zZptKg=s576-c-no",
                                )
                            }

                            item {
                                PersonalRoutes(
                                    sheetState = modalSheetStates,
                                    homeViewModel = viewModel,
                                    navController = navController
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                            }


                        }
                        if (viewModel.isAnimationVisible.value) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    when (state.value) {
                                        is ApiState.Error -> {

                                        }

                                        is ApiState.Loaded -> {
                                            val currenanim by rememberLottieComposition(
                                                spec = LottieCompositionSpec.Asset("location.json")
                                            )
                                            LottieAnimation(
                                                composition = currenanim,
                                                iterations = Int.MAX_VALUE,
                                                contentScale = ContentScale.Crop,
                                                speed = 1f,
                                                modifier = Modifier
                                                    .size(250.dp)
                                            )

                                            Text(text = "Fetching your location", color = textColor, fontSize = 18.sp)
                                        }

                                        ApiState.Loading -> {
                                            val currenanim by rememberLottieComposition(
                                                spec = LottieCompositionSpec.Asset("location.json")
                                            )
                                            LottieAnimation(
                                                composition = currenanim,
                                                iterations = Int.MAX_VALUE,
                                                contentScale = ContentScale.Crop,
                                                speed = 1f,
                                                modifier = Modifier
                                                    .size(250.dp)
                                            )

                                            Text(text = "Fetching your location", color = textColor, fontSize = 18.sp)
                                        }

                                        ApiState.NotStarted -> {

                                        }

                                        ApiState.ReceivedGeoCodes -> {
                                            val currenanim by rememberLottieComposition(
                                                spec = LottieCompositionSpec.Asset("itineary.json")
                                            )
                                            LottieAnimation(
                                                composition = currenanim,
                                                iterations = Int.MAX_VALUE,
                                                contentScale = ContentScale.Crop,
                                                speed = 1f,
                                                modifier = Modifier
                                                    .size(250.dp)
                                            )
                                            Text(text = "Planning Itineary", color = textColor, fontSize = 18.sp)
                                        }

                                        ApiState.ReceivedPhoto -> {
                                            viewModel.isAnimationVisible.value = false

                                        }

                                        ApiState.ReceivedPhotoId -> {
                                            val currenanim by rememberLottieComposition(
                                                spec = LottieCompositionSpec.Asset("getset.json")
                                            )
                                            LottieAnimation(
                                                composition = currenanim,
                                                iterations = Int.MAX_VALUE,
                                                contentScale = ContentScale.Crop,
                                                speed = 1f,
                                                modifier = Modifier
                                                    .size(250.dp)
                                            )
                                            Text(text = "Get Set Go", color = textColor, fontSize = 18.sp)
                                        }

                                        ApiState.ReceivedPlaceId -> {
                                            val currenanim by rememberLottieComposition(
                                                spec = LottieCompositionSpec.Asset("onyourmark.json")
                                            )
                                            LottieAnimation(
                                                composition = currenanim,
                                                iterations = Int.MAX_VALUE,
                                                contentScale = ContentScale.Crop,
                                                speed = 1f,
                                                modifier = Modifier
                                                    .size(250.dp)
                                            )
                                            Text(text = "ON your Mark", color = textColor, fontSize = 18.sp)
                                        }

                                        else -> {}
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
package ai.travel.app.home.ui

import ai.travel.app.R
import ai.travel.app.bottomBar.items
import ai.travel.app.datastore.UserDatastore
import ai.travel.app.datastore.UserDatastore.Companion.pfp
import ai.travel.app.home.ApiState
import ai.travel.app.home.HomeViewModel
import ai.travel.app.navigation.Screens
import ai.travel.app.newTrip.NewTripScreen
import ai.travel.app.newTrip.NewTripViewModel
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.bottomBarBackground
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.monteSB
import ai.travel.app.ui.theme.textColor
import ai.travel.app.utils.ProfileImage
import ai.travel.app.utils.dashedBorder
import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreenMain(
    viewModel: HomeViewModel,
    bottomBarPadding: PaddingValues,
    newTripViewModel: NewTripViewModel,
    isBottomBarVisible: MutableState<Boolean>,
    navController: NavController,
) {
    val context = LocalContext.current
    val userDatastore = UserDatastore(context)
    val userName = userDatastore.getName.collectAsState(initial = "")
    val pfp = userDatastore.getPfp.collectAsState(initial = "")
    val modalSheetStates = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden, skipPartiallyExpanded = false
        )
    )
    val listState = rememberLazyListState()
    val collapseThreshold = remember {
        mutableFloatStateOf(0.5f)
    }

    val state = viewModel.imageState.collectAsState()
    val isCollapsed = remember(listState) {
        derivedStateOf {
            val firstVisibleItemOffset = listState.firstVisibleItemScrollOffset
            val firstItemHeight = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
            val totalOffset = (firstVisibleItemOffset.toFloat() / firstItemHeight.toFloat())

            totalOffset > collapseThreshold.floatValue
        }
    }
    BottomSheetScaffold(
        sheetContent = {

        },
        sheetContainerColor = CardBackground,
        scaffoldState = modalSheetStates,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetPeekHeight = 0.dp,
    ) {
        Scaffold(
            topBar = {
                CollapsedTopBarHomeScreen(
                    imageUrl = pfp.value,
                    isCollapsed = isCollapsed.value,
                    scroll = listState,
                )
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
                                    if (viewModel.isAnimationVisible.value) Modifier.blur(10.dp)
                                    else Modifier
                                ), state = listState
                        ) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 0.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .clip(
                                                RoundedCornerShape(
                                                    0.dp, 0.dp, 50.dp, 50.dp
                                                )
                                            )
                                            .fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = CardBackground.copy(0.8f)
                                        ),
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        top = 45.dp,
                                                        bottom = 15.dp,
                                                        end = 25.dp,
                                                        start = 25.dp
                                                    ),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {

                                                    Text(
                                                        text = "WELCOME TO ",
                                                        color = lightText,
                                                        fontSize = 18.sp,
                                                        fontFamily = monteSB,
                                                        modifier = Modifier.padding(bottom = 7.dp)
                                                    )

                                                        Text(
                                                            text = "Mumbai",
                                                            color = Color.White,
                                                            fontSize = 35.sp,
                                                            fontFamily = monteSB,
                                                            modifier = Modifier
                                                        )


                                                    Text(
                                                        text = userName.value,
                                                        color = lightText,
                                                        fontSize = 23.sp,
                                                        fontFamily = monteSB,
                                                        modifier = Modifier.padding(bottom = 7.dp)
                                                    )

                                                }
                                                ProfileImage(imageUrl = pfp.value,
                                                    modifier = Modifier
                                                        .size(70.dp)
                                                        .border(
                                                            width = 1.dp,
                                                            color = textColor,
                                                            shape = CircleShape
                                                        )
                                                        .padding(2.dp)
                                                        .clip(CircleShape)
                                                        .clickable {
                                                            navController.navigate(Screens.Profile.route)
                                                        })
                                            }
                                            Spacer(modifier = Modifier.height(15.dp))
                                        }
                                    }

                                }





                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 16.dp, start = 14.dp, end = 14.dp, bottom = 10.dp
                                        ), horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Row {
                                        Icon(
                                            imageVector = Icons.Filled.LocationOn,
                                            contentDescription = null,
                                            modifier = Modifier,
                                            tint = lightText
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Personal Routes",
                                            fontSize = 20.sp,
                                            color = textColor
                                        )
                                    }
                                    Card(colors = CardDefaults.cardColors(containerColor = lightText)) {
                                        Text(
                                            text = "More",
                                            modifier = Modifier.padding(all = 7.dp),
                                            color = textColor
                                        )
                                    }

                                }
                            }
                            item {
                                PersonalRoutes(
                                    sheetState = modalSheetStates,
                                    homeViewModel = viewModel,
                                    navController = navController
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp, horizontal = 14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Filled.LocationOn,
                                            contentDescription = null,
                                            modifier = Modifier,
                                            tint = lightText
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))

                                        Text(
                                            text = "Explore Mumbai",
                                            fontSize = 20.sp,
                                            color = textColor
                                        )


                                    }
                                }
                                LazyRow {
                                    items(image) { icon ->

                                        Spacer(modifier = Modifier.width(15.dp))


                                        Card(
                                            modifier = Modifier
                                                .height(250.dp)
                                                .width(170.dp)
                                        ) {
                                            Box(modifier = Modifier.fillMaxSize()) {

                                                Image(
                                                    painter = painterResource(id = icon.image ),
                                                    contentDescription = "",
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.BottomCenter
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .align(Alignment.BottomCenter)
                                                    ) {
                                                        Card(
                                                            modifier = Modifier

                                                        ) {

                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(
                                                                        horizontal = 10.dp,
                                                                        vertical = 10.dp
                                                                    ),
                                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Text(
                                                                    text = icon.name,
                                                                    fontSize = 14.sp,

                                                                    )

                                                                Text(
                                                                    text = icon.distance,
                                                                    fontSize = 14.sp,
                                                                )


                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(15.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp, horizontal = 14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Filled.LocationOn,
                                            contentDescription = null,
                                            modifier = Modifier,
                                            tint = lightText
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))

                                        Text(
                                            text = "Explore Majestic Maharashtra",
                                            fontSize = 20.sp,
                                            color = textColor
                                        )

                                    }
                                }

                                LazyRow(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)) {
                                    items(image2) { icon ->

                                        Spacer(modifier = Modifier.width(20.dp))
                                        Card(
                                            modifier = Modifier.size(100.dp),
                                            shape = CircleShape,
                                        ) {

                                            Image(
                                                painter = painterResource(id = icon),
                                                contentDescription = "",
                                                contentScale = ContentScale.FillBounds,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(50.dp))

                                Column (modifier = Modifier.fillMaxWidth().padding(start = 20.dp),
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.Center){
                                    Text(
                                        text = "We make travel fun",
                                        color = lightText,
                                        fontSize = 15.sp,
                                        fontFamily = monteSB,
                                        modifier = Modifier.padding(bottom = 7.dp)
                                    )
                                    Text(
                                        text = "Waste wise, Reward Rise",
                                        color = lightText,
                                        fontSize = 15.sp,
                                        fontFamily = monteSB,
                                        modifier = Modifier.padding(bottom = 7.dp)
                                    )
                                    Text(
                                        text = "Effort by Team Centennials ❤️",
                                        color = lightText,
                                        fontSize = 15.sp,
                                        fontFamily = monteSB,
                                        modifier = Modifier.padding(bottom = 7.dp)
                                    )


                                }

                                Spacer(modifier = Modifier.height(150.dp))
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
                                                modifier = Modifier.size(250.dp)
                                            )

                                            Text(
                                                text = "Fetching your location",
                                                color = textColor,
                                                fontSize = 18.sp
                                            )
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
                                                modifier = Modifier.size(250.dp)
                                            )

                                            Text(
                                                text = "Fetching your location",
                                                color = textColor,
                                                fontSize = 18.sp
                                            )
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
                                                modifier = Modifier.size(250.dp)
                                            )
                                            Text(
                                                text = "Planning Itineary",
                                                color = textColor,
                                                fontSize = 18.sp
                                            )
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
                                                modifier = Modifier.size(250.dp)
                                            )
                                            Text(
                                                text = "Get Set Go",
                                                color = textColor,
                                                fontSize = 18.sp
                                            )
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
                                                modifier = Modifier.size(250.dp)
                                            )
                                            Text(
                                                text = "ON your Mark",
                                                color = textColor,
                                                fontSize = 18.sp
                                            )
                                        }

                                        ApiState.CalculatedDistance -> {
                                            val currenanim by rememberLottieComposition(
                                                spec = LottieCompositionSpec.Asset("go.json")
                                            )
                                            LottieAnimation(
                                                composition = currenanim,
                                                iterations = Int.MAX_VALUE,
                                                contentScale = ContentScale.Crop,
                                                speed = 1f,
                                                modifier = Modifier.size(250.dp)
                                            )
                                            Text(
                                                text = "Calculating Distance",
                                                color = textColor,
                                                fontSize = 18.sp
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


@Composable
fun TravelCards(text1: String, text2: String, text3: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 14.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = bottomBarBackground)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 14.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.size(100.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.compose),
                        contentDescription = "",
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = text1)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Text(text = text2, fontSize = 10.sp, color = textColor)
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Text(text = text3, fontSize = 10.sp, color = textColor)
                    }
                }

            }
        }
    }
}


val image = listOf(
    MumbaiImages(
        R.drawable.mum4,
        "Taj Hotel",
        "2.1 mi"
    ),
    MumbaiImages(
        R.drawable.mum3,
        "CSMT",
        "2.1 mi"
    ),
    MumbaiImages(
        R.drawable.mum6,
        "Gateway",
        "2.1 mi"
    ),
    MumbaiImages(
        R.drawable.mum2,
        "CSMT",
        "2.1 mi"
    ),
    MumbaiImages(
        R.drawable.mum1,
        "Wankhede",
        "2.1 mi"
    ),
    MumbaiImages(
        R.drawable.mum5,
        "HajiAli",
        "2.1 mi"
    ),

    )

data class MumbaiImages(
    val image: Int,
    val name: String,
    val distance: String
)

val image2= listOf(
    R.drawable.mum1,
    R.drawable.mum2,
    R.drawable.mum3,
    R.drawable.mum4,
    R.drawable.mum5,
    R.drawable.mum6
)

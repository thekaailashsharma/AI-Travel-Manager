package ai.travel.app.home.ui

import ai.travel.app.R
import ai.travel.app.bottomBar.items
import ai.travel.app.home.HomeViewModel
import ai.travel.app.newTrip.NewTripScreen
import ai.travel.app.newTrip.NewTripViewModel
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.bottomBarBackground
import ai.travel.app.ui.theme.lightText
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreenMain(
    viewModel: HomeViewModel,
    bottomBarPadding: PaddingValues,
    newTripViewModel: NewTripViewModel,
    isBottomBarVisible: MutableState<Boolean>,
    navController: NavController,
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
            NewTripScreen(
                viewModel = newTripViewModel,
                sheetScaffoldState = modalSheetStates,
                homeViewModel = viewModel
            )
        },
        sheetContainerColor = CardBackground,
        scaffoldState = modalSheetStates,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetPeekHeight = 0.dp,
    ) {
        Scaffold(
            topBar = {
                CollapsedTopBarHomeScreen(
                    imageUrl = R.drawable.app_icon,
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
                                    if (viewModel.isAnimationVisible.value)
                                        Modifier.blur(10.dp)
                                    else Modifier
                                ),
                            state = listState
                        ) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {

                                    ProfileImage(
                                        imageUrl = R.drawable.app_icon,
                                        modifier = Modifier
                                            .size(150.dp)
                                            .clip(CircleShape),
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(text = "Welcome to", fontSize = 20.sp, color = textColor)
                                    Text(text = "Tripify", fontSize = 30.sp, color = textColor)

                                }
                                Divider(modifier = Modifier, color = lightText)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 16.dp,
                                            start = 14.dp,
                                            end = 14.dp,
                                            bottom = 10.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
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

                                        Text(text = "Quests", fontSize = 20.sp, color = textColor)
                                    }

                                    Card(colors = CardDefaults.cardColors(containerColor = lightText)) {
                                        Text(
                                            text = "More",
                                            modifier = Modifier.padding(all = 6.dp),
                                            color = textColor
                                        )
                                    }
                                }


                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 0.dp, horizontal = 14.dp),
                                    horizontalArrangement = Arrangement.Start
                                ) {


                                    Card(
                                        modifier = Modifier.padding(horizontal = 10.dp),
                                        colors = CardDefaults.cardColors(containerColor = lightText)
                                    ) {
                                        Text(
                                            text = "Recommended",
                                            modifier = Modifier.padding(all = 5.dp),
                                            color = textColor
                                        )
                                    }

                                    Card(colors = CardDefaults.cardColors(containerColor = lightText)) {
                                        Text(
                                            text = "Current ",
                                            modifier = Modifier.padding(all = 5.dp),
                                            color = textColor
                                        )
                                    }

                                }


                                TravelCards(
                                    text1 = "Witness the balance of design and innovation",
                                    text2 = "Quests", text3 = "Quests"
                                )
                                TravelCards(
                                    text1 = "Witness the balance of design and innovation",
                                    text2 = "Quests", text3 = "Quests"
                                )
                                TravelCards(
                                    text1 = "Witness the balance of design and innovation",
                                    text2 = "Quests", text3 = "Quests"
                                )


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
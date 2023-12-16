package ai.travel.app.home.ui

import ai.travel.app.R
import ai.travel.app.database.travel.TripsEntity
import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.base64ToByteArray
import ai.travel.app.navigation.Screens
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import ai.travel.app.utils.dashedBorder
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalRoutes(
    sheetState: BottomSheetScaffoldState,
    homeViewModel: HomeViewModel,
    navController: NavController
) {

    val items = homeViewModel.allTrips.collectAsState(initial = emptyList())
    val newItems = remember {
        mutableStateListOf<TripInfo?>()
    }
    LaunchedEffect(key1 = homeViewModel.allTrips) {
        homeViewModel.allTrips.collectLatest {
            if (it.isNotEmpty()) {
                newItems.clear()
                newItems.addAll(extractTripInfo(it))
            }
        }
    }


    Column {
        LazyRow() {
            item {
                NewRouteCard(sheetState, navController)
            }
            items(newItems.reversed()) { item ->
                RouteCard(item, navController, homeViewModel)
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRouteCard(sheetState: BottomSheetScaffoldState, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(180.dp)
            .padding(16.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null,
                onClick = {
                    navController.navigate(Screens.NewTrip.route)
                }),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight(0.6f)
                    .dashedBorder(1.dp, textColor, 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(10.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.route),
                        contentDescription = "",
                        modifier = Modifier.size(50.dp),
                        tint = textColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Add New Route",
                fontSize = 15.sp,
                color = textColor,
                modifier = Modifier.weight(1f)
            )

        }
    }
}

@Composable
fun RouteCard(
    item: TripInfo?,
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(180.dp)
            .padding(16.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null,
                onClick = {
                    homeViewModel.currentDestination.value = item?.name ?: ""
                    navController.navigate(Screens.TripDetails.route)
                }),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight(0.6f)
                    .dashedBorder(1.dp, textColor, 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(10.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item?.photoBase64?.let {
                        convertImageByteArrayToBitmap(base64ToByteArray(it))?.asImageBitmap()
                            ?.let { it1 ->
                                Image(
                                    bitmap = it1,
                                    contentDescription = "some useful description",
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item?.name ?: "",
                fontSize = 18.sp,
                color = textColor,
            )
        }
    }
}

data class TripInfo(
    val name: String?,
    val photoBase64: String?,
    val budget: String?
)

fun extractTripInfo(items: List<TripsEntity?>): List<TripInfo> {
    val uniqueNamesMap = items.groupBy { it?.destination }

    return uniqueNamesMap.map { entry ->
        val firstMatchNotNull = entry.value.firstOrNull { it?.photoBase64 != null && it.budget != null }
        TripInfo(
            name = entry.value.firstOrNull()?.destination,
            photoBase64 = firstMatchNotNull?.photoBase64,
            budget = firstMatchNotNull?.budget
        )
    }
}
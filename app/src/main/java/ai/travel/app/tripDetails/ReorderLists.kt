package ai.travel.app.tripDetails

import ai.travel.app.database.travel.TripsEntity
import ai.travel.app.home.CustomMarker
import ai.travel.app.home.HomeViewModel
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.borderBrush
import ai.travel.app.ui.theme.bottomBarBackground
import ai.travel.app.ui.theme.bottomBarBorder
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ReorderLists(viewModel: HomeViewModel, paddingValues: PaddingValues) {
    val currentDay = remember { mutableStateOf("1") }
    var dayTrips =
        viewModel.getTrips(currentDay.value, viewModel.currentDestination.value)
            .collectAsState(initial = emptyList())
    val days = viewModel.uniqueDays(viewModel.currentDestination.value)
        .collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    var newTrips = remember { mutableStateOf<List<TripsEntity?>>(emptyList()) }
    LaunchedEffect(key1 = dayTrips.value) {
        println("Gone insideeee")
        newTrips.value = emptyList()
        newTrips.value = dayTrips.value.toList()
    }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        println("frommmmmmmm = $from && to = $to")
        newTrips.value = newTrips.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        newTrips.value = newTrips.value.toMutableList().apply {
            this[0]?.timeOfDay = "Morning"
            this[1]?.timeOfDay = "Afternoon"
            this[2]?.timeOfDay = "Evening"
        }


    })
    val isLoading = viewModel.isReorderLoading.collectAsState(initial = false)
    Box(
        modifier = Modifier
            .background(CardBackground)
            .fillMaxWidth()
            .fillMaxHeight(0.91f)
            .padding(paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier
                .reorderable(state)
                .detectReorderAfterLongPress(state)
                .padding(
                    top = 40.dp,
                    bottom = 40.dp,
                )
                .then(
                    if (isLoading.value) Modifier.blur(10.dp) else Modifier
                ),
            state = state.listState,
        ) {
            items(newTrips.value, key = {
                it?.id ?: 0
            }) { item ->
                ReorderableItem(
                    state,
                    key = item?.id,
                    defaultDraggingModifier = Modifier,
                ) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 8.dp else 0.dp, label = "")
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 15.dp, bottom = 10.dp, top = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                            contentColor = textColor
                        ),
                        elevation = CardDefaults.cardElevation(elevation.value),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(0.5.dp, color = bottomBarBorder.copy(0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(start = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = item?.timeOfDay ?: "",
                                    color = textColor,
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    softWrap = true
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    VerticalDashedDivider(
                                        color = lightText,
                                        height = 100,
                                        dashWidth = 14f,
                                        gapWidth = 10f
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = item?.name ?: "",
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
                                        text = item?.budget ?: "",
                                        color = textColor,
                                        fontSize = 12.sp,
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }

            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp, top = 20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = {
                        if (newTrips.value.isNotEmpty()) {
                            println("changed.newTrips.size = ${newTrips.value.size}")
                            println("changed.newTrips.value = ${newTrips.value}")
                            viewModel.updateTripsWithDistance(
                                newTripsEntity = newTrips.value.toMutableList(),
                                fromDestination = viewModel.currentDestination.value,
                                oldTrips = dayTrips.value.toMutableList(),
                                fromDay = currentDay.value
                            )
                        }
                    },
                    modifier = Modifier
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                    ),
                    border = BorderStroke(1.dp, brush = borderBrush),
                    enabled = !isLoading.value
                ) {
                    Text(
                        text = "Save",
                        color = textColor,
                        fontSize = 15.sp,
                        modifier = Modifier,
                        softWrap = true
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow {
                    items(days.value) { day ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (day == currentDay.value)
                                    lightText else Color.Transparent,
                            ),
                            border = BorderStroke(1.dp, brush = borderBrush),
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp, bottom = 20.dp)
                .then(
                    if (isLoading.value) Modifier.blur(10.dp) else Modifier
                ),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reorder your itinerary",
                    color = textColor,
                    fontSize = 20.sp,
                    modifier = Modifier,
                    softWrap = true
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Click and drag to reorder",
                    color = lightText,
                    fontSize = 8.sp,
                    modifier = Modifier,
                    softWrap = true
                )
            }
        }
        if (isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp, top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = lightText)
            }
        }
    }

}


@Composable
fun VerticalReorderList() {
    val data = remember { mutableStateOf(List(100) { "Item $it" }) }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        data.value = data.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })
    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(data.value, { it }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Text(item)
                }
            }
        }
    }
}


package ai.travel.app.home.ui

import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.base64ToByteArray
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.lightText
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewHomeScreen(viewModel: HomeViewModel, bottomBarPadding: PaddingValues) {

    val listState = rememberLazyListState()
    val collapseThreshold = remember {
        mutableFloatStateOf(0.5f)
    }

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

    Scaffold(
        topBar = {
            CollapsedTopBarHomeScreen(
                imageUrl = "https://lh3.googleusercontent.com/a/ACg8ocLRSg1ANIUVzU42MCsMSsHnHvu_MeSrh7lLkADF4zZptKg=s576-c-no",
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(appGradient),
                    state = listState
                ) {

                    item {
                        ExpandedTopBarHomeScreen(
                            imageUrl = "https://lh3.googleusercontent.com/a/ACg8ocLRSg1ANIUVzU42MCsMSsHnHvu_MeSrh7lLkADF4zZptKg=s576-c-no",
                        )
                    }

                    item {
                        PersonalRoutes()

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(trips.value) { newData ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Place: ${newData?.name}",
                                color = lightText,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            newData?.photoBase64?.let {
                                Image(
                                    bitmap = convertImageByteArrayToBitmap(base64ToByteArray(it)).asImageBitmap(),
                                    contentDescription = "some useful description",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape),
                                )
                            }


                        }
                    }

                    items(trips.value) { newData ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Place: ${newData?.name}",
                                color = lightText,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            newData?.photoBase64?.let {
                                Image(
                                    bitmap = convertImageByteArrayToBitmap(base64ToByteArray(it)).asImageBitmap(),
                                    contentDescription = "some useful description",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape),
                                )
                            }


                        }
                    }

                    items(trips.value) { newData ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Place: ${newData?.name}",
                                color = lightText,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            newData?.photoBase64?.let {
                                Image(
                                    bitmap = convertImageByteArrayToBitmap(base64ToByteArray(it)).asImageBitmap(),
                                    contentDescription = "some useful description",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape),
                                )
                            }


                        }
                    }

                    items(trips.value) { newData ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Place: ${newData?.name}",
                                color = lightText,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            newData?.photoBase64?.let {
                                Image(
                                    bitmap = convertImageByteArrayToBitmap(base64ToByteArray(it)).asImageBitmap(),
                                    contentDescription = "some useful description",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape),
                                )
                            }


                        }
                    }

                    items(trips.value) { newData ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Place: ${newData?.name}",
                                color = lightText,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            newData?.photoBase64?.let {
                                Image(
                                    bitmap = convertImageByteArrayToBitmap(base64ToByteArray(it)).asImageBitmap(),
                                    contentDescription = "some useful description",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape),
                                )
                            }


                        }
                    }
                }
            }
        }
    }
}
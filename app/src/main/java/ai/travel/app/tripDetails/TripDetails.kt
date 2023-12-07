package ai.travel.app.tripDetails

import ai.travel.app.R
import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.base64ToByteArray
import ai.travel.app.home.ui.convertImageByteArrayToBitmap
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.bottomBarBackground
import ai.travel.app.ui.theme.bottomBarBorder
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import ai.travel.app.utils.ProfileImage
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TripDetailsScreen(viewModel: HomeViewModel) {
    val cardData1 = listOf(
        GridCardData(
            topText = "Country",
            bottomText = "India",
            icon = Icons.Filled.Public
        ),
        GridCardData(
            topText = "City",
            bottomText = "Mumbai",
            icon = Icons.Filled.Public
        ),
    )
    val cardData2 = listOf(
        GridCardData(
            topText = "Budget",
            bottomText = "Rs. 1000",
            icon = Icons.Filled.Wallet
        ),
        GridCardData(
            topText = "Days",
            bottomText = "3",
            icon = Icons.Filled.CalendarToday
        ),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {

        val trips = viewModel.allTrips.collectAsState(initial = emptyList())

        if (trips.value.isEmpty()) {
            CircularProgressIndicator()
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                trips.value[0]?.photoBase64?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.24f),
                        shape = RoundedCornerShape(0.dp),
                        elevation = CardDefaults.cardElevation(7.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Image(
                            bitmap = convertImageByteArrayToBitmap(base64ToByteArray(it)).asImageBitmap(),
                            contentDescription = "some useful description",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .drawWithCache {
                                    val gradient = Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(0.8f)),
                                        startY = size.height / 5.5f,
                                        endY = size.height
                                    )
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(gradient, blendMode = BlendMode.Multiply)
                                    }
                                },
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    Column {
                        Spacer(modifier = Modifier.fillMaxHeight(0.13f))
                        Text(
                            text = "Mumbai",
                            color = textColor,
                            fontSize = 35.sp,
                            modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
                        )

                        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
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

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }


    }
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
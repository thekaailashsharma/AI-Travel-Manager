package com.example.mapbox_map

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.mapbox_map.ui.LocationService
import com.example.requestlocation.R
import com.mapbox.geojson.Point
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp

class MainActivity : ComponentActivity() {

    init {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                MapboxNavigationApp.attach(owner)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapScreen()
        }
    }

}

@Composable
fun MapScreen() {

    var point: Point? by remember {
        mutableStateOf(null)
    }
    var relaunch by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current


    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (!permissions.values.all { it }) {
                //handle permission denied
            } else {
                relaunch = !relaunch
            }
        }
    )
    var isClicked = remember { mutableStateOf(false) }
    var isReset = remember { mutableStateOf(false) }
    var currentPoint = remember { mutableStateOf<MapBoxPoint?>(null) }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            MapBoxMap(
                onPointChange = { point = it },
                modifier = Modifier
                    .fillMaxSize(),
                isClicked = isClicked,
                currentPoint = currentPoint,
                isReset = isReset,
                latitude = 0.0,
                longitude = 0.0,
                points = listOf()
            )
        }
        AnimatedVisibility(
            visible = !isClicked.value,
            enter = slideInVertically(tween(1000), initialOffsetY = {
                it
            }),
            exit = slideOutVertically(tween(1000), targetOffsetY = {
                it
            })
        ) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
//                LazyRow(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentPadding = PaddingValues(30.dp)
//                ) {
//                    items(items) { item ->
//                        Card(
//                            modifier = Modifier
//                                .width(300.dp)
//                                .height(200.dp)
//                                .padding(end = 10.dp),
//                            shape = RoundedCornerShape(10.dp),
//                            elevation = CardDefaults.cardElevation(10.dp)
//                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxSize(),
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.Center
//                            ) {
//                                Image(
//                                    painter = painterResource(id = item.image),
//                                    contentDescription = null,
//                                    contentScale = ContentScale.Crop,
//                                    modifier = Modifier
//                                        .fillMaxWidth(0.5f)
//                                        .fillMaxHeight()
//                                )
//                                Spacer(modifier = Modifier.width(10.dp))
//                                Column(modifier = Modifier.fillMaxWidth()) {
//                                    Spacer(modifier = Modifier.height(10.dp))
//                                    Text(
//                                        text = item.location,
//                                        color = Color.Black,
//                                        fontSize = 20.sp,
//                                        fontWeight = FontWeight.Bold,
//                                        softWrap = true
//                                    )
//                                    Spacer(modifier = Modifier.height(10.dp))
//                                    Text(
//                                        text = item.time,
//                                        color = Color.Black,
//                                        fontSize = 10.sp,
//                                        fontWeight = FontWeight.Normal
//                                    )
//                                    Spacer(modifier = Modifier.height(10.dp))
//                                    OutlinedButton(
//                                        onClick = {
//                                            isReset.value = false
//                                            isClicked.value = true
////                                            currentPoint.value = item.point
//                                        },
//                                        shape = RoundedCornerShape(10.dp)
//                                    ) {
//                                        Row(verticalAlignment = Alignment.CenterVertically) {
//                                            Icon(
//                                                imageVector = Icons.Filled.LocationOn,
//                                                contentDescription = null,
//                                                modifier = Modifier.size(20.dp),
//                                                tint = Color.Black
//                                            )
//                                            Spacer(modifier = Modifier.width(10.dp))
//                                            Text(
//                                                text = "Navigate",
//                                                color = Color.Black,
//                                                fontSize = 10.sp,
//                                                fontWeight = FontWeight.Normal
//                                            )
//                                        }
//                                    }
//                                }
//                                Spacer(modifier = Modifier.width(10.dp))
//                            }
//                        }
//                    }
//
//                }
            }
//        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 25.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Card(
            modifier = Modifier
                .padding(end = 10.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Button(
                onClick = {
                    isReset.value = true
                    isClicked.value = false

                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Reset",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,

                    )
            }

        }
    }

    LaunchedEffect(key1 = relaunch) {
        try {
            val location = LocationService().getCurrentLocation(context)
            point = Point.fromLngLat(location.longitude, location.latitude)

        } catch (e: LocationService.LocationServiceException) {
            when (e) {
                is LocationService.LocationServiceException.LocationDisabledException -> {
                    //handle location disabled, show dialog or a snack-bar to enable location
                }

                is LocationService.LocationServiceException.MissingPermissionException -> {
                    permissionRequest.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                is LocationService.LocationServiceException.NoNetworkEnabledException -> {
                    //handle no network enabled, show dialog or a snack-bar to enable network
                }

                is LocationService.LocationServiceException.UnknownException -> {
                    //handle unknown exception
                }
            }
        }
    }
}

//val items = listOf(
//    MapItem(
//        image = R.drawable.one,
//        location = "Taj Mahal, Agra",
//        time = "2 hours ago",
//        point = Point.fromLngLat(78.0421, 27.1750) // Taj Mahal's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.two,
//        location = "Jaipur Palace, Jaipur",
//        time = "1 day ago",
//        point = Point.fromLngLat(75.8376, 26.9124) // Jaipur Palace's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.three,
//        location = "Kerala Backwaters, Alappuzha",
//        time = "3 days ago",
//        point = Point.fromLngLat(76.3300, 9.4981) // Kerala Backwaters' latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.two,
//        location = "SIES GST NERUL",
//        time = "3 days ago",
//        point = Point.fromLngLat(73.0231, 19.0428) // Kerala Backwaters' latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.one, // Repeating image
//        location = "Hampi, Karnataka",
//        time = "4 days ago",
//        point = Point.fromLngLat(76.4741, 15.3350) // Hampi's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.two, // Repeating image
//        location = "Lotus Temple, Delhi",
//        time = "5 days ago",
//        point = Point.fromLngLat(77.2647, 28.5535) // Lotus Temple's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.three, // Repeating image
//        location = "Goa Beach, Goa",
//        time = "6 days ago",
//        point = Point.fromLngLat(73.7319, 15.2993) // Goa Beach's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.one, // Repeating image
//        location = "Mysore Palace, Mysore",
//        time = "7 days ago",
//        point = Point.fromLngLat(76.6550, 12.3051) // Mysore Palace's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.two, // Repeating image
//        location = "Jaisalmer Fort, Jaisalmer",
//        time = "8 days ago",
//        point = Point.fromLngLat(70.9100, 26.9124) // Jaisalmer Fort's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.three, // Repeating image
//        location = "Kanchipuram Temples, Tamil Nadu",
//        time = "9 days ago",
//        point = Point.fromLngLat(79.7006, 12.8431) // Kanchipuram Temples' latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.one, // Repeating image
//        location = "Ajanta Caves, Maharashtra",
//        time = "10 days ago",
//        point = Point.fromLngLat(75.7050, 20.5513) // Ajanta Caves' latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.two, // Repeating image
//        location = "Sundarbans, West Bengal",
//        time = "11 days ago",
//        point = Point.fromLngLat(88.9763, 21.9497) // Sundarbans' latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.three, // Repeating image
//        location = "Dudhsagar Falls, Goa",
//        time = "12 days ago",
//        point = Point.fromLngLat(74.3192, 15.3145) // Dudhsagar Falls' latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.one, // Repeating image
//        location = "Shimla, Himachal Pradesh",
//        time = "13 days ago",
//        point = Point.fromLngLat(77.1734, 31.1048) // Shimla's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.two, // Repeating image
//        location = "Kaziranga National Park, Assam",
//        time = "14 days ago",
//        point = Point.fromLngLat(93.2277, 26.6340) // Kaziranga National Park's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.three, // Repeating image
//        location = "Puducherry Beach, Puducherry",
//        time = "15 days ago",
//        point = Point.fromLngLat(79.8083, 11.9416) // Puducherry Beach's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.one, // Repeating image
//        location = "Mount Abu, Rajasthan",
//        time = "16 days ago",
//        point = Point.fromLngLat(72.8277, 24.5925) // Mount Abu's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.two, // Repeating image
//        location = "Rann of Kutch, Gujarat",
//        time = "17 days ago",
//        point = Point.fromLngLat(69.8597, 23.7337) // Rann of Kutch's latitude and longitude
//    ),
//    MapItem(
//        image = R.drawable.three, // Repeating image
//        location = "Tawang Monastery, Arunachal Pradesh",
//        time = "18 days ago",
//        point = Point.fromLngLat(91.6274, 27.5860) // Tawang Monastery's latitude and longitude
//    ),
//    // Repeat the cycle of images as needed
//)



data class MapItem(
    val image: Int,
    val location: String,
    val time: String,
   val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
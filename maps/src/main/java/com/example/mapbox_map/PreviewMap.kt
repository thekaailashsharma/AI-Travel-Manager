package com.example.mapbox_map

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import com.example.requestlocation.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.atmosphere.generated.atmosphere
import com.mapbox.maps.extension.style.expressions.dsl.generated.rgb
import com.mapbox.maps.extension.style.expressions.dsl.generated.zoom
import com.mapbox.maps.extension.style.layers.properties.generated.ProjectionName
import com.mapbox.maps.extension.style.projection.generated.projection
import com.mapbox.maps.extension.style.sources.generated.rasterDemSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.extension.style.terrain.generated.terrain
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager

@Composable
fun PreviewMap(
    modifier: Modifier = Modifier,
    onPointChange: (Point) -> Unit,
    isClicked: MutableState<Boolean> = mutableStateOf(false),
    isReset: MutableState<Boolean> = mutableStateOf(false),
    points: List<MapItem>,
    latitude: Double,
    longitude: Double,
    currentPoint: MutableState<MapBoxPoint?>,
) {

    val context = LocalContext.current

    val marker = remember(context) {
        context.getDrawable(R.drawable.marker)!!.toBitmap()
    }

    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }



    AndroidView(
        factory = { context ->
            val cameraOptions = CameraOptions.Builder()
                .center(Point.fromLngLat(points[0].longitude, points[0].latitude))
                .zoom(13.0)
                .pitch(45.0)
                .bearing(-17.6)
                .build()
            MapView(context).also { mapView ->
                mapView.getMapboxMap().loadStyle(
                    style(Style.SATELLITE_STREETS) {
                        val zoom = zoom()
                        Log.i("Zoommmmmmmm", "${zoom.literalValue}")
                        +terrain("terrain-enable")
                        +projection(ProjectionName.MERCATOR)
                        +atmosphere {
                            color(rgb(220.0, 159.0, 159.0)) // Pink fog / lower atmosphere
                            highColor(rgb(220.0, 159.0, 159.0)) // Blue sky / upper atmosphere
                            horizonBlend(0.4) // Exaggerate atmosphere (default is .1)
                        }
                        +rasterDemSource("raster-dem") {
                            url("mapbox://mapbox.terrain-rgb")
                        }
                        +terrain("raster-dem") {
                            exaggeration(1.5)
                        }
                    }
                )
                isScalingOut(mapView) {
                    isClicked.value = false
                }
                mapView.getMapboxMap().flyTo(
                    cameraOptions,
                    MapAnimationOptions.mapAnimationOptions {
                        duration(5000L)
                    }
                )


//                val annotationApi = mapView.annotations
//                pointAnnotationManager = annotationApi.createPointAnnotationManager()
//
//                mapView.getMapboxMap().addOnMapClickListener { p ->
//                    onPointChange(p)
//                    true
//                }
            }
        },
        update = { mapView ->
            isScalingOut(mapView) {
                isClicked.value = false
                isReset.value = true
            }
            val cameraOptions = CameraOptions.Builder()
                .center(Point.fromLngLat(longitude, latitude))
                .zoom(currentPoint.value?.zoom ?: 3.0)
                .build()
            mapView.annotations.cleanup()
            points.forEach { mapItem ->
                addAnnotationToMap(
                    context = context,
                    mapView = mapView,
                    point = Point.fromLngLat(mapItem.longitude, mapItem.latitude),
                    icon = mapItem.image
                )
            }
            if (isClicked.value) {
                mapView.getMapboxMap().flyTo(
                    cameraOptions,
                    MapAnimationOptions.mapAnimationOptions {
                        duration(5000L)
                    }
                )
            }
            if (isReset.value){
                mapView.getMapboxMap().flyTo(
                    cameraOptions,
                    MapAnimationOptions.mapAnimationOptions {
                        duration(5000L)
                    }
                )
            }


//            if (point != null) {
//                pointAnnotationManager?.let {
//                    it.deleteAll()
//                    val pointAnnotationOptions = PointAnnotationOptions()
//                        .withPoint(point)
//                        .withIconImage(marker)
//
//                    it.create(pointAnnotationOptions)
//                    mapView.getMapboxMap()
//                        .flyTo(CameraOptions.Builder().zoom(16.0).center(point).build())
//                }
//            }
//            NoOpUpdate
        },
        modifier = modifier
    )
}
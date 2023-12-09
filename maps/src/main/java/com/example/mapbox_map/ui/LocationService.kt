package com.example.mapbox_map.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class LocationService {

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): Location {

        if (!context.hasPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            throw throw LocationServiceException.MissingPermissionException()
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled) {
            throw LocationServiceException.LocationDisabledException()
        }
        if (!isNetworkEnabled) {
            throw LocationServiceException.NoNetworkEnabledException()
        }

        val locationProvider = LocationServices.getFusedLocationProviderClient(context)
        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()


        try {
            val location = locationProvider.getCurrentLocation(request, null).await()

            return location

        } catch (e: Exception) {
            throw LocationServiceException.UnknownException(e)
        }

    }

    fun Context.hasPermissions(vararg permissions: String) =
        permissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }


    sealed class LocationServiceException : Exception() {
        class MissingPermissionException : LocationServiceException()
        class LocationDisabledException : LocationServiceException()
        class NoNetworkEnabledException : LocationServiceException()
        class UnknownException(val exception: Exception) : LocationServiceException()
    }

}
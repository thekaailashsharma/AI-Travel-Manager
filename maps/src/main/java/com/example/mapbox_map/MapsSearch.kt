package com.example.mapbox_map

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.requestlocation.R
import com.mapbox.search.autocomplete.PlaceAutocomplete


suspend fun mapsSearch(context: Context, query: String): List<Address> {
    val placeAutocomplete = PlaceAutocomplete.create(
        accessToken = context.getString(R.string.mapbox_access_token),
    )
    val response = placeAutocomplete.suggestions(
        query = query,
    )
    val addresses = mutableListOf<Address>()
    if (response.isValue) {
        val suggestions = requireNotNull(response.value)
        Log.i("SearchApiExample", "Place Autocomplete suggestions: $suggestions")
        if (suggestions.isNotEmpty()) {
            suggestions.forEach { suggestion ->
                val selectionResponse = placeAutocomplete.select(suggestion)
                selectionResponse.onValue { result ->
                    val address = Address(
                        name = result.name,
                        latitude = result.coordinate.latitude(),
                        longitude = result.coordinate.longitude(),
                        houseNumber = result.address?.houseNumber,
                        street = result.address?.street,
                        neighborhood = result.address?.neighborhood,
                        locality = result.address?.locality,
                        postcode = result.address?.postcode,
                        place = result.address?.place,
                        district = result.address?.district,
                        region = result.address?.region,
                        country = result.address?.country,
                        formattedAddress = result.address?.formattedAddress,
                        countryIso1 = result.address?.countryIso1
                    )
                    addresses.add(address)
                }.onError { e ->
                    Log.i("SearchApiExample", "An error occurred during selection", e)

                }
            }

//            Log.i("SearchApiExample", "Selecting first suggestion...")
//            val selectionResponse = placeAutocomplete.select(selectedSuggestion)
//            selectionResponse.onValue { result ->
//
//                Log.i("SearchApiExample", "Place Autocomplete result: $result")
//            }.onError { e ->
//                Log.i("SearchApiExample", "An error occurred during selection", e)
//            }
        }
    } else {
        Log.i("SearchApiExample", "An error occurred during suggestions", response.error)
    }
    return addresses
}

data class Address(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val houseNumber: String? = null,
    val street: String? = null,
    val neighborhood: String? = null,
    val locality: String? = null,
    val postcode: String? = null,
    val place: String? = null,
    val district: String? = null,
    val region: String? = null,
    val country: String? = null,
    val formattedAddress: String? = null,
    val countryIso1: String? = null,
)



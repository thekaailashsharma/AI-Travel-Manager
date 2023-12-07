package ai.travel.app.newTrip

import ai.travel.app.database.DatabaseRepo
import ai.travel.app.repository.ApiService
import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewTripViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    val tripName = mutableStateOf("")
    val tripBudget = mutableStateOf("")
    val noOfDays = mutableStateOf("")
    val tags = mutableStateListOf<String>()
    val travelMode = mutableStateListOf<String>()
    val source = mutableStateOf("")
    val destination = mutableStateOf("")

}
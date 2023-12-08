package ai.travel.app.home

import ai.travel.app.database.ArrayListConverter
import ai.travel.app.database.DatabaseRepo
import ai.travel.app.database.travel.TripsEntity
import ai.travel.app.dto.ApiPrompt
import ai.travel.app.dto.PalmApi
import ai.travel.app.dto.Prompt
import ai.travel.app.dto.getPlaceId.PlaceIdBody
import ai.travel.app.repository.ApiService
import android.app.Application
import android.util.Base64
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val repository: ApiService,
    private val dbRepository: DatabaseRepo,
) : AndroidViewModel(application) {

    private val _imageState = MutableStateFlow<ApiState>(ApiState.NotStarted)
    val imageState: StateFlow<ApiState> = _imageState.asStateFlow()

    private val _message = MutableStateFlow("")
    private val _location = MutableStateFlow("")
    private val _budget = MutableStateFlow("")
    private val noOfDays = MutableStateFlow("")

    private val _data = MutableStateFlow(emptyList<Map<String, String>>())
    val data: StateFlow<List<Map<String, String>>> = _data.asStateFlow()

    private val _geoCodesData = MutableStateFlow(emptyList<TourDetails>().toMutableList())
    val geoCodesData: StateFlow<List<TourDetails>> = _geoCodesData.asStateFlow()

    fun getTrips(day: String, destination: String): Flow<List<TripsEntity?>> =
        dbRepository.getTrips(day, destination)

    fun getMoreInfo(destination: String): Flow<List<TripsEntity?>> =
        dbRepository.getMoreInfo(destination)

    val allTrips: Flow<List<TripsEntity?>> = dbRepository.allTrips
    fun getCurrentTrip(destination: String): Flow<List<TripsEntity?>> = dbRepository.getCurrentTrip(destination)
    fun uniqueDays(destination: String): Flow<List<String?>> = dbRepository.distinctDays(destination)

    val tripName = mutableStateOf(TextFieldValue(""))
    val tripBudget = mutableStateOf(TextFieldValue(""))
    val tripNoOfDays = mutableStateOf(TextFieldValue(""))
    val tags = mutableStateListOf<String>()
    val travelMode = mutableStateListOf<String>()
    val source = mutableStateOf(TextFieldValue(""))
    val destination = mutableStateOf(TextFieldValue(""))
    val isAnimationVisible = mutableStateOf(false)

    val currentDestination = mutableStateOf("")
    val currentNewDestination = mutableStateOf("")
    val currentDay = mutableStateOf("")
    val currentTimeOfDay = mutableStateOf("")

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()


    fun updateResult(result: String) {
        _result.value = result
    }



    fun getApiData() {
        viewModelScope.launch {
            delay(1000)
            val apiData =
                repository.getApiData(
                    ApiPrompt(
                        prompt = Prompt(
                            text = _message.value
                        )
                    )
                )
            _imageState.value = ApiState.Loaded(apiData)
            extractTourDetails(apiData.candidates?.get(0)?.output ?: "")
            getGeoCodes()
        }
    }

    private fun getGeoCodes() {
        viewModelScope.launch {
            delay(1000)
            _data.value.forEachIndexed { index, location ->
                val geoCodes = mutableMapOf<String, String>()
                val day = location.getOrDefault("Day", "-2")
                if (day != "-2") {
                    val locationName = location.getOrDefault("Name", "")
                    if (locationName != "") {
                        val apiData =
                            repository.getGeocodingData(
                                query = "$locationName, ${_location.value}",
                            )
                        geoCodes["latitude"] =
                            apiData.items?.get(0)?.position?.lat?.toString() ?: ""
                        geoCodes["longitude"] =
                            apiData.items?.get(0)?.position?.lng?.toString() ?: ""
                        _geoCodesData.value[index].geoCode = GeoCode(
                            latitude = geoCodes["latitude"] ?: "",
                            longitude = geoCodes["longitude"] ?: ""
                        )
                    }
                }

            }
            _imageState.value = ApiState.ReceivedGeoCodes
            getPlaceId()
        }
    }

    private fun getPlaceId() {
        viewModelScope.launch {
            delay(1000)
            _geoCodesData.value.forEachIndexed { index, location ->
                val apiData =
                    repository.getPlaceIdData(
                        PlaceIdBody(
                            textQuery = location.name
                        )
                    )
                _geoCodesData.value[index].placeId = apiData.places?.get(0)?.id ?: ""
                println("placeIddddd: ${_geoCodesData.value[index].placeId}")
            }
            _imageState.value = ApiState.ReceivedPlaceId
            getPhotoId()
        }

    }

    private fun getPhoto() {
        viewModelScope.launch {
            delay(1000)
            _geoCodesData.value.forEachIndexed { index, location ->
                val apiData =
                    repository.getPhoto(
                        photoReference = _geoCodesData.value[index].photoID ?: "",
                        maxWidth = 1200,
                    )
                _geoCodesData.value[index].photo = apiData
            }
            addTripToDatabase()
            _imageState.value = ApiState.ReceivedPhoto

        }

    }

    private fun addTripToDatabase() {
        viewModelScope.launch {
            println("Adding to databasesssssssssss")
            dbRepository.insertAllTrips(_geoCodesData.value.map {
                TripsEntity(
                    day = it.day,
                    timeOfDay = it.timeOfDay,
                    name = it.name,
                    budget = it.budget,
                    latitude = it.geoCode?.latitude?.toDouble(),
                    longitude = it.geoCode?.longitude?.toDouble(),
                    photoBase64 = byteArrayToBase64(it.photo ?: ByteArray(0)),
                    source = source.value.text,
                    destination = destination.value.text,
                    travelActivity = "",
                )
            })

//            _geoCodesData.value.forEachIndexed { _, location ->
//                dbRepository.insertTrip(
//                    TripsEntity(
//                        day = location.day,
//                        timeOfDay = location.timeOfDay,
//                        name = location.name,
//                        budget = location.budget,
//                        latitude = location.geoCode?.latitude?.toDouble(),
//                        longitude = location.geoCode?.longitude?.toDouble(),
//                        photoBase64 = byteArrayToBase64(location.photo ?: ByteArray(0)),
//                        source = source.value.text,
//                        destination = destination.value.text,
//                        travelActivity = "",
//                    )
//                )
//            }

        }

    }

    private fun byteArrayToBase64(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun getPhotoId() {
        viewModelScope.launch {
            delay(1000)
            _geoCodesData.value.forEachIndexed { index, location ->
                val apiData =
                    repository.getPhotoId(
                        photoId = _geoCodesData.value[index].placeId ?: ""
                    )
                _geoCodesData.value[index].photoID =
                    apiData.result?.photos?.get(0)?.photo_reference ?: ""
                println("photoIddddd: ${apiData.result}")
                println("photoIddddd 111: ${_geoCodesData.value[index].placeId}")
            }
            _imageState.value = ApiState.ReceivedPhotoId
            getPhoto()
        }

    }

    fun updateMessage(message: String, location: String, noOfDays: String) {
        _location.value = location
        this.noOfDays.value = noOfDays
        _message.value = message
        _imageState.value = ApiState.Loading
    }

    private fun extractTourDetails(output: String) {
        val tourDetails = mutableListOf<Map<String, String>>()

        val days = """Day (\d+) ([A-Za-z]+)""".toRegex(
            options = setOf(
                RegexOption.IGNORE_CASE
            )
        ).findAll(output)

        val names = """Name:(.{0,50})""".toRegex(
            options = setOf(
                RegexOption.IGNORE_CASE
            )
        ).findAll(output)

        val budgets = """Budget:(.{0,20})""".toRegex(
            options = setOf(
                RegexOption.IGNORE_CASE
            )
        ).findAll(output)

        println("Output: $output")

        println(
            "daysssss: ${
                days.forEachIndexed { index, matchResult ->
                    println("daysssss: $index, matchResult: ${matchResult.groupValues}")
                }
            }"
        )

        println(
            "names: ${
                names.forEachIndexed { index, matchResult ->
                    println("names: $index, matchResult: ${matchResult.groupValues}")
                }
            }"
        )

        println(
            "btssss: ${
                budgets.forEachIndexed { index, matchResult ->
                    println("btssss: $index, matchResult: ${matchResult.groupValues}")
                }
            }"
        )


        val namesList = names.map { it.groupValues[1] }.toList()
        val budgetsList = budgets.map { it.groupValues[1] }.toList()

        println("namesList: $namesList")
        println("budgetsList: $budgetsList")

        days.forEachIndexed { index, dayMatch ->
            val dayNumber = dayMatch.groupValues[1]
            val timeOfDay = dayMatch.groupValues[2]

            val dayInfo = mutableMapOf<String, String>()
            dayInfo["Day"] = dayNumber
            dayInfo["Time of Day"] = timeOfDay

            if (index < namesList.size) {
                dayInfo["Name"] = namesList[index]
            }

            if (index < budgetsList.size) {
                dayInfo["Budget"] = budgetsList[index]
            }

            tourDetails.add(dayInfo)
            _geoCodesData.value.add(
                TourDetails(
                    day = dayNumber,
                    timeOfDay = timeOfDay,
                    name = namesList[index],
                    budget = budgetsList[index]
                )
            )
        }

        _data.value = tourDetails

    }


}

sealed class ApiState {
    object Loading : ApiState()
    data class Loaded(val data: PalmApi) : ApiState()

    data class Error(val exception: Exception) : ApiState()

    object NotStarted : ApiState()
    object ReceivedGeoCodes : ApiState()
    object ReceivedPlaceId : ApiState()
    object ReceivedPhotoId : ApiState()

    object ReceivedPhoto : ApiState()
}

data class GeoCode(
    val latitude: String,
    val longitude: String,
)

data class TourDetails(
    val day: String,
    val timeOfDay: String,
    val name: String,
    val budget: String,
    var geoCode: GeoCode? = null,
    var placeId: String? = null,
    var photoID: String? = null,
    var photo: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TourDetails

        if (photo != null) {
            if (other.photo == null) return false
            if (!photo.contentEquals(other.photo)) return false
        } else if (other.photo != null) return false

        return true
    }

    override fun hashCode(): Int {
        return photo?.contentHashCode() ?: 0
    }
}

fun base64ToByteArray(base64String: String): ByteArray {
    return Base64.decode(base64String, Base64.DEFAULT)
}
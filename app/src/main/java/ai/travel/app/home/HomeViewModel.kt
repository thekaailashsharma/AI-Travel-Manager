package ai.travel.app.home

import ai.travel.app.database.ArrayListConverter
import ai.travel.app.database.DatabaseRepo
import ai.travel.app.database.travel.TripsEntity
import ai.travel.app.datastore.UserDatastore
import ai.travel.app.dto.ApiPrompt
import ai.travel.app.dto.PalmApi
import ai.travel.app.dto.Prompt
import ai.travel.app.dto.getPlaceId.PlaceIdBody
import ai.travel.app.repository.ApiService
import ai.travel.app.tripDetails.TimeSlot
import android.app.Application
import android.icu.util.Calendar
import android.util.Base64
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit
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

    private val _arrivalDate = MutableStateFlow("")
    private val _arrivalTime = MutableStateFlow("")
    private val _departureDate = MutableStateFlow("")
    private val _departureTime = MutableStateFlow("")

    private val _data = MutableStateFlow(emptyList<Map<String, String>>())
    val data: StateFlow<List<Map<String, String>>> = _data.asStateFlow()

    private val _currentTime = MutableStateFlow<TimeSlot?>(null)
    val currentTime: StateFlow<TimeSlot?> = _currentTime.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _geoCodesData = MutableStateFlow(emptyList<TourDetails>().toMutableList())
    val geoCodesData: StateFlow<List<TourDetails>> = _geoCodesData.asStateFlow()

    fun getTrips(day: String, destination: String): Flow<List<TripsEntity?>> =
        dbRepository.getTrips(day, destination)

    fun getDepartureDate(day: String, destination: String): Flow<List<String?>> =
        dbRepository.getDepartureDate(day, destination)

    fun getArrivalDate(day: String, destination: String): Flow<List<String?>> =
        dbRepository.getArrivalDate(day, destination)

    fun getMoreInfo(destination: String): Flow<List<TripsEntity?>> =
        dbRepository.getMoreInfo(destination)

    val allTrips: Flow<List<TripsEntity?>> = dbRepository.allTrips
    fun getCurrentTrip(destination: String): Flow<List<TripsEntity?>> =
        dbRepository.getCurrentTrip(destination)

    fun uniqueDays(destination: String): Flow<List<String?>> =
        dbRepository.distinctDays(destination)

    val tripName = mutableStateOf(TextFieldValue(""))
    val tripBudget = mutableStateOf(TextFieldValue(""))
    val tripNoOfDays = mutableStateOf(TextFieldValue(""))
    val likes = mutableStateOf(TextFieldValue(""))
    val disLikes = mutableStateOf(TextFieldValue(""))
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

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userPfp = MutableStateFlow("")
    val userPfp: StateFlow<String> = _userPfp.asStateFlow()

    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender.asStateFlow()

    private val _userPhoneNumber = MutableStateFlow("")
    val userPhoneNumber: StateFlow<String> = _userPhoneNumber.asStateFlow()
    fun totalBudget(destination: String): Flow<List<Double?>> =
        dbRepository.getTotalBudget(destination)

    private val _remainingBudget = MutableStateFlow<Double>(0.0)
    val remainingBudget: StateFlow<Double> = _remainingBudget.asStateFlow()

    private val _loginStatus = MutableStateFlow(false)
    val loginStatus: StateFlow<Boolean> = _loginStatus.asStateFlow()

    private val _isReorderLoading = MutableStateFlow(false)
    val isReorderLoading: StateFlow<Boolean> = _isReorderLoading.asStateFlow()

    init {
        viewModelScope.launch {
            val dataStore = UserDatastore(application.applicationContext)
            dataStore.getLoginStatus.collectLatest {
                _loginStatus.value = it
            }
        }
    }

    fun updateDates(
        arrivalDate: String,
        arrivalTime: String,
        departureDate: String,
        departureTime: String,
    ) {
        _arrivalDate.value = arrivalDate
        _arrivalTime.value = arrivalTime
        _departureDate.value = departureDate
        _departureTime.value = departureTime
    }


    fun calculateTimeSlotUpdates() {
        val currentTime = System.currentTimeMillis()
        val timeSlotFlow = MutableStateFlow(determineTimeSlot(currentTime))

        // Create a coroutine to update the time slot every 15 minutes
        val coroutine = determineTimeSlot(System.currentTimeMillis())
        println("coroutineeeee: $coroutine")
        _currentTime.value = coroutine
    }

    private fun determineTimeSlot(currentTime: Long): TimeSlot {
        val instant = Instant.ofEpochMilli(currentTime)
        val zoneOffset = ZoneOffset.UTC // Change this if you want to use a different time zone

        val localDateTime = LocalDateTime.ofInstant(instant, zoneOffset)
        println("localDateTimeeeee: $localDateTime")
        val hour = localDateTime.hour
        println("localDateTimeeeee 11: $hour")

        return when (getHourFromMillis(currentTime)) {
            in 9..11 -> TimeSlot.MORNING // 9 AM to 11 AM is morning
            in 12..16 -> TimeSlot.AFTERNOON // 12 PM to 4 PM is afternoon
            in 17..20 -> TimeSlot.EVENING // 5 PM to 8 PM is evening
            else -> TimeSlot.NIGHT // Other times are considered night
        }
    }

    private fun getHourFromMillis(systemTimeMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = systemTimeMillis
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun updateUserDetails(
        userName: String,
        gender: String,
        userPhoneNumber: String,
        loginStatus: Boolean,
        pfp: String,
    ) {
        _userName.value = userName
        _userPhoneNumber.value = userPhoneNumber
        _gender.value = gender
        _loginStatus.value = loginStatus
        _userPfp.value = pfp
    }


    fun updateResult(result: String) {
        _result.value = result
    }


    fun getApiData() {
        viewModelScope.launch {
            println("messageeeee: ${_message.value}")
            withContext(Dispatchers.IO) {
                val apiData =
                    repository.getApiData(
                        ApiPrompt(
                            prompt = Prompt(
                                text = _message.value
                            )
                        )
                    )
                _imageState.value = ApiState.Loaded(apiData)
                println("dataaaaaaaaa message: ${_message.value}")
                println("dataaaaaaaaa value: ${apiData.candidates?.get(0)?.output ?: ""}")
                extractTourDetails(apiData.candidates?.get(0)?.output ?: "")
                println("dataaaaaaaaa: ${_data.value}")
                _data.value.forEachIndexed { index, location ->
                    val geoCodes = mutableMapOf<String, String>()
                    val day = location.getOrDefault("Day", "-2")
                    val locationName = location.getOrDefault("Name", "")
//                    if (locationName != "") {
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
//                    }

                }
                _imageState.value = ApiState.ReceivedGeoCodes
                println("geoCodesDataaaaa: ${_geoCodesData.value}")
                _geoCodesData.value.forEachIndexed { index, tourDetails ->
                    if (index != 0) {
                        val apiData =
                            repository.getDistanceMatrix(
                                origins = _geoCodesData.value[index - 1].name,
                                destinations = _geoCodesData.value[index].name,
                            )
                        _geoCodesData.value[index].distance =
                            apiData.rows?.get(0)?.elements?.get(0)?.distance?.text ?: "0 m"
                        _geoCodesData.value[index].duration =
                            apiData.rows?.get(0)?.elements?.get(0)?.duration?.text ?: "0 hrs"
                    }
                }
                _imageState.value = ApiState.CalculatedDistance
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
                _geoCodesData.value.forEachIndexed { index, location ->
                    val apiData =
                        repository.getPhoto(
                            photoReference = _geoCodesData.value[index].photoID ?: "",
                            maxWidth = 1200,
                        )
                    _geoCodesData.value[index].photo = apiData
                }
                dbRepository.insertAllTrips(_geoCodesData.value.take(8).map {
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
                        distance = it.distance,
                        duration = it.duration,
                        totalBudget = tripBudget.value.text.toDoubleOrNull(),
                        departureDate = _departureDate.value,
                        arrivalDate = _arrivalDate.value,
                        departureTime = _departureTime.value,
                        arrivalTime = _arrivalTime.value,
                    )
                })
                _imageState.value = ApiState.ReceivedPhoto
                _geoCodesData.value = mutableListOf()
            }
        }
    }

    fun calculateProgress() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val totalAvailableTimeMinutes = TimeUnit.HOURS.toMinutes(12)
        val elapsedMinutes =
            ((currentHour - 9) * 60) + currentMinute

        _progress.value = (elapsedMinutes.toFloat() / totalAvailableTimeMinutes).coerceIn(0f, 1f)
    }

    private suspend fun getGeoCodes() {
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
    }

    private suspend fun getPlaceId() {
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
    }

    private suspend fun getPhoto() {
        _geoCodesData.value.forEachIndexed { index, location ->
            val apiData =
                repository.getPhoto(
                    photoReference = _geoCodesData.value[index].photoID ?: "",
                    maxWidth = 1200,
                )
            _geoCodesData.value[index].photo = apiData
        }
    }

    fun swapTripPositions(day: String, fromIndex: Int, toIndex: Int, destination: String) {
        viewModelScope.launch {
            dbRepository.swapTripPositions(day, fromIndex, toIndex, destination)
        }
    }

    fun updateTrips(
        name: String, budget: String?, latitude: Double?, longitude: Double?,
        photoBase64: String?, distance: String, duration: String, timeOfDay: String,
        fromId: Long, fromDay: String, fromDestination: String,
    ) {
        viewModelScope.launch {
            dbRepository.updateTrips(
                name,
                budget,
                latitude,
                longitude,
                photoBase64,
                distance,
                duration,
                timeOfDay,
                fromId,
                fromDay,
                fromDestination
            )
        }
    }

    fun updateTripsWithDistance(
        newTripsEntity: MutableList<TripsEntity?>,
        oldTrips: MutableList<TripsEntity?>, fromDay: String, fromDestination: String,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _isReorderLoading.value = true
                val tempList = mutableListOf<TripsEntity>()
                newTripsEntity.forEach {
                    if (it != null) {
                        tempList.add(it)
                    }
                }
                newTripsEntity.forEachIndexed { index, tourDetails ->
                    if (index != 0) {
                        println("fromDistancee = ${newTripsEntity[index - 1]?.name}, toooo = ${newTripsEntity[index]?.name}")
                        val apiData =
                            repository.getDistanceMatrix(
                                origins = "${newTripsEntity[index - 1]?.name}",
                                destinations = "${newTripsEntity[index]?.name}",
                            )
                        tempList[index].distance =
                            apiData.rows?.get(0)?.elements?.get(0)?.distance?.text ?: "0 m"
                        tempList[index].duration =
                            apiData.rows?.get(0)?.elements?.get(0)?.duration?.text ?: "0 hrs"

                        println(
                            "fromDistanceeApidata = ${apiData.rows?.get(0)?.elements?.get(0)?.distance?.text}, " +
                                    "toooo = ${apiData.rows?.get(0)?.elements?.get(0)?.duration?.text}"
                        )

                        println(
                            "fromDistanceeValue = ${newTripsEntity[index - 1]?.name}, " +
                                    "toooo = ${newTripsEntity[index]?.name}," +
                                    " distance = ${tempList[index].distance}, " +
                                    "duration = ${tempList[index].duration}"
                        )
                    }
                }
                newTripsEntity.forEachIndexed { index, it ->
                    dbRepository.updateTrips(
                        it?.name ?: "",
                        it?.budget ?: "",
                        it?.latitude,
                        it?.longitude,
                        it?.photoBase64,
                        tempList[index].distance ?: "",
                        tempList[index].duration ?: "",
                        oldTrips[index]?.timeOfDay ?: "",
                        oldTrips[index]?.id ?: 0,
                        fromDay,
                        fromDestination
                    )
                }
                _isReorderLoading.value = false
            }
        }
    }

    fun addTripToDatabase(tripsEntity: List<TripsEntity?>) {
        viewModelScope.launch {
            val tempList = mutableListOf<TripsEntity>()
            tripsEntity.forEach {
                if (it != null) {
                    tempList.add(it)
                }
            }
            dbRepository.insertAllTrips(tempList)
        }

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

    private fun byteArrayToBase64(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private suspend fun getPhotoId() {
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
    }

    fun updateMessage(message: String, location: String, noOfDays: String) {
        _location.value = location
        this.noOfDays.value = noOfDays
        _message.value = message
        _imageState.value = ApiState.Loading
    }

    fun extractBudgetValue(destination: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _remainingBudget.value = 0.0
                dbRepository.getBudget(destination).collectLatest { budgets ->
                    println("budgetssss: $budgets")
                    budgets.forEach {
                        val pattern = """\d+""".toRegex()
                        val matches = pattern.findAll(it ?: "")
                        val numbers = matches.map { it1 ->
                            it1.value.toIntOrNull()
                        }.toList()
                        numbers.forEach { value ->
                            _remainingBudget.value +=
                                value?.toDouble() ?: 0.0
                        }

                    }
                }
            }

        }
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
    object CalculatedDistance : ApiState()

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
    var distance: String = "0",
    var duration: String = "0",
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
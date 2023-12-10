package ai.travel.app.mapsSearch

import ai.travel.app.database.DatabaseRepo
import ai.travel.app.dto.getPhotoId.Geometry
import ai.travel.app.dto.getPhotoId.Location
import ai.travel.app.dto.getPhotoId.PhotoIdResponse
import ai.travel.app.dto.getPhotoId.Result
import ai.travel.app.dto.getPlaceId.PlaceIdBody
import ai.travel.app.dto.getPlaceId.PlaceIdResponse
import ai.travel.app.home.ApiState
import ai.travel.app.home.TourDetails
import ai.travel.app.repository.ApiService
import android.app.Application
import android.widget.SearchView
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapbox_map.Address
import com.example.mapbox_map.mapsSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.delayEach
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MapsSearchViewModel @Inject constructor(
    private val application: Application,
    private val repository: ApiService,
    private val dbRepository: DatabaseRepo,
) : AndroidViewModel(application) {

    private val _imageState = MutableStateFlow<ApiState>(ApiState.NotStarted)
    val imageState: StateFlow<ApiState> = _imageState.asStateFlow()

    private val _query = MutableStateFlow(TextFieldValue())
    val query: StateFlow<TextFieldValue> = _query.asStateFlow()

    private val _addresses = MutableStateFlow<List<Address>>(listOf())
    val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()

    private val _address = MutableStateFlow<Address?>(null)
    val address: StateFlow<Address?> = _address.asStateFlow()

    private val _placeId = MutableStateFlow("")
    val placeId: StateFlow<String> = _placeId.asStateFlow()

    private val _photoId = MutableStateFlow<MutableList<ByteArray?>>(mutableListOf())
    val photoId: StateFlow<List<ByteArray?>> = _photoId.asStateFlow()

    private val _searchResponse = MutableStateFlow<MapsSearchResponse?>(null)
    val searchResponse: StateFlow<MapsSearchResponse?> = _searchResponse.asStateFlow()

    private val _isChecking = MutableStateFlow<Boolean>(false)
    val isChecking: StateFlow<Boolean> = _isChecking.asStateFlow()

    var isClicked = mutableStateOf(false)
    var latitude =  mutableDoubleStateOf(20.5937)
    var longitude =mutableDoubleStateOf(78.9629)


    fun setImageState(state: ApiState) {
        _imageState.value = state
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun setQuery(query: TextFieldValue) {
        _query.value = query
        viewModelScope.launch {
            _query.debounce(2400)
                .filter { query ->
                    if (query.text.isEmpty() && !_isChecking.value) {
                        _query.value = TextFieldValue("")
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .filter {
                    return@filter _imageState.value !is ApiState.ReceivedPhoto
                }
                .onStart {
                    _isChecking.emit(true) // Set API request status to true
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query.text)
                        .catch {
                            emitAll(flowOf(""))
                        }
                        .onCompletion {
                            _isChecking.emit(false) // Set API request status to false on completion
                        }
                }
                .flowOn(Dispatchers.Default)
                .collect { result ->
                    if (result.isNotEmpty()) {
                        getAutoComplete(result)
                    }
                }


//                _query.debounce(800).collectLatest {
//                    if (_imageState.value !is ApiState.ReceivedPhoto) {
//                    getAutoComplete(it.text)
//                }
//            }
        }
    }

    fun searchPlace(index: Int) {
        try {
            _address.value = _addresses.value[index]
            latitude.value = _addresses.value[index].latitude
            longitude.value = _addresses.value[index].longitude
            _addresses.value = listOf()
            getApiData()
        } catch (e: Exception) {
            _query.value = TextFieldValue("")
            _addresses.value = listOf()
            e.printStackTrace()
        }
    }

    fun getAutoComplete(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val apiData =
                        repository.getGeocodingData(
                            query = query,
                        )
                    val latitude = apiData.items?.get(0)?.position?.lat ?: 0.0
                    val longitude = apiData.items?.get(0)?.position?.lng ?: 0.0
                    val autoComplete = repository.hereSearch(
                        latitude = latitude,
                        longitude = longitude,
                        query = query,
                    )
                    _addresses.value = autoComplete.items?.map {
                        Address(
                            name = it.title ?: "",
                            formattedAddress = it.address?.label ?: "",
                            latitude = latitude,
                            longitude = longitude,
                        )
                    } ?: listOf()
                    println("_addresses.value: ${_addresses.value}")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//                _addresses.value =  mapsSearch(application.applicationContext, query)
            }
        }
    }

    private fun dataFromNetwork(query: String): Flow<String> {
        return flow {
            delay(500)
            emit(query)
        }
    }

    private fun getApiData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                    val apiData =
                        repository.getPlaceIdData(
                            PlaceIdBody(
                                textQuery = address.value?.formattedAddress
                            )
                        )
                _imageState.value = ApiState.ReceivedPlaceId
                    var photoIdData =
                        repository.getPhotoId(
                            photoId = apiData.places?.get(0)?.id ?: ""
                        )
                _imageState.value = ApiState.ReceivedPhotoId
                try {
                    for (i in 1..3) {
                        val photoId =
                            repository.getPhoto(
                                photoReference = photoIdData.result?.photos?.get(i)?.photo_reference
                                    ?: "",
                                maxWidth = 600,
                            )
                        _photoId.value.add(photoId)
                    }
                    _searchResponse.value?.photos?.clear()
                    _searchResponse.value = MapsSearchResponse(
                        places = photoIdData.result?.copy(
                            formattedAddress = address.value?.formattedAddress ?: "",
                            geometry = Geometry(
                                location = Location(
                                    lat = address.value?.latitude ?: 0.0,
                                    lng = address.value?.longitude ?: 0.0
                                ),
                                viewport = null
                            ),
                            name = address.value?.name ?: "",
                        ),
                        photos = _photoId.value.toMutableList()
                    )
                    _imageState.value = ApiState.ReceivedPhoto
                    _query.value = TextFieldValue(searchResponse.value?.places?.name ?: "")
                    _addresses.value = listOf()
                    isClicked.value = true
                    _photoId.value.clear()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}

data class MapsSearchResponse(
    val places: Result?,
    val photos: MutableList<ByteArray?>,
)


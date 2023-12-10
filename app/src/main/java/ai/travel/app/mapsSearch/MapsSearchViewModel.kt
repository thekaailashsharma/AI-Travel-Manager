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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapbox_map.Address
import com.example.mapbox_map.mapsSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
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

    var isClicked = mutableStateOf(false)


    fun setImageState(state: ApiState) {
        _imageState.value = state
    }

    @OptIn(FlowPreview::class)
    fun setQuery(query: TextFieldValue) {
        _query.value = query
        viewModelScope.launch {
                _query.debounce(800).collectLatest {
                    if (_imageState.value !is ApiState.ReceivedPhoto) {
                    getAutoComplete(it.text)
                }
            }
        }
    }

    fun searchPlace(index: Int) {
        try {
            _address.value = _addresses.value[index]
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
                _addresses.value =  mapsSearch(application.applicationContext, query)
            }
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
                    val photoIdData =
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
                                maxWidth = 1200,
                            )
                        _photoId.value.add(photoId)
                    }
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
                        photos = photoId.value
                    )
                    _imageState.value = ApiState.ReceivedPhoto
                    _query.value = TextFieldValue(searchResponse.value?.places?.name ?: "")
                    _addresses.value = listOf()
                    isClicked.value = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

data class MapsSearchResponse(
    val places: Result?,
    val photos: List<ByteArray?>,
)


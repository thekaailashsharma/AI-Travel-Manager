package ai.travel.app.mapsSearch

import ai.travel.app.database.DatabaseRepo
import ai.travel.app.home.ApiState
import ai.travel.app.repository.ApiService
import android.app.Application
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

    private val _query = MutableStateFlow(TextFieldValue())
    val query: StateFlow<TextFieldValue> = _query.asStateFlow()

    private val _addresses = MutableStateFlow<List<Address>>(listOf())
    val address: StateFlow<List<Address>> = _addresses.asStateFlow()

    @OptIn(FlowPreview::class)
    fun setQuery(query: TextFieldValue) {
        _query.value = query
        viewModelScope.launch {
            _query.debounce(800).collectLatest {
                getAutoComplete(it.text)
            }
        }
    }

    fun getAutoComplete(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _addresses.value =  mapsSearch(application.applicationContext, query)
            }
        }
    }


}


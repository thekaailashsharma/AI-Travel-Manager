package ai.travel.app.home.ui

import ai.travel.app.dto.ApiPrompt
import ai.travel.app.dto.PalmApi
import ai.travel.app.dto.Prompt
import ai.travel.app.repository.ApiService
import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val repository: ApiService,
) : AndroidViewModel(application) {

    private val _imageState = MutableStateFlow<ApiState>(ApiState.NotStarted)
    val imageState: StateFlow<ApiState> = _imageState.asStateFlow()

    private val _message = MutableStateFlow("")

    private val _data = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val data: StateFlow<List<Map<String, String>>> = _data.asStateFlow()


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
        }
    }

    fun updateMessage(message: String) {
        _message.value = message
        _imageState.value = ApiState.Loading
    }

    private fun extractTourDetails(output: String){
        val tourDetails = mutableListOf<Map<String, String>>()

        val days = """Day (\d+) ([A-Za-z]+)""".toRegex(options = setOf(
            RegexOption.IGNORE_CASE
        )).findAll(output)

        val names = """Name:(.{0,50})""".toRegex(options = setOf(
            RegexOption.IGNORE_CASE
        )).findAll(output)

        val budgets = """Budget:(.{0,20})""".toRegex(options = setOf(
            RegexOption.IGNORE_CASE
        )).findAll(output)

        println("Output: $output")

        println("daysssss: ${days.forEachIndexed { index, matchResult -> 
            println("daysssss: $index, matchResult: ${matchResult.groupValues}")
        }}")

        println("names: ${names.forEachIndexed { index, matchResult ->
            println("names: $index, matchResult: ${matchResult.groupValues}")
        }}")

        println("btssss: ${budgets.forEachIndexed { index, matchResult ->
            println("btssss: $index, matchResult: ${matchResult.groupValues}")
        }}")


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
        }

        _data.value = tourDetails

    }


}

sealed class ApiState {
    object Loading : ApiState()
    data class Loaded(val data: PalmApi) : ApiState()

    data class Error(val exception: Exception) : ApiState()

    object NotStarted : ApiState()
}
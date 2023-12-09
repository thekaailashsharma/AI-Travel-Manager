package ai.travel.app.mapsSearch.ui

import ai.travel.app.mapsSearch.MapsSearchViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.mapbox_map.PreviewMap

@Composable
fun MapsSearchScreen(viewModel: MapsSearchViewModel) {
    val query = viewModel.query.collectAsState()
    val address = viewModel.address.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        PreviewMap(
            modifier = Modifier.fillMaxSize()
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
            MapsSearchBar(
                mutableText = query.value,
                onValueChange = {
                    viewModel.setQuery(it)
                },
                viewModel = viewModel,
                onTrailingClick = {
                    viewModel.setQuery(TextFieldValue(""))
                }
            )
        }
    }
}
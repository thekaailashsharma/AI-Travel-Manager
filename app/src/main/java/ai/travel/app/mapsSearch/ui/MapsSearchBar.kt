package ai.travel.app.mapsSearch.ui

import ai.travel.app.mapsSearch.MapsSearchViewModel
import ai.travel.app.newTrip.TextFieldWithIcons
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import ai.travel.app.utils.dashedBorder
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MapsSearchBar(
    mutableText: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onTrailingClick: () -> Unit = {},
    viewModel: MapsSearchViewModel,
) {
    var isChecking by remember { mutableStateOf(false) }
    var isCheckingJob: Job? = null // Initialize isCheckingJob
    val addresses = viewModel.address.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (addresses.value.isEmpty()) Color.Transparent else Color(0xFF333232).copy(
                    alpha = 0.5f
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextFieldWithIcons(
                textValue = "Search",
                placeholder = "Search for a place",
                icon = Icons.Filled.Search,
                mutableText = mutableText,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
                onValueChanged = {
                    onValueChange(it)
                    if (isCheckingJob?.isActive == true) {
                        isCheckingJob?.cancel()
                    }
                    isCheckingJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(1000) // Adjust the delay time as needed
                        isChecking = false
                    }
                    isChecking = true
                },
                onSearch = {
                    viewModel.getAutoComplete(mutableText.text)
                },
                contentColor = textColor,
                containerColor = Color.Transparent,
                trailingIcon = Icons.Filled.Close,
                isTrailingVisible = true,
                onTrailingClick = {
                    onTrailingClick()
                },
                modifier = Modifier,
            )
        }
        if (isChecking && addresses.value.isEmpty()) {
            LinearProgressIndicator(
                color = lightText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
        }
        AnimatedVisibility(
            visible = addresses.value.isNotEmpty(),
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(contentPadding = PaddingValues(5.dp)) {
                items(addresses.value) { address ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = address.formattedAddress ?: "",
                                color = textColor,
                                fontSize = 15.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Icon(
                                imageVector = Icons.Filled.NorthEast,
                                contentDescription = "",
                                tint = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}
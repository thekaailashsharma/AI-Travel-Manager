package ai.travel.app.mapsSearch.ui

import ai.travel.app.home.ApiState
import ai.travel.app.mapsSearch.MapsSearchViewModel
import ai.travel.app.newTrip.TextFieldWithIcons
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
    navController: NavController,
) {
    val isChecking by viewModel.isChecking.collectAsState()
    var isCheckingJob: Job? = null // Initialize isCheckingJob
    val addresses = viewModel.addresses.collectAsState()
    val imageState = viewModel.imageState.collectAsState()
    val searchResponse = viewModel.searchResponse.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (addresses.value.isEmpty()) Color.Transparent else Color(0xFF333232).copy(
                    alpha = 0.5f
                )
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    navController.popBackStack()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = "topText",
                tint = textColor,
                modifier = Modifier.size(30.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
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
                    viewModel.setImageState(
                        ApiState.NotStarted
                    )
                    onValueChange(it)
                    if (isCheckingJob?.isActive == true) {
                        isCheckingJob?.cancel()
                    }
                    isCheckingJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(1000) // Adjust the delay time as needed
                    }
                },
                onSearch = {
                    viewModel.getAutoComplete(mutableText.text)
                },
                contentColor = textColor,
                containerColor = Color.Black.copy(0.8f),
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
                itemsIndexed(addresses.value) { index, address ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                viewModel.searchPlace(index)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = address.formattedAddress ?: "",
                                color = textColor,
                                fontSize = 15.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )
                            Icon(
                                imageVector = Icons.Filled.NorthEast,
                                contentDescription = "",
                                tint = lightText
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = imageState.value is ApiState.ReceivedPhoto,
            enter = slideInVertically(initialOffsetY = {
                it
            }),
            exit = slideOutVertically(targetOffsetY = {
                it
            })
        ) {
            searchResponse.value?.let { response ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .offset(y = (-10).dp),
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                    elevation = CardDefaults.cardElevation(7.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(0.8f),
                    ),
                    border = BorderStroke(
                        width = 0.5.dp,
                        color = textColor
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddReaction,
                                contentDescription = "topText",
                                tint = lightText,
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = "Generate Trip",
                                color = textColor,
                                fontSize = 15.sp,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PhotoLibrary,
                                contentDescription = "topText",
                                tint = lightText,
                                modifier = Modifier
                                    .size(30.dp)
                                    .rotate(45f)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = "Add Collection",
                                color = textColor,
                                fontSize = 15.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}
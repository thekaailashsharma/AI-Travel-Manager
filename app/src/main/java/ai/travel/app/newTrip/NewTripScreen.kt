package ai.travel.app.newTrip

import ai.travel.app.home.HomeViewModel
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.bottomBarBackground
import ai.travel.app.ui.theme.bottomBarBorder
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.launch

data class TravelModes(
    val mode: String,
    val icon: ImageVector,
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewTripScreen(
    viewModel: NewTripViewModel,
    sheetScaffoldState: BottomSheetScaffoldState,
    homeViewModel: HomeViewModel
) {

    val travelModes = listOf(
        TravelModes("Flight", Icons.Filled.Flight),
        TravelModes("Train", Icons.Filled.Train),
        TravelModes("Bus", Icons.Filled.DirectionsBus),
        TravelModes("Car", Icons.Filled.ElectricCar),
        TravelModes("Bike", Icons.Filled.ElectricBike),
        TravelModes("Walk", Icons.Filled.DirectionsWalk),
    )
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(CardBackground.copy(0.5f))
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Arrow Back",
                tint = textColor,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .size(25.dp)
                    .clickable {
                        coroutineScope.launch {
                            sheetScaffoldState.bottomSheetState.hide()
                        }
                    }
            )

            Text(
                text = "Create New Trip",
                color = textColor,
                fontSize = 20.sp,
            )

            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = "Arrow Done",
                tint = textColor,
                modifier = Modifier
                    .padding(end = 15.dp)
                    .size(25.dp)
                    .clickable {
                        if (homeViewModel.tripName.value.text.isNotEmpty() && homeViewModel.source.value.text.isNotEmpty()
                            && homeViewModel.destination.value.text.isNotEmpty()
                            && homeViewModel.tripBudget.value.text.isNotEmpty()
                            && homeViewModel.tripNoOfDays.value.text.isNotEmpty() && homeViewModel.travelMode.isNotEmpty()
                            && homeViewModel.tripBudget.value.text.isDigitsOnly()) {
                            coroutineScope.launch {
                                sheetScaffoldState.bottomSheetState.hide()
                            }
                            homeViewModel.updateMessage(
                                "Generate a guided tour plan for a trip to " +
                                        "[LOCATION] for [NUMBER_OF_DAYS] days, considering various factors " +
                                        "such as [BUDGET_RANGE], preferred activities, accommodations, " +
                                        "transportation, and any specific preferences.1. Destination: " +
                                        "[${homeViewModel.destination.value.text}] 2. Duration: [${homeViewModel.tripNoOfDays.value.text}] days 3. Budget: [${homeViewModel.tripBudget.value.text}] 4. " +
                                        "Activities: [TEMPLES, Sea] 5. Accommodations: [AC] 6. " +
                                        "Transportation: [Bus, Car] 7. Special Preferences: [None]." +
                                        "Provide a comprehensive guided tour plan that includes " +
                                        "recommended activities, places to visit, estimated costs, " +
                                        "suitable accommodations, transportation options, and any other " +
                                        "relevant information. Please consider the specified factors to " +
                                        "tailor the plan accordingly. GIVE OUTPUT IN THE FORMAT I ASKED ONLY. " +
                                        "DO NOT CHANGE THE output FORMAT. DO NOT. DO NOT change the FORMAT. " +
                                        "Format is Day1 Morning: 1. Location (Latitude, Longitude) 2. Name:" +
                                        " Name of Location 3. Budget, 4. Some additional notes. " +
                                        "Same for afternoon & evening. Generate same output for all days",
                                location = homeViewModel.destination.value.text,
                                noOfDays = homeViewModel.tripNoOfDays.value.text
                            )
                            homeViewModel.getApiData()
                            homeViewModel.isAnimationVisible.value = true
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        TextFieldWithIcons(
            textValue = "Trip Name",
            placeholder = "Enter Trip Name",
            icon = Icons.Filled.TripOrigin,
            mutableText = homeViewModel.tripName.value,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            isTrailingVisible = false,
            trailingIcon = null,
            onTrailingClick = {},
            ifIsOtp = false,
            isEnabled = true,
            onValueChanged = { value ->
                homeViewModel.tripName.value = value
            }
        )

        TextFieldWithIcons(
            textValue = "Source",
            placeholder = "Enter Source",
            icon = Icons.Filled.FlightTakeoff,
            mutableText = homeViewModel.source.value,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            isTrailingVisible = false,
            trailingIcon = null,
            onTrailingClick = {},
            ifIsOtp = false,
            isEnabled = true,
            onValueChanged = { value ->
                homeViewModel.source.value = value
            }
        )

        TextFieldWithIcons(
            textValue = "Destination",
            placeholder = "Enter Destination",
            icon = Icons.Filled.FlightLand,
            mutableText = homeViewModel.destination.value,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            isTrailingVisible = false,
            trailingIcon = null,
            onTrailingClick = {},
            ifIsOtp = false,
            isEnabled = true,
            onValueChanged = { value ->
                homeViewModel.destination.value = value
            }
        )

        TextFieldWithIcons(
            textValue = "Budget",
            placeholder = "Enter Budget",
            icon = Icons.Filled.Wallet,
            mutableText = homeViewModel.tripBudget.value,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next,
            isTrailingVisible = false,
            trailingIcon = null,
            onTrailingClick = {},
            ifIsOtp = false,
            isEnabled = true,
            onValueChanged = { value ->
                homeViewModel.tripBudget.value = value
            }
        )

        TextFieldWithIcons(
            textValue = "No of Days",
            placeholder = "Enter No of Days",
            icon = Icons.Filled.CalendarToday,
            mutableText = homeViewModel.tripNoOfDays.value,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
            isTrailingVisible = false,
            trailingIcon = null,
            onTrailingClick = {},
            ifIsOtp = false,
            isEnabled = true,
            onValueChanged = { value ->
                homeViewModel.tripNoOfDays.value = value
            }
        )

        Text(
            text = "Select Mode of Travel",
            color = textColor,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 15.dp, top = 15.dp, bottom = 15.dp, end = 15.dp)
        )

        FlowRow(modifier = Modifier.fillMaxWidth()) {
            travelModes.forEach { mode ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (homeViewModel.travelMode.contains(mode.mode)) {
                            lightText
                        } else {
                            bottomBarBackground
                        }
                    ),
                    border = BorderStroke(1.dp, bottomBarBorder),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    modifier = Modifier
                        .width(120.dp)
                        .padding(
                            start = 12.dp,
                            top = 0.dp,
                            bottom = 12.dp,
                            end = 12.dp
                        )
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            if (homeViewModel.travelMode.contains(mode.mode)) {
                                homeViewModel.travelMode.remove(mode.mode)
                            } else {
                                homeViewModel.travelMode.add(mode.mode)
                            }
                        }

                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 0.dp,
                                top = 10.dp,
                                bottom = 10.dp
                            )
                    ) {
                        Text(
                            text = mode.mode,
                            color = textColor,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = mode.icon,
                            contentDescription = "Icon",
                            tint = textColor,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .size(20.dp)
                        )
                    }
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithIcons(
    modifier: Modifier = Modifier,
    textValue: String,
    placeholder: String,
    icon: ImageVector,
    mutableText: TextFieldValue,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    isTrailingVisible: Boolean = false,
    trailingIcon: ImageVector? = null,
    onTrailingClick: () -> Unit = {},
    ifIsOtp: Boolean = false,
    isEnabled: Boolean = true,
    onValueChanged: (TextFieldValue) -> Unit,
    onSearch: () -> Unit = {},
    contentColor : Color = textColor,
    containerColor : Color = CardBackground
    ) {
    TextField(
        value = mutableText,
        leadingIcon = {
            Icon(
                imageVector = icon,
                tint = textColor,
                contentDescription = "Icon"
            )
        },
        trailingIcon = {
            if (isTrailingVisible && trailingIcon != null) {
                if (!ifIsOtp) {
                    IconButton(onClick = {
                        onTrailingClick()
                    }) {
                        Icon(
                            imageVector = trailingIcon,
                            tint = textColor,
                            contentDescription = "Icon"
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            onTrailingClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardBackground,
                            contentColor = textColor
                        ),
                        shape = RoundedCornerShape(35.dp),
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Text(
                            text = "Get OTP",
                            color = textColor,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 4.dp),
                            maxLines = 1,
                            softWrap = true
                        )
                    }
                }
            }
        },
        onValueChange = onValueChanged,
        label = { Text(text = textValue, color = textColor) },
        placeholder = { Text(text = placeholder, color = textColor) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        ),
        modifier = modifier
            .padding(start = 15.dp, top = 5.dp, bottom = 15.dp, end = 15.dp)
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedTextColor = contentColor,
            disabledTextColor = contentColor,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
        ),
        enabled = isEnabled,
        shape = RoundedCornerShape(20.dp),
    )
}

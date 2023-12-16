package ai.travel.app.newTrip

import ai.travel.app.home.HomeViewModel
import ai.travel.app.navigation.Screens
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.borderBrush
import ai.travel.app.ui.theme.bottomBarBackground
import ai.travel.app.ui.theme.bottomBarBorder
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class TravelModes(
    val mode: String,
    val icon: ImageVector,
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewTripScreen(
    viewModel: NewTripViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
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

    var departPickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var isDepartDateSelected by remember {
        mutableStateOf(false)
    }

    var isDepartTimeSelected by remember {
        mutableStateOf(false)
    }

    var isArrivalDateSelected by remember {
        mutableStateOf(false)
    }

    var isArrivalTimeSelected by remember {
        mutableStateOf(false)
    }

    var departPickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    val departFormattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(departPickedDate)
        }
    }
    val departFormattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm")
                .format(departPickedTime)
        }
    }
    val context = LocalContext.current

    val departDateDialogState = rememberMaterialDialogState()
    val departTimeDialogState = rememberMaterialDialogState()

    var arrivalPickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var arrivalPickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    val arrivalFormattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(arrivalPickedDate)
        }
    }
    val arrivalFormattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm")
                .format(arrivalPickedTime)
        }
    }

    val arrivalDateDialogState = rememberMaterialDialogState()
    val arrivalTimeDialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier
            .background(CardBackground)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = "Arrow Back",
                tint = textColor,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .size(25.dp)
                    .clickable {
                        coroutineScope.launch {
                            navController.navigate(Screens.Home.route)
                        }
                    }
            )

            Spacer(modifier = Modifier.width(60.dp))

            Text(
                text = "Create New Trip",
                color = textColor,
                fontSize = 20.sp,
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
            textValue = "What you want to visit?",
            placeholder = "Eg : Temples, Sea, Historical Places etc",
            icon = Icons.Filled.CalendarToday,
            mutableText = homeViewModel.likes.value,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            isTrailingVisible = false,
            trailingIcon = null,
            onTrailingClick = {},
            ifIsOtp = false,
            isEnabled = true,
            onValueChanged = { value ->
                homeViewModel.likes.value = value
            }
        )

        TextFieldWithIcons(
            textValue = "What you don't want to visit?",
            placeholder = "Eg : Temples, Sea, Historical Places etc",
            icon = Icons.Filled.CalendarToday,
            mutableText = homeViewModel.disLikes.value,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            isTrailingVisible = false,
            trailingIcon = null,
            onTrailingClick = {},
            ifIsOtp = false,
            isEnabled = true,
            onValueChanged = { value ->
                homeViewModel.disLikes.value = value
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

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Select Departure Date and Time",
            color = textColor,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 15.dp, top = 15.dp, bottom = 15.dp, end = 15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp)
        ) {
            Button(
                onClick = {
                    departDateDialogState.show()
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                border = BorderStroke(1.dp, brush = borderBrush),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (!isDepartDateSelected) {
                        "Pick date"
                    } else {
                        departFormattedDate
                    },
                    color = textColor
                )
            }
            MaterialDialog(
                dialogState = departDateDialogState,
                buttons = {
                    positiveButton(text = "Ok") {
                        isDepartDateSelected = true
                        Toast.makeText(
                            context,
                            "Departure Date selected",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    negativeButton(text = "Cancel") {
                        isDepartDateSelected = false
                    }
                }
            ) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "Pick a date",
                ) {
                    departPickedDate = it
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    departTimeDialogState.show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                border = BorderStroke(1.dp, brush = borderBrush),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (!isDepartTimeSelected) {
                        "Pick time"
                    } else {
                        departFormattedTime
                    },
                    color = textColor
                )
            }
            MaterialDialog(
                dialogState = departTimeDialogState,
                buttons = {
                    positiveButton(text = "Ok") {
                        isDepartTimeSelected = true
                        Toast.makeText(
                            context,
                            "Depart Time selected",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    negativeButton(text = "Cancel") {
                        isDepartTimeSelected = false
                    }
                }
            ) {
                timepicker(
                    initialTime = LocalTime.NOON,
                    title = "Pick a time",
                ) {
                    departPickedTime = it
                }
            }
        }


        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Select Arrival Date and Time",
            color = textColor,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 15.dp, top = 15.dp, bottom = 15.dp, end = 15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp)
        ) {
            Button(
                onClick = {
                    arrivalDateDialogState.show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                border = BorderStroke(1.dp, brush = borderBrush),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (!isArrivalDateSelected) {
                        "Pick date"
                    } else {
                        arrivalFormattedDate
                    },
                    color = textColor
                )
            }
            MaterialDialog(
                dialogState = arrivalDateDialogState,
                buttons = {
                    positiveButton(text = "Ok") {
                        isArrivalDateSelected = true
                        Toast.makeText(
                            context,
                            "Departure Date selected",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    negativeButton(text = "Cancel") {
                        isArrivalDateSelected = false
                    }
                }
            ) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "Pick a date",
                ) {
                    arrivalPickedDate = it
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    arrivalTimeDialogState.show()
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                border = BorderStroke(1.dp, brush = borderBrush),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (!isArrivalTimeSelected) {
                        "Pick time"
                    } else {
                        arrivalFormattedTime
                    },
                    color = textColor
                )
            }
            MaterialDialog(
                dialogState = arrivalTimeDialogState,
                buttons = {
                    positiveButton(text = "Ok") {
                        isArrivalTimeSelected = true
                        Toast.makeText(
                            context,
                            "Depart Time selected",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    negativeButton(text = "Cancel") {
                        isArrivalTimeSelected = false
                    }
                }
            ) {
                timepicker(
                    initialTime = LocalTime.NOON,
                    title = "Pick a time",
                ) {
                    arrivalPickedTime = it
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {

                    if (homeViewModel.tripName.value.text.isNotEmpty() && homeViewModel.source.value.text.isNotEmpty()
                        && homeViewModel.destination.value.text.isNotEmpty()
                        && homeViewModel.tripBudget.value.text.isNotEmpty()
                        && homeViewModel.tripNoOfDays.value.text.isNotEmpty() && homeViewModel.travelMode.isNotEmpty()
                        && homeViewModel.tripBudget.value.text.isDigitsOnly()
                    ) {
                        homeViewModel.updateMessage(
                            "Generate a guided tour plan for a trip to " +
                                    "[LOCATION] for [NUMBER_OF_DAYS] days, considering various factors " +
                                    "such as [BUDGET_RANGE], preferred activities, accommodations, " +
                                    "transportation, and any specific preferences.1. Destination: " +
                                    "[${homeViewModel.destination.value.text}] " +
                                    "2. Duration: [${homeViewModel.tripNoOfDays.value.text}] days " +
                                    "3. Budget: [${homeViewModel.tripBudget.value.text}] " +
                                    "4. Activities that should be include: ${homeViewModel.likes.value.text} " +
                                    "and Activities that should not be include: ${homeViewModel.disLikes.value.text}" +
                                    "Please follow activities strictly. Only add those that should be included " +
                                    "and do not add those that should not be included. Follow the order strictly." +
                                    "5. Accommodations: [AC] 6. Transportation: [Bus, Car] " +
                                    "7. Special Preferences: [None]." +
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
                        homeViewModel.updateDates(
                            departureDate = departFormattedDate,
                            arrivalDate = arrivalFormattedDate,
                            departureTime = departFormattedTime,
                            arrivalTime = arrivalFormattedTime
                        )
                        homeViewModel.getApiData()
                        homeViewModel.isAnimationVisible.value = true
                        navController.navigate(Screens.Home.route)
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = CardBackground
                ),
                border = BorderStroke(1.dp, brush = borderBrush),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Submit",
                    color = textColor
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

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
    contentColor: Color = textColor,
    containerColor: Color = CardBackground,
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

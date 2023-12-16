package ai.travel.app.riveanimation

import ai.travel.app.R
import ai.travel.app.datastore.UserDatastore
import ai.travel.app.firestore.updateInfoToFirebase
import ai.travel.app.home.HomeViewModel
import ai.travel.app.navigation.Screens
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.monteSB
import ai.travel.app.ui.theme.textColor
import ai.travel.app.utils.ProfileImage
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginUI(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel,
    navController: NavController,
) {
    var password by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var phoneNumber by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var name by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var gender by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var passwordVisible = rememberSaveable { mutableStateOf(false) }
    var isChecking by remember { mutableStateOf(false) }
    var isCheckingJob: Job? = null // Initialize isCheckingJob
    var passVisibleJob: Job? = null // Initialize isCheckingJob
    var trigSuccess by remember { mutableStateOf(false) }
    var trigFail by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val currentActivity = context as? Activity

    val dataStore = UserDatastore(context = context)
    val coroutineScope = rememberCoroutineScope()

    var isOtpSent by remember {
        mutableStateOf(false)
    }

    var isVerified by remember {
        mutableStateOf(false)
    }

    var selectedAvatar by remember {
        mutableStateOf(Avatars(""))
    }

    var login by remember {
        mutableStateOf(false)
    }

    var vfId by remember {
        mutableStateOf(TextFieldValue(""))
    }

    LaunchedEffect(key1 = viewModel.result) {
        isOtpSent = false
        viewModel.result.collectLatest {
            if (it != "") {
                password = TextFieldValue(it)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
            .padding(paddingValues = paddingValues),
    ) {
        AnimatedVisibility(
            visible = !login,
            enter = slideInHorizontally(initialOffsetX = {
                it
            }),
            exit = slideOutHorizontally(
                targetOffsetX = {
                    it
                }
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .background(appGradient),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize(0.8f)
                        .align(Alignment.Center)
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        RiveAnimation(
                            animation = R.raw.new_login,
                            modifier = Modifier
                                .size(400.dp)
                        ) { view ->
                            view.setBooleanState(
                                "State Machine 1",
                                "hands_up",
                                passwordVisible.value
                            )
                            view.setBooleanState(
                                "State Machine 1",
                                "Check",
                                isChecking
                            )
                            if (trigFail)
                                view.fireState("State Machine 1", "fail")
                            if (trigSuccess)
                                view.fireState("State Machine 1", "success")
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize(0.5f)
                        .padding(start = 40.dp, bottom = 20.dp, end = 40.dp)
                        .align(Alignment.BottomCenter),
                    colors = CardDefaults.cardColors(CardBackground),
                    shape = RoundedCornerShape(15.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    AnimatedVisibility(
                        visible = !isVerified,
                        enter = slideInHorizontally(initialOffsetX = {
                            it
                        }, animationSpec = tween(500, 100)),
                        exit = slideOutHorizontally(
                            targetOffsetX = {
                                it
                            }, animationSpec = tween(500, 100)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 35.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFF4885ED),
                                        fontFamily = monteSB,
                                    )
                                ) {
                                    append("Welcome")
                                }
                                append(" ")
                                withStyle(
                                    SpanStyle(
                                        color = Color(0xFFF4C20D).copy(0.89f),
                                        fontFamily = monteSB
                                    )
                                ) {
                                    append("to")
                                }
                            }, fontSize = 25.sp)
                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFF1DE9B6),
                                        fontFamily = monteSB,
                                    )
                                ) {
                                    append("Tripify")
                                }
                                append(" ")
                            }, fontSize = 25.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            TextFieldWithIconsLogin(
                                textValue = "Phone Number",
                                placeholder = "Enter Your Phone Number",
                                icon = Icons.Filled.Email,
                                mutableText = phoneNumber,
                                onValueChanged = {
                                    phoneNumber = it
                                    if (passVisibleJob?.isActive == true) {
                                        passVisibleJob?.cancel()
                                    }
                                    passVisibleJob = CoroutineScope(Dispatchers.Main).launch {
                                        delay(1000) // Adjust the delay time as needed
                                        passwordVisible.value = false
                                    }
                                    passwordVisible.value = true
                                },
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next,
                                enabled = !isOtpSent
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedVisibility(
                                visible = isOtpSent,
                                enter = slideInHorizontally(),
                                exit = slideOutHorizontally()
                            ) {
                                TextFieldWithIconsLogin(
                                    textValue = "OTP",
                                    placeholder = "Enter OTP Sent to your Phone Number",
                                    icon = Icons.Filled.Lock,
                                    mutableText = password,
                                    onValueChanged = {
                                        password = it
                                        if (isCheckingJob?.isActive == true) {
                                            isCheckingJob?.cancel()
                                        }
                                        isCheckingJob = CoroutineScope(Dispatchers.Main).launch {
                                            delay(1000) // Adjust the delay time as needed
                                            isChecking = false
                                        }
                                        isChecking = true
                                    },
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next,
                                    passwordVisible = passwordVisible
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    if (!isOtpSent) {
                                        currentActivity?.let {
                                            val callbacks =
                                                object :
                                                    PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                                        // Verification successful, automatically sign in the user
                                                        signInWithPhoneAuthCredential(
                                                            credential,
                                                            auth
                                                        )
                                                    }

                                                    override fun onVerificationFailed(exception: FirebaseException) {
                                                        // Verification failed, show error message to the user
                                                        Toast.makeText(
                                                            context,
                                                            "exception: ${exception.message}",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }

                                                    override fun onCodeSent(
                                                        verificationId: String,
                                                        token: PhoneAuthProvider.ForceResendingToken,
                                                    ) {
                                                        vfId = vfId.copy(verificationId)
                                                    }
                                                }


                                            val options = PhoneAuthOptions.newBuilder(auth)
                                                .setPhoneNumber("+91${phoneNumber.text}") // Phone number to verify
                                                .setTimeout(
                                                    0L,
                                                    java.util.concurrent.TimeUnit.SECONDS
                                                ) // Timeout and unit
                                                .setCallbacks(callbacks)
                                                .setActivity(currentActivity)// OnVerificationStateChangedCallbacks
                                                .build()
                                            PhoneAuthProvider.verifyPhoneNumber(options)
                                        }
                                        isOtpSent = true
                                        Toast.makeText(context, "Please Wait..", Toast.LENGTH_LONG)
                                            .show()
                                    } else {
                                        try {
                                            val credential =
                                                PhoneAuthProvider.getCredential(
                                                    vfId.text,
                                                    password.text
                                                )
                                            FirebaseAuth.getInstance()
                                                .signInWithCredential(credential)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        // Sign in success
                                                        val users = task.result?.user
                                                        trigSuccess = true
                                                        trigFail = false
                                                        isVerified = true
                                                        println("Success")
                                                        Toast.makeText(
                                                            context,
                                                            "Success",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                            .show()
                                                        // Do something with user
                                                    } else {
                                                        trigSuccess = false
                                                        trigFail = true
                                                        isVerified = false
                                                        // Sign in failed
                                                        val message = task.exception?.message
                                                        // Handle error
                                                    }
                                                }
                                        } catch (e: Exception) {
                                            println("Exception ${e.message}")
                                        }

//                                if (password.text == "123456" && phoneNumber.text == "abc@gmail.com") {
//                                    trigSuccess = true
//                                    trigFail = false
//                                } else {
//                                    trigSuccess = false
//                                    trigFail = true
//                                }
                                    }


                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = lightText,
                                    contentColor = textColor
                                )
                            ) {
                                Text(
                                    text = if (!isOtpSent) "Verify OTP" else "Login",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                )
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }
                    AnimatedVisibility(
                        visible = isVerified,
                        enter = slideInHorizontally(initialOffsetX = {
                            -it
                        }),
                        exit = slideOutHorizontally(targetOffsetX = {
                            -it
                        })
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 35.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFF4885ED),
                                        fontFamily = monteSB,
                                    )
                                ) {
                                    append("Some")
                                }
                                append(" ")
                                withStyle(
                                    SpanStyle(
                                        color = Color(0xFFF4C20D).copy(0.89f),
                                        fontFamily = monteSB
                                    )
                                ) {
                                    append("Basic")
                                }
                            }, fontSize = 25.sp)
                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color(0xFF1DE9B6),
                                        fontFamily = monteSB,
                                    )
                                ) {
                                    append("Details")
                                }
                                append(" ")
                            }, fontSize = 25.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            TextFieldWithIconsLogin(
                                textValue = "NickName",
                                placeholder = "Enter Your Nickname",
                                icon = Icons.Filled.Person,
                                mutableText = name,
                                onValueChanged = {
                                    name = it
                                },
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next,
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            TextFieldWithIconsLogin(
                                textValue = "Gender",
                                placeholder = "Enter Your Gender",
                                icon = Icons.Filled.Male,
                                mutableText = gender,
                                onValueChanged = {
                                    gender = it
                                },
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    login = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = lightText,
                                    contentColor = textColor
                                )
                            ) {
                                Text(
                                    text = "Choose Avatar",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                )
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                        }


                    }

                }
            }
        }

        AnimatedVisibility(
            visible = login,
            enter = slideInHorizontally(initialOffsetX = {
                it
            }),
            exit = slideOutHorizontally(
                targetOffsetX = {
                    it
                }
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .background(appGradient)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            )
            {
                LazyVerticalGrid(columns = GridCells.Adaptive(dpFromPx(context, 170f).dp)) {
                    items(avatarsList) { avatar ->
                        Card(
                            modifier = Modifier
                                .padding(5.dp)
                                .height(dpFromPx(context, 220f).dp)
                                .width(dpFromPx(context, 220f).dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) {
                                    selectedAvatar = avatar
                                }
                                .then(
                                    if (selectedAvatar == avatar) Modifier
                                        .border(
                                            color = lightText,
                                            shape = CircleShape,
                                            width = 1.dp
                                        )
                                    else Modifier
                                ),
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(0.dp),
                            colors = CardDefaults.cardColors(
                                if (selectedAvatar == avatar) lightText else CardBackground
                            ),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(appGradient),
                                contentAlignment = Alignment.Center
                            ) {
                                ProfileImage(
                                    imageUrl = avatar.imageUrl,
                                    modifier = Modifier
                                        .size(110.dp)
                                        .clip(CircleShape),
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    if (selectedAvatar.imageUrl != "") {
                                        dataStore.saveGender(gender.text)
                                        dataStore.saveName(name.text)
                                        dataStore.saveNumber(phoneNumber.text)
                                        dataStore.savePfp(selectedAvatar.imageUrl)
                                        dataStore.saveLoginStatus(true)
                                        updateInfoToFirebase(
                                            name = name.text,
                                            phoneNumber = phoneNumber.text,
                                            gender = gender.text,
                                            context = context,
                                            imageUrl = selectedAvatar.imageUrl
                                        )
                                        withContext(Dispatchers.Main) {
                                            navController.navigate(
                                                Screens.Home.route
                                            )
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please Select an Avatar",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightText,
                            contentColor = textColor
                        ),
                        enabled = selectedAvatar.imageUrl != ""
                    ) {
                        Text(
                            text = "Take Me In",
                            color = Color.White,
                            fontSize = 20.sp,
                        )
                    }
                }
            }
        }


    }

    AnimatedVisibility(
        visible = !login,
        enter = slideInHorizontally(initialOffsetX = {
            it
        }),
        exit = slideOutHorizontally(
            targetOffsetX = {
                it
            }
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.app_icon),
                    tint = Color.Unspecified,
                    contentDescription = "Icon",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    Icons.Filled.MoreHoriz,
                    tint = Color(0xFF6297F1),
                    contentDescription = "Icon",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    Icons.Filled.TravelExplore,
                    tint = lightText,
                    contentDescription = "Icon",
                    modifier = Modifier.size(60.dp)
                )

            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithIconsLogin(
    textValue: String,
    placeholder: String,
    icon: ImageVector,
    mutableText: TextFieldValue,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    passwordVisible: MutableState<Boolean> = mutableStateOf(false),
    onValueChanged: (TextFieldValue) -> Unit,
    enabled: Boolean = true,
) {
    if (keyboardType == KeyboardType.Password) {
        TextField(
            value = mutableText,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    tint = Color(0xFF4483D1),
                    contentDescription = "Icon"
                )
            },
            trailingIcon = {
                val image = if (passwordVisible.value)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible.value) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    Icon(imageVector = image, description, tint = Color(0xFF4483D1))
                }
            },
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = onValueChanged,
            label = { Text(text = textValue, color = textColor) },
            placeholder = { Text(text = placeholder, color = textColor) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = textColor,
                disabledTextColor = textColor,
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                disabledContainerColor = CardBackground,
            ),
            enabled = enabled
        )
    } else {
        TextField(
            value = mutableText,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    tint = Color(0xFF4483D1),
                    contentDescription = "Icon"
                )
            },
            onValueChange = onValueChanged,
            label = { Text(text = textValue, color = textColor) },
            placeholder = { Text(text = placeholder, color = textColor) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = textColor,
                disabledTextColor = textColor,
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                disabledContainerColor = CardBackground,
            ),
            enabled = enabled

        )
    }
}

fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, auth: FirebaseAuth) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Successful")
            } else {
                println("Failed")
            }
        }
}

fun dpFromPx(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}
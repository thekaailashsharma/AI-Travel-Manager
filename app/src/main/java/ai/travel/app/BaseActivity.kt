package ai.travel.app

import ai.travel.app.datastore.UserDatastore
import ai.travel.app.home.HomeViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ai.travel.app.ui.theme.AITravelManagerTheme
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.monteSB
import ai.travel.app.ui.theme.textColor
import android.content.Intent
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.firestore.remote.Datastore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AITravelManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    val context = LocalContext.current
                    val userDatastore = UserDatastore(context)
                    val userName = userDatastore.getName.collectAsState(initial = "")
                    val userPhone = userDatastore.getNumber.collectAsState(initial = "")
                    val userGender = userDatastore.getGender.collectAsState(initial = "")
                    val loginStatus = userDatastore.getLoginStatus.collectAsState(initial = false)
                    val pfp = userDatastore.getPfp.collectAsState(initial = "")
                    SplashScreen(
                        homeViewModel = homeViewModel,
                        userName = userName.value,
                        userPhone = userPhone.value,
                        userGender = userGender.value,
                        loginStatus = loginStatus.value,
                        pfp = pfp.value
                    )

                }
            }
        }
    }
}

@Composable
fun SplashScreen(
    homeViewModel: HomeViewModel,
    userName: String,
    userPhone: String,
    userGender: String,
    loginStatus : Boolean,
    pfp: String
) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.95f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        runBlocking {
            homeViewModel.updateUserDetails(
                userName = userName,
                userPhoneNumber = userPhone,
                gender = userGender,
                loginStatus = loginStatus,
                pfp = pfp
            )
            delay(1000L)
            val refresh = Intent(context, MainActivity::class.java)
            refresh.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            ContextCompat.startActivity(context, refresh, null)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "Logo",
                modifier = Modifier.scale(scale.value)
            )
            Spacer(modifier = Modifier.height(20.dp))
            AnimatedVisibility(
                visible = scale.value > 0.4f,
                enter = slideInHorizontally(tween(150), initialOffsetX = {
                    it
                })
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = textColor,
                    fontSize = 25.sp,
                    fontFamily = monteSB,
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                )

            }

        }
    }
}


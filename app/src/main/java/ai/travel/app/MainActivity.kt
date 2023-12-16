package ai.travel.app

import ai.travel.app.bottomBar.BottomBar
import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.ui.HomeScreenMain
import ai.travel.app.navigation.NavController
import ai.travel.app.navigation.Screens
import ai.travel.app.riveanimation.SmsBroadcastReceiver
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
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import javax.annotation.Nullable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    private lateinit var viewModel: HomeViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isBottomBarVisible = remember { mutableStateOf(true) }
            val homeViewModel: HomeViewModel = hiltViewModel()
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            when (navBackStackEntry?.destination?.route) {
                Screens.Home.route -> {
                    isBottomBarVisible.value = true
                }

                Screens.Profile.route -> {
                    isBottomBarVisible.value = true
                }

                Screens.Routes.route -> {
                    isBottomBarVisible.value = false
                }

                Screens.TripDetails.route -> {
                    isBottomBarVisible.value = false
                }

                Screens.Login.route -> {
                    isBottomBarVisible.value = false
                }

                Screens.NewTrip.route -> {
                    isBottomBarVisible.value = false
                }
            }


            AITravelManagerTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            isBottomBarVisible = isBottomBarVisible
                        )
                    },


                    ) {
//                    HomeScreenMain()
                    NavController(
                        navHostController = navController,
                        paddingValues = it,
                        isBottomBarVisible = isBottomBarVisible
                    )
                    print(it)
                }
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    intent?.let { startActivityForResult(it, 200) }
                }

                override fun onFailure() {}
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }


    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                println("Message $message")
                viewModel.updateResult(message.toString())
            }
        }
    }
}

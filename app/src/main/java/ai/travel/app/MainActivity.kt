package ai.travel.app

import ai.travel.app.bottomBar.BottomBar
import ai.travel.app.home.ui.MemoryCollection
import ai.travel.app.navigation.NavController
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AITravelManagerTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomBar(navController = navController)
                    },


                ) {
//                    NavController(navHostController = navController)
                    print(it)

                    MemoryCollection()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AITravelManagerTheme {
        Greeting("Android")
    }
}
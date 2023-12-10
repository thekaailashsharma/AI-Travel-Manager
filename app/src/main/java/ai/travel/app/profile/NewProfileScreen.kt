package ai.travel.app.profile


import ai.travel.app.R
import ai.travel.app.datastore.UserDatastore
import ai.travel.app.navigation.Screens
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.monteSB
import ai.travel.app.ui.theme.textColor
import ai.travel.app.utils.ProfileImage
import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalComposeUiApi::class
)
@Composable
fun NewProfileScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val dataStore = UserDatastore(context)
    val myPhoneNumber by dataStore.getNumber.collectAsState("")
    val name by dataStore.getName.collectAsState("")
    val pfp by dataStore.getPfp.collectAsState("")
    val coroutineScope = rememberCoroutineScope()
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp, bottom = 7.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ProfileImage(
                    imageUrl = pfp,
                    modifier = Modifier
                        .size(100.dp)
                        .border(
                            width = 1.dp,
                            color = lightText,
                            shape = CircleShape
                        )
                        .padding(3.dp)
                        .clip(CircleShape),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 7.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name ?: "User Name",
                    color = textColor,
                    fontSize = 20.sp,
                    fontFamily = monteSB,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 7.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = myPhoneNumber ?: "Phone Number",
                    color = textColor,
                    fontSize = 12.sp,
                    fontFamily = monteSB,
                    softWrap = true,
                    modifier = Modifier.padding(end = 7.dp)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground.copy(0.8f)
                ),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Column {

                    RepeatedProfileInfo(
                        icon = Icons.Filled.EditNote,
                        text = "Edit Profile"
                    )
                    RepeatedProfileInfo(
                        icon = Icons.Filled.Notifications,
                        text = "Notifications"
                    )
                }

            }

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground.copy(0.8f)
                ),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Column {

                    RepeatedProfileInfo(
                        icon = Icons.Filled.SupportAgent,
                        text = "Help and Support"
                    )
                    RepeatedProfileInfo(
                        icon = Icons.Filled.ContactPage,
                        text = "Contact Us"
                    )
                    RepeatedProfileInfo(
                        icon = Icons.Filled.PrivacyTip,
                        text = "Privacy Policy"
                    )
                }

            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        dataStore.saveName("")
                        dataStore.savePfp("")
                        dataStore.saveName("")
                        dataStore.saveGender("")
                        dataStore.saveLoginStatus(false)
                    }
                    Firebase.auth.signOut()
                    navController.navigate(Screens.Login.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFD5065),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(35.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp)
            ) {
                Text(
                    text = "Logout",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = monteSB,
                    modifier = Modifier.padding(bottom = 4.dp),
                    maxLines = 1,
                    softWrap = true
                )
            }

        }
    }



@Composable
fun RepeatedProfileInfo(
    icon: ImageVector,
    text: String,
    onClick: (() -> Unit?)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick?.invoke()
            }
            .padding(start = 10.dp, top = 15.dp, bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = textColor,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 13.dp)
        )
        Text(
            text = text,
            color = textColor,
            fontSize = 15.sp,
            fontFamily = monteSB
        )
    }
}
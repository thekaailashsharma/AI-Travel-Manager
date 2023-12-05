package ai.travel.app.home.ui

import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.lightText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(appGradient)) {
        Text(text = "Home Screen", color = lightText)

    }

}

@Composable
fun PfScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(appGradient)) {
        Text(text = "Profile Screen", color = lightText)

    }

}

@Composable
fun RtScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(appGradient)) {
        Text(text = "Routes Screen", color = lightText)

    }

}
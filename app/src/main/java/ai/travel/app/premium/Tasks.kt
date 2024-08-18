package ai.travel.app.premium

import ai.travel.app.R
import ai.travel.app.ui.theme.indigo
import ai.travel.app.ui.theme.orange
import ai.travel.app.ui.theme.yellow
import androidx.compose.ui.graphics.Color

data class Tasks(
    val name: String,
    val color: Color,
    val icon: Int? = null,
)

val dummyTasks = listOf(
    Tasks(
        name = "Premium",
        color = orange,
        icon = R.drawable.premium
    ),
    Tasks(
        name = "3D Maps",
        color = indigo,
        icon = R.drawable.mapssubs
    ),
    Tasks(
        name = "Travel Planning",
        color = yellow,
        icon = R.drawable.travsuubs
    ),
    Tasks(
        name = "Itinerary",
        color = orange,
        icon = R.drawable.itisubs
    ),
    Tasks(
        name = "Tripify",
        color = Color.White,
        icon = R.drawable.app_icon
    ),

    )
package ai.travel.app.bottomBar


import ai.travel.app.ui.theme.bottomBarBackground
import ai.travel.app.ui.theme.bottomBarBorder
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavController, isBottomBarVisible: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = isBottomBarVisible.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        Card(
            modifier = Modifier
                .padding(start = 25.dp, end = 25.dp, bottom = 15.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = bottomBarBackground,
                contentColor = textColor
            ),
            elevation = CardDefaults.cardElevation(5.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, color = bottomBarBorder)
        ) {
            Divider(thickness = 1.dp, color = textColor.copy(0.5f))
            NavigationBar(
                modifier = Modifier
                    .height(80.dp),
                containerColor = bottomBarBackground,
                tonalElevation = 0.dp,
            ) {
                items.forEach {
                    val selected = currentRoute?.hierarchy?.any { nav ->
                        nav.route == it.route
                    } == true
                    NavigationBarItem(
                        icon = {
                            AnimatedIcon(
                                imageVector = it.icon,
                                scale = if (selected) 1.3f else 1.2f,
                                color = if (selected) textColor else lightText,
                            ) {
                                it.route?.let { it1 ->
                                    navController.navigate(it1) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        },
                        selected = selected,
                        onClick = {
                            it.route?.let { it1 ->
                                navController.navigate(it1) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = textColor.copy(0.9f),
                            indicatorColor = bottomBarBackground,
                        )
                    )
                }
            }
        }
    }
}
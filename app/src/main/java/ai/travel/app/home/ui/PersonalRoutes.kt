package ai.travel.app.home.ui

import ai.travel.app.R
import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import ai.travel.app.utils.dashedBorder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PersonalRoutes() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(
                    start = 12.dp,
                    top = 12.dp,
                    bottom = 0.dp,
                    end = 12.dp
                )
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = "topText",
                tint = lightText,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = "Personal Routes",
                color = textColor,
                fontSize = 16.sp,
            )
        }
        LazyRow() {
            item {
                NewRouteCard()
            }
        }
    }
}


@Composable
fun NewRouteCard() {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(180.dp)
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight(0.6f)
                    .dashedBorder(1.dp, textColor, 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(10.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.route),
                        contentDescription = "",
                        modifier = Modifier.size(50.dp),
                        tint = textColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Add New Route",
                fontSize = 15.sp,
                color = textColor,
                modifier = Modifier.weight(1f)
            )

        }
    }
}
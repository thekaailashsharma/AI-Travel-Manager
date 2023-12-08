package ai.travel.app.home.ui

import ai.travel.app.R
import ai.travel.app.bottomBar.items
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreenMain() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Card(modifier = Modifier.size(120.dp), shape = CircleShape) {
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Welcome to", fontSize = 20.sp)
            Text(text = "TRIPIFY", fontSize = 30.sp)


        }
        Divider(modifier = Modifier)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    modifier = Modifier
                )

                Text(text = "Personal Routes", fontSize = 20.sp)
            }

            Card {
                Text(text = "More", modifier = Modifier.padding(all = 5.dp))
            }

        }


        LazyRow {
            items(21) { listItem ->

                Spacer(modifier = Modifier.width(15.dp))
                Card() {

                    Column(
                        modifier = Modifier.padding(all = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.route),
                            contentDescription = "",
                            modifier = Modifier.size(150.dp)
                        )


                        Column {
                            Text(text = "Add New Route")

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )

                                Text(text = "$500", fontSize = 10.sp)
                                Spacer(modifier = Modifier.width(15.dp))
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )

                                Text(text = "57", fontSize = 10.sp)
                            }
                        }
                    }

                }

            }
        }




        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    modifier = Modifier
                )

                Text(text = "Quests", fontSize = 20.sp)
            }

            Card {
                Text(text = "More", modifier = Modifier.padding(all = 5.dp))
            }

        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.Start
        ) {


            Card(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(text = "Recommended ", modifier = Modifier.padding(all = 5.dp))
            }

            Card {
                Text(text = "Current ", modifier = Modifier.padding(all = 5.dp))
            }

        }


        TravelCards(
            text1 = "Witness the balance of design and innovation",
            text2 = "Quests", text3 = "Quests"
        )
        TravelCards(
            text1 = "Witness the balance of design and innovation",
            text2 = "Quests", text3 = "Quests"
        )
        TravelCards(
            text1 = "Witness the balance of design and innovation",
            text2 = "Quests", text3 = "Quests"
        )


    }
}


@Composable
fun TravelCards(text1: String, text2: String, text3: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 14.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        Card() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 14.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(modifier = Modifier.size(100.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.compose),
                        contentDescription = "",
                    )
                }


                Column {
                    Text(text = text1)

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Text(text = text2, fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Text(text = text3, fontSize = 10.sp)
                    }
                }

            }
        }
    }
}
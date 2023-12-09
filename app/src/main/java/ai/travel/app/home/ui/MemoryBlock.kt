package ai.travel.app.home.ui

import ai.travel.app.R
import ai.travel.app.ui.theme.Purple40
import ai.travel.app.ui.theme.Purple80
import ai.travel.app.ui.theme.PurpleGrey80
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryBlock() {
    var nameValue = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Memory Blocks", fontSize = 30.sp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 3.dp)
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = nameValue.value,
                colors = TextFieldDefaults.textFieldColors(Color.White),
                onValueChange = {
                    nameValue.value = it
                },
                label = {

                    Row {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "",
                            tint = Color.LightGray,
                            modifier = Modifier
                                .padding(horizontal = 2.dp, vertical = 2.dp)
                                .size(23.dp)
                        )

                        Text(
                            text = "Find your Location",
                            color = Color.LightGray,
                            fontSize = 18.sp

                        )
                    }
                },
                placeholder = {

                    Text(
                        text = "Location",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .size(60.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp)
                .padding(horizontal = 17.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "",
                    tint = PurpleGrey80,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(45.dp)
                )
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Vrindavan", fontSize = 25.sp, color = Purple80)
                    Text(text = "fav memories", fontSize = 15.sp, color = PurpleGrey80)
                }
            }

            Icon(
                imageVector = Icons.Filled.Dehaze,
                contentDescription = "",
                tint = Color.LightGray,
                modifier = Modifier
                    .padding(end = 18.dp)
                    .size(28.dp)
            )
        }

        LazyRow {
            items(21) { listItem ->


                Box(modifier = Modifier) {
                    Card(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.TopStart)
                            .zIndex(1f),
                        shape = CircleShape,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.compose),
                            contentDescription = ""
                        )
                    }

                    Card(
                        modifier = Modifier
                            .height(80.dp)
                            .width(135.dp)
                            .padding(all = 10.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp, vertical = 15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Divider(color = Color.White)

                        }


                    }

                }


            }
        }


        LazyStaggerdGrid()


    }
}


@Composable
fun LazyStaggerdGrid() {

    val items = (1..100).map {
        ListItem(
            height = Random.nextInt(100, 300).dp,
            color = Color(
                Random.nextLong(0xFFFFFFFF)
            ).copy(alpha = 1f),
            image = images.random()
        )
    }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp
    ) {

        items(items) { item ->

            ProfileRow()


            RandomColorBox(item = item)
        }

    }
}

data class ListItem(
    val height: Dp,
    val image: Int,
    val color: androidx.compose.ui.graphics.Color
)


val images = listOf(
    R.drawable.img,
    R.drawable.img_1,
    R.drawable.img_2,
    R.drawable.img_10,
    R.drawable.img_4,
    R.drawable.img_3,
    R.drawable.img_5,
    R.drawable.img_6,
    R.drawable.img_7,
    R.drawable.img_9,
    R.drawable.img_11,
    R.drawable.img_12


)


@Composable
fun ProfileRow() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .height(30.dp)
            .zIndex(1f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Card(modifier = Modifier.size(30.dp), shape = CircleShape) {

                Image(painter = painterResource(id = R.drawable.compose),
                    contentDescription = "")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Quests", fontSize = 10.sp)
            Spacer(modifier = Modifier.width(15.dp))
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Text(text = " Like ", fontSize = 10.sp)
        }
    }

}

@Composable
fun RandomColorBox(item: ListItem) {

    val context = LocalContext.current


    Row(
        modifier = Modifier

            .height(item.height)
            .clip(RoundedCornerShape(10.dp))
            .background(item.color)
    ) {
        Image(
            painter = painterResource(id = item.image),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 5.dp)
        )
    }


}




package ai.travel.app.home.ui

import ai.travel.app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun MemoryCollection() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(horizontal = 30.dp, vertical = 30.dp)) {
            Text(text = "Memory Blocks")
        }

        LazyStaggeredGrid()
    }
}

@Composable
fun LazyStaggeredGrid() {

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
//                    columns = StaggeredGridCells.Adaptive(50.dp),
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp
//       verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(items) { item ->
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
    R.drawable.ic_launcher_background,
    R.drawable.ic_launcher_background,
    R.drawable.ic_launcher_background,
    R.drawable.ic_launcher_background,
    R.drawable.ic_launcher_background
)



@Composable
fun RandomColorBox(item: ListItem) {
    Box(
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
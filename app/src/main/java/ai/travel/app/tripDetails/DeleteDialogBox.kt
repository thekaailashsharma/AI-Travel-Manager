package ai.travel.app.tripDetails

import ai.travel.app.ui.theme.CardBackground
import ai.travel.app.ui.theme.appGradient
import ai.travel.app.ui.theme.bottomBarBackground
import ai.travel.app.ui.theme.bottomBarBorder
import ai.travel.app.ui.theme.lightText
import ai.travel.app.ui.theme.textColor
import ai.travel.app.utils.dashedBorder
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeleteBottomSheet(
    isDeleteSuccess: MutableState<Boolean>,
    isDeleteUndo: MutableState<Boolean>,
    isDeleteClicked: MutableState<Boolean>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardBackground,
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This trip will be deleted permanently",
                    color = textColor,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Divider(
                    color = Color(0xFF3e3f3e),
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(onClick = {
                    isDeleteSuccess.value = true
                    isDeleteClicked.value = true
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ), modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Delete",
                        color = Color(0xFFFF1106),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(8.dp)

                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardBackground,
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    isDeleteUndo.value = true
                    isDeleteSuccess.value = false
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ), modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Cancel",
                        color = Color(0xFF87DAFF),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

            }
        }
    }
}
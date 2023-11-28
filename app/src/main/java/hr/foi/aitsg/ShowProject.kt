package hr.foi.aitsg

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import hr.foi.database.DataViewModel

class ShowProject {
    var id_project : Int? = null
    @Composable
    fun showProject( navHostController: NavHostController, dataViewModel: DataViewModel, project_id : String? ){
        Column {
            Row (
                modifier = Modifier.padding(10.dp)
            ){
                Text(text = "Izvješaji",)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navHostController.navigate("search-users/$project_id")},
                    contentAlignment = Alignment.BottomEnd
                ){
                    Image(
                        painter = painterResource(id = R.drawable.group),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape = CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(Color.White),
                        alignment = androidx.compose.ui.Alignment.CenterEnd,
                    )
                }

            }

        }
    }
}
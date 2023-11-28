package hr.foi.aitsg

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.foi.database.DataViewModel
import hr.foi.database.DefaultDataRepository
import hr.foi.database.Report

class ShowProject {
    var id_project : Int? = null
    @Composable
    fun showProject( navHostController: NavHostController, dataViewModel: DataViewModel, project_id : String? ){
        var id_project = project_id!!.toInt()
        var reports = getAllProjectReports(dataViewModel = dataViewModel, id_project = id_project)

        Log.e("Reporti", reports.toString())
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
            if(reports.isNotEmpty()){
                reports.forEach(){
                    Row (modifier = Modifier
                        .padding(all = 5.dp)
                        .background(
                            MaterialTheme.colorScheme.tertiary
                        )
                        .padding(5.dp)
                    ){
                        Text(
                            text = it.name,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.Top)
                        )

                    }
                }
            }

        }
    }
}
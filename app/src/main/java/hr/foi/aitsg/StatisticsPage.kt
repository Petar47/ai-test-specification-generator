package hr.foi.aitsg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import hr.foi.aitsg.auth.getAllProjectReports
import hr.foi.aitsg.auth.getProjects
import hr.foi.database.DataViewModel
import hr.foi.database.Report
import hr.foi.database.User
import org.apache.poi.sl.usermodel.VerticalAlignment

@Composable
fun StatisticsPage(viewModel: DataViewModel, onMenuClick: () -> Unit){
    //val loggedInUser = Authenticated.loggedInUser as User
    val mockUser: User = User(8, "vcor@foi.hr", "3eaf108c3f000e30b201863c6a58ec8174bec1d0c0602695d97da586ed94ff3b",
        "Viktor", "Coric")

    //var savedTimeSum by remember { mutableStateOf("0h 0m 0s") }
    var savedTimeData = HashMap<String, Float>()
    var coroutine = rememberCoroutineScope()
    val projects = getProjects(dataViewModel = viewModel, id_user = mockUser.id_user!!, coroutine)
    /*val reports = List<Report>() // TODO dohvatiti izvjestaje od korisnika
    projects.forEach { project ->
        var timeSum = 0f
        reports.forEach { report ->
            if(report.id_project == project.id_project){
                timeSum += report.saved_time.toFloat()
            }
        }
        savedTimeData.put(project.name, timeSum)
    }*/
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        var mockSavedTimeData = HashMap<String, Float>()
        mockSavedTimeData.put("Projekt 1", 333.12f)
        mockSavedTimeData.put("Projekt 2", 123114.4548f)
        mockSavedTimeData.put("Projekt 3", 34578.111f)
        mockSavedTimeData.put("Projekt 4", 88755124.13287f)
        TopAppBar(onMenu = {onMenuClick()})
        SavedTimeGraph(mockSavedTimeData)
        NumberOfReports()
        ScanningFrequency()
    }
}

@Composable
fun TopAppBar(onMenu: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClick = { onMenu() }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(45.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Statistika",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SavedTimeGraph(timeData: HashMap<String, Float>){
    //TODO stupicasti sa ustedjenim vremenima po projektu
    var savedTime = "0s"
    var timeSum = 0f
    timeData.forEach{k, v ->
        timeSum += v
    }
    savedTime = formatSeconds(timeSum)
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Ušteđeno vrijeme",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inversePrimary
            )
            Text(text = savedTime)
        }
        //Graf
    }
}
/*
fun barChart(data: HashMap<String, Float>): BarChartData{
    var labels = data.keys
    var floats = data.values
    var barchartData: List<BarData>
    data.forEach{k, v ->


    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(labels.size - 1)
        .bottomPadding(40.dp)
        .axisLabelAngle(20f)
        .labelData { index -> labels.elementAt(index)}
        .build()

    val yAxisData = AxisData.Builder()
        .steps(100)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (100000 / 100)).toString() }
        .build()
/*
    val barChartData = BarChartData(
        chartData = data,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        paddingBetweenBars = 20.dp,
        barWidth = 25.dp
    )

 */
    return barChartData
}
*/
@Composable
fun NumberOfReports(){
    //TODO Broj izvjesca po projektu, piechart
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Broj generiranih izvještaja",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }
        //graf
    }
}

@Composable
fun ScanningFrequency(){
    //TODO linijski graf sa ucestalosti skeniranja
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Učestalost skeniranja",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }
        //graf
    }
}

fun formatSeconds(seconds: Float): String {
    val days = (seconds / (24 * 60 * 60)).toInt()
    val hours = ((seconds % (24 * 60 * 60)) / (60 * 60)).toInt()
    val minutes = (((seconds % (24 * 60 * 60)) % (60 * 60)) / 60).toInt()
    val remainingSeconds = (((seconds % (24 * 60 * 60)) % (60 * 60)) % 60).toInt()

    val formattedTime = StringBuilder()

    if (days > 0) {
        formattedTime.append("${days}d ")
    }

    if (hours > 0) {
        formattedTime.append("${hours}h ")
    }

    if (minutes > 0) {
        formattedTime.append("${minutes}m ")
    }

    if (remainingSeconds > 0 || formattedTime.isEmpty()) {
        formattedTime.append("${remainingSeconds}s")
    }

    return formattedTime.toString().trim()
}
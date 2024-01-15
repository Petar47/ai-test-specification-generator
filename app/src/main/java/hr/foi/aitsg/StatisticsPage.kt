package hr.foi.aitsg

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import hr.foi.aitsg.auth.getAllProjectReports
import hr.foi.aitsg.auth.getProjects
import hr.foi.aitsg.composables.CircularLoadingBar
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.Project
import hr.foi.database.Report
import hr.foi.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.apache.poi.sl.usermodel.VerticalAlignment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun StatisticsPage(viewModel: DataViewModel, onMenuClick: () -> Unit){
    val loggedInUser = Authenticated.loggedInUser as User
    //val mockUser: User = User(8, "vcor@foi.hr", "3eaf108c3f000e30b201863c6a58ec8174bec1d0c0602695d97da586ed94ff3b", "Viktor", "Coric")

    //var savedTimeSum by remember { mutableStateOf("0h 0m 0s") }
    var savedTimeData: ArrayList<Pair<String, Float>> = ArrayList<Pair<String, Float>>()
    var numberOfReports: ArrayList<Pair<String, Int>> = ArrayList<Pair<String, Int>>()
    var coroutine = rememberCoroutineScope()
    val projects = getProjects(dataViewModel = viewModel, id_user = loggedInUser.id_user!!, coroutine = coroutine)
    val reports = getReports(dataViewModel = viewModel, id_user = loggedInUser.id_user!!, coroutine = coroutine)
    projects.forEach { project ->
        var timeSum = 0f
        var reportSum = 0
        reports.forEach { report ->
            if(report.id_project == project.id_project){
                timeSum += report.saved_time.toFloat()
                reportSum += 1
            }
        }
        if(timeSum != 0f){
            savedTimeData.add(Pair(project.name, timeSum))
        }
        numberOfReports.add(Pair(project.name, reportSum))

    }
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        TopAppBar(onMenu = {onMenuClick()})
        if (projects.isEmpty() || reports.isEmpty()) {
            CircularLoadingBar()
        }else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                SavedTimeGraph(savedTimeData)
                Spacer(modifier = Modifier.height(20.dp))
                NumberOfReports(numberOfReports, reports.size)
                Spacer(modifier = Modifier.height(20.dp))
                ScanningFrequency(reports)
            }
        }
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
fun SavedTimeGraph(timeData: ArrayList<Pair<String, Float>>){
    var savedTime = "0s"
    var timeSum = 0f
    timeData.forEach{pair ->
        timeSum += pair.second
    }
    savedTime = formatSeconds(timeSum)
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(modifier = Modifier.fillMaxWidth()){
            Spacer(modifier = Modifier.width(10.dp))
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
                Text(text = savedTime,color = MaterialTheme.colorScheme.inversePrimary)
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        if(!timeData.isEmpty()){
            val barChartData = barChart(timeData)
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                BarChart(
                    modifier = Modifier
                        .height(300.dp)
                        .width(400.dp)
                        .background(color = Color.Transparent),
                    barChartData = barChartData
                )
            }
        }
    }
}
@Composable
fun barChart(data: ArrayList<Pair<String, Float>>): BarChartData{
    var barDataList: ArrayList<BarData> = ArrayList<BarData>()
    var maxNum = 0f
    data.forEachIndexed{i, item ->
        var barData = BarData(point = Point((i+1).toFloat(), item.second), label = item.first, color = MaterialTheme.colorScheme.primary)
        barDataList.add(barData)
        if(item.second > maxNum){
            maxNum = item.second
        }
    }

    val xAxisData = AxisData.Builder()
        .axisOffset(10.dp)
        .axisStepSize(100.dp)
        .steps(barDataList.size - 1)
        .bottomPadding(40.dp)
        .axisLabelAngle(20f)
        .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
        .axisLabelColor(MaterialTheme.colorScheme.inversePrimary)
        .labelData { i -> barDataList[i].label}
        .backgroundColor(MaterialTheme.colorScheme.background)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelAndAxisLinePadding(50.dp)
        .axisOffset(20.dp)
        .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
        .axisLabelColor(MaterialTheme.colorScheme.inversePrimary)
        .labelData { index -> formatSeconds((index * (maxNum / 5))) }
        .backgroundColor(MaterialTheme.colorScheme.background)
        .build()

    val barChartData = BarChartData(
        chartData = barDataList,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colorScheme.background
    )


    return barChartData
}

@Composable
fun NumberOfReports(data: ArrayList<Pair<String, Int>>, reportsSum: Int){
    var projectName by remember{mutableStateOf("")}
    var numReportsOnProject by remember {mutableStateOf(0)}
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(modifier = Modifier.fillMaxWidth()){
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Broj generiranih izvještaja",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
                Text(text = reportsSum.toString(),color = MaterialTheme.colorScheme.inversePrimary)
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        if(!data.isEmpty()){
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                pieChart(data, onSliceClick = {slice ->
                    projectName = slice.label
                    numReportsOnProject = slice.value.toInt()
                })
            }
        }
        if(numReportsOnProject > 0){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(
                    text = projectName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
                Text(
                    text = numReportsOnProject.toString(),
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    }
}

fun getColor(index: Int): Color {
    val predefinedColors = arrayOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan,
        Color.LightGray,
        Color.Gray,
        Color.Black,
        Color.DarkGray,
        Color.Magenta
    )


    return predefinedColors[index%predefinedColors.size]
}
@Composable
fun pieChart(data: ArrayList<Pair<String, Int>>, onSliceClick: (PieChartData.Slice) -> Unit){
    var chartData: ArrayList<PieChartData.Slice> = ArrayList<PieChartData.Slice>()
    var colors =
    data.forEachIndexed{index, pair ->
        chartData.add(PieChartData.Slice(pair.first, pair.second.toFloat(), color = getColor(index)))
    }
    val pieChartData = PieChartData(
        slices = chartData,
        plotType = PlotType.Pie
    )

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = false,
        showSliceLabels = true,
        backgroundColor = MaterialTheme.colorScheme.background,
        labelColor = MaterialTheme.colorScheme.inversePrimary,
        labelType = PieChartConfig.LabelType.VALUE,
        isSumVisible = true
    )

    PieChart(
        modifier = Modifier
            .height(250.dp)
            .width(250.dp)
            .background(MaterialTheme.colorScheme.background),
        pieChartData,
        pieChartConfig,
        onSliceClick = {slice ->
            onSliceClick(slice)
        }
    )
}

@Composable
fun ScanningFrequency(reports: List<Report>){
    //TODO linijski graf sa ucestalosti skeniranja
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Učestalost skeniranja",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inversePrimary
            )
            //val names = listOf("Dan","Mjesec","Godina")
            //SelectableDropdownMenu(names = names)

        }
        Spacer(modifier = Modifier.height(10.dp))
        if(!reports.isEmpty()){
            lineChart(reports)
        }
    }
}

@Composable
fun lineChart(data: List<Report>){
    val steps = 5
    var listOfDates: ArrayList<String> = ArrayList<String>()
    var pointsData: ArrayList<Point> = ArrayList<Point>()
    val datesPointsData = getPoints(data)
    var maxRange = 0
    datesPointsData.forEach{pair ->
        listOfDates.add(pair.first)
        pointsData.add(pair.second)
        if(maxRange < pair.second.y){
            maxRange = pair.second.y.toInt()
        }
    }

    while(maxRange%5 != 0){
        maxRange++
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(MaterialTheme.colorScheme.background)
        .steps(pointsData.size-1)
        .labelData { i -> listOfDates[i] }
        .labelAndAxisLinePadding(15.dp)
        .axisLabelColor(MaterialTheme.colorScheme.inversePrimary)
        .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(MaterialTheme.colorScheme.background)
        .labelAndAxisLinePadding(20.dp)
        .axisLabelColor(MaterialTheme.colorScheme.inversePrimary)
        .axisLineColor(MaterialTheme.colorScheme.inversePrimary)
        .labelData { i ->
            val yScale = maxRange / steps
            (i * yScale).toString()
        }.build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(color = MaterialTheme.colorScheme.primary),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = MaterialTheme.colorScheme.background
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )
}

fun getPoints(reports: List<Report>): List<Pair<String, Point>>{
    var list: ArrayList<Pair<String, Point>> = ArrayList<Pair<String, Point>>()
    var dateList: ArrayList<String> = ArrayList<String>()
    reports.forEach{report ->
        dateList.add(formatDateString(report.generated.toString()))
    }
    dateList = dateList.distinct() as ArrayList<String>
    dateList.forEachIndexed {index, s ->
        var reportSum = 0
        reports.forEach{report ->
            if(s == formatDateString(report.generated.toString())){
                reportSum++
            }
        }
        list.add(Pair(s, Point(x = (index+1).toFloat(), y = reportSum.toFloat())))
    }
    return list
}

fun formatDateString(input: String): String {
    val inputDate = input.split("T").first()
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yy")

    val dateTime = LocalDate.parse(inputDate, inputFormatter)
    return dateTime.format(outputFormatter)
}

fun sortDates(dateList: List<Pair<String, Point>>): List<Pair<String, Point>> {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yy")

    val sortedList = dateList.sortedBy { LocalDate.parse(it.first, formatter) }

    return sortedList
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

@Composable
@SuppressLint("CoroutineCreationDuringComposition")
fun getReports(
    dataViewModel: DataViewModel,
    id_user: Int,
    coroutine: CoroutineScope
): List<Report> {
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    LaunchedEffect(key1 = id_user) {
        dataViewModel.getUserReports(id_user)
        coroutine.launch {
            dataViewModel.uiState.collectLatest { data ->
                when (data) {
                    is APIResult.Error -> {
                        Log.e("Error Data - getReports", "error getAllReports")
                    }

                    APIResult.Loading -> {
                        Log.d("Loading Data - getReports", "loading getAllReports")
                    }

                    is APIResult.Success -> {
                        if(data.data is List<*>) {
                            val dataList = data.data as List<*>
                            if (dataList.isNotEmpty() && dataList[0] is Report) {
                                reports = data.data as List<Report>
                            }
                        }
                        Log.d("Success - getProjects", reports.toString())
                    }
                }
            }
        }
    }
    return reports
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectableDropdownMenu(names: List<String>){
    var isExpanded by remember {
        mutableStateOf(false)
    }

    val selectedNames = remember {
        mutableStateListOf<String>()
    }

    val focusRequester = remember { FocusRequester() }

    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = {isExpanded = it}) {
        TextField(
            value = selectedNames.joinToString(", "),
            onValueChange = {},
            placeholder = {
                Text(text = "Odaberi vremenski interval")
            },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .focusRequester(focusRequester)
                .clickable { focusRequester.requestFocus() }
        )
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it }
        ) {
            // TextField

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                names.forEach { name ->
                    AnimatedContent(
                        targetState = selectedNames.contains(name),
                        label = "Animate the selected item"
                    ) { isSelected ->
                        if (isSelected) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = name)
                                },
                                onClick = {
                                    selectedNames.remove(name)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = null
                                    )
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = {
                                    Text(text = name)
                                },
                                onClick = {
                                    selectedNames.add(name)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
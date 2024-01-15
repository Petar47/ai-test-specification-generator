package hr.foi.aitsg

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.foi.aitsg.auth.getAllProjectReports
import hr.foi.database.DataViewModel
import hr.foi.database.Report

class ShowProject {
    var id_project: Int? = null
    @Composable
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    fun showProject(
        navHostController: NavHostController,
        dataViewModel: DataViewModel,
        project_id: String?,
        context : Context
    ) {
        var id_project = project_id!!.toInt()
        var reports = getAllProjectReports(dataViewModel = dataViewModel, id_project = id_project)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    androidx.compose.material3.TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { navHostController.navigate("menu") }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(45.dp)
                                    )
                                }

                                Text(
                                    text = " ",
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(45.dp)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    )
                },
                floatingActionButton = {
                    Column {
                        MainActivity.scannersList.map{scanner ->
                            FloatingActionButton(
                                onClick = {
                                    navHostController.navigate(scanner.getRoute() + id_project)
                                },
                                content = {
                                    Icon(
                                        painter = scanner.getIcon(),
                                        contentDescription = null,
                                        modifier = Modifier.size(45.dp)
                                    )
                                },
                                containerColor = MaterialTheme.colorScheme.primary
                            )

                        }
                    }
                }

            ) {
                BodyView(reports, navHostController, project_id, context)
            }
        }
    }


    private @Composable
    fun BodyView(reports: List<Report>, navHostController: NavHostController, project_id: String, context: Context) {
        Column {
            Spacer(modifier = Modifier.height(65.dp))
            Row(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = "Izvještaji")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navHostController.navigate("add-users/$project_id") },
                    contentAlignment = Alignment.BottomEnd
                ) {
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
            if (reports.isNotEmpty()) {
                LazyColumn {
                    item {
                        //Spacer(modifier = Modifier.height(65.dp))
                    }
                    items(reports) { report ->
                        Divider(modifier = Modifier.fillMaxWidth())
                        ReportItem(report, navHostController, context)
                    }
                }
            }

        }
    }

    @Composable
    fun ReportItem(report: Report, navHostController: NavHostController, context: Context) {
        val generated = report.generated!!.split('T')
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    navHostController.navigate("report-view/${report.id_report}/${report.JSON_response}")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.xlsx),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(5.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column{
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = report.name,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                    //modifier = Modifier.width(230.dp)
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = generated[0],
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = CircleShape)
                            .background(MaterialTheme.colorScheme.tertiary)
                            .padding(5.dp)
                            .clickable {
                                sendEmail(context, report.name, report.JSON_response)
                            },
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = CircleShape)
                            .background(MaterialTheme.colorScheme.tertiary)
                            .padding(5.dp)
                            .clickable {
                                DownloadReport(context, report.JSON_response, report.name)
                            },
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        }
    }

}
package hr.foi.aitsg

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.foi.aitsg.auth.getAllUserReports
import hr.foi.database.User
import hr.foi.database.DataViewModel
import hr.foi.database.Project
import hr.foi.aitsg.auth.getProjects
import hr.foi.database.Report


@Composable
fun History(navController: NavHostController, viewModel: DataViewModel, context: Context) {
    val user: User
    var id: Int? = null
    if (Authenticated.loggedInUser != null) {
        user = Authenticated.loggedInUser!!
        id=user.id_user
    }
    var coroutine = rememberCoroutineScope()
    val reports = getAllUserReports(userId = id!!, dataViewModel = viewModel)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ListOfReports(reports, navController, context)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfReports(reports: List<Report>, navController: NavHostController, context: Context) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigate("menu") }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(45.dp)
                            )
                        }

                        Text(
                            text = "Povijest",
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(45.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.tertiary)
            )
        }
    ) {
        if (reports.isNotEmpty()) {
            ReportsView(reports, navController, context)
        }
    }
}

@Composable
fun ReportsView(reports: List<Report>, navController: NavHostController, context: Context) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(65.dp))
        }
        items(reports) { report ->
            Divider(modifier = Modifier.fillMaxWidth())
            ReportItem(report, navController, context)
        }
    }
}

@Composable
fun ReportItem(report: Report, navController: NavHostController, context: Context) {
    val generated = report.generated!!.split('T')
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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

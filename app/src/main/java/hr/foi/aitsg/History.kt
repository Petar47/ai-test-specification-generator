package hr.foi.aitsg

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Spinner
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import hr.foi.aitsg.auth.getAllUserReports
import hr.foi.database.User
import hr.foi.database.DataViewModel
import hr.foi.database.Project
import hr.foi.aitsg.auth.getProjects
import hr.foi.database.Report
import hr.foi.database.UserReportsWithProjects


@Composable
fun History(navController: NavHostController, viewModel: DataViewModel, context: Context) {
    val user: User
    var id: Int? = null
    if (Authenticated.loggedInUser != null) {
        user = Authenticated.loggedInUser!!
        id=user.id_user
    }
    var coroutine = rememberCoroutineScope()
    var reports = getAllUserReports(userId = id!!, dataViewModel = viewModel)
    var projects = mutableListOf<Project>()
    reports.forEach(){
        if(!projects.contains(it.Project)){
            projects.add(it.Project)
        }
    }
    if(SelectedProjects.selectedSort.id == 1){
        reports = reports.sortedBy { it->it.name }
    }
    else if (SelectedProjects.selectedSort.id == 2){
        reports = reports.sortedByDescending { it-> it.name }
    }
    else if (SelectedProjects.selectedSort.id == 3){
        reports = reports.sortedBy { it-> it.generated }
    }
    else if (SelectedProjects.selectedSort.id == 4){
        reports = reports.sortedByDescending { it-> it.generated }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ListOfReports(reports, navController, context, projects)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfReports(reports: List<UserReportsWithProjects>, navController: NavHostController, context: Context, projects: List<Project>) {
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
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(45.dp)
                            )
                        }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.tertiary)
            )
        }
    ) {
        if (reports.isNotEmpty()) {
            ReportsView(reports, navController, context, projects)
        }
    }
}

@Composable
fun ReportsView(reports: List<UserReportsWithProjects>, navController: NavHostController, context: Context, projects: List<Project>) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(65.dp))
        }
        item{
            SelectableSort(navHostController = navController, names = projects)
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End){
                Button(onClick = { navController.navigate("history")}) {
                    Text("Filtriraj")
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        }
        items(reports) { report ->
            if(SelectedProjects.selectedProjects.contains(report.Project) || SelectedProjects.selectedProjects.isEmpty()) {
                ReportItem(report, navController, context)
            }
        }
    }
}

@Composable
fun ReportItem(report: UserReportsWithProjects, navController: NavHostController, context: Context) {
    val generated = report.generated!!.split('T')
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp)){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
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
                    .padding(10.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column{
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = report.name,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = report.Project.name,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = generated[0],
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary,
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
                            .size(35.dp)
                            //.clip(shape = CircleShape)
                            //.background(MaterialTheme.colorScheme.tertiary)
                            .padding(5.dp)
                            .clickable {
                                sendEmail(context, report.name, report.JSON_response)
                            },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inversePrimary)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = null,
                        modifier = Modifier
                            .size(35.dp)
                            .padding(5.dp)
                            .clickable {
                                DownloadReport(context, report.JSON_response, report.name)
                            },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inversePrimary)
                    )
                }
            }
        }
    }
}

data class PreviewOptions(val id: Int, val text: String)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectableSort(navHostController: NavHostController, names: List<Project>) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val options = arrayOf("naziv-uzlazno","naziv-silazno", "datum-uzlazno", "datum-silazno")
    var isExpanded1 by remember {
        mutableStateOf(false)
    }
    val selectedNames = remember {
        mutableStateListOf<String>()
    }
    SelectedProjects.selectedProjects.forEach(){
        if(!selectedNames.contains(it.name)){
            selectedNames.add(it.name)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(modifier = Modifier
            .fillMaxWidth(0.8f),
            contentAlignment = Alignment.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded1,
                onExpandedChange = { isExpanded1 = it }
            ) {
                TextField(
                    value = selectedNames.joinToString(", "),
                    onValueChange = {},
                    placeholder = {
                        Text(text = "Projekti")
                    },
                    readOnly = true, // Makes the TextField clickable
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded1)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor() // Needed to anchor the dropdown menu
                )
                ExposedDropdownMenu(
                    expanded = isExpanded1,
                    onDismissRequest = { isExpanded1 = false }
                ) {
                    names.forEach { name ->
                        AnimatedContent(
                            targetState = selectedNames.contains(name.name),
                            label = "Animate the selected item"
                        ) { isSelected ->
                            if (isSelected) {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = name.name)
                                    },
                                    onClick = {
                                        selectedNames.remove(name.name)
                                        SelectedProjects.selectedProjects.remove(name)
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
                                        Text(text = name.name)
                                    },
                                    onClick = {
                                        selectedNames.add(name.name)
                                        SelectedProjects.selectedProjects.add(name)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth(),
            contentAlignment = Alignment.Center) {
            Row {
                Box(
                    contentAlignment = Alignment.Center
                ) {

                    IconButton(onClick = {
                        isExpanded = true
                        Log.e("Klik na gumb", isExpanded.toString())
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.sort),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp),
                            tint = MaterialTheme.colorScheme.inversePrimary
                        )
                    }

                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = {
                            isExpanded = false
                        }
                    ) {
                        // adding items
                        options.forEachIndexed { itemIndex, itemValue ->
                            DropdownMenuItem(
                                onClick = {
                                    SelectedProjects.selectedSort =
                                        SortTypes(itemIndex + 1, itemValue)
                                    navHostController.navigate("history")
                                },
                                text = {
                                    Text(text = itemValue)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenu1(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(-16.dp, -16.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit
){
}

@Composable
fun DropdownMenuItem1(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = MenuDefaults.DropdownMenuItemContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
){}

package hr.foi.aitsg

import android.annotation.SuppressLint
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
import hr.foi.database.User
import hr.foi.database.DataViewModel
import hr.foi.database.Project
import hr.foi.aitsg.auth.getProjects


@Composable
fun ListofProjects(navController: NavHostController, viewModel: DataViewModel) {
    val user: User
    var id: Int? = null
    if (Authenticated.loggedInUser != null) {
        user = Authenticated.loggedInUser!!
        id = user.id_user
    }
    var coroutine = rememberCoroutineScope()
    val projects = getProjects(dataViewModel = viewModel, id_user = id!!, coroutine)
    UserProjects.projects = projects

    //var isLoading by remember { mutableStateOf(false) }

    /*if (isLoading){
        CircularLoadingBar()
    }*/
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ProjectList(projects, navController)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectList(projects: List<Project>, navController: NavHostController) {
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
                                tint = Color.White,
                                modifier = Modifier.size(45.dp)
                            )
                        }

                        Text(
                            text = "Projekti",
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
            FloatingActionButton(
                onClick = {
                    navController.navigate("addProject")
                },
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        if (projects.isNotEmpty()) {
            ProjectListView(projects, navController)
        }
    }
}

@Composable
fun ProjectListView(projects: List<Project>, navController: NavHostController) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(65.dp))
        }
        items(projects) { project ->
            Divider(modifier = Modifier.fillMaxWidth())
            ProjectItem(project, navController)
        }
    }
}

@Composable
fun ProjectItem(project: Project, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate("show-project/" + "${project.id_project}")
            },
        verticalAlignment = Alignment.CenterVertically

    ) {
        Image(
            painter = painterResource(id = R.drawable.file),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(5.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = project.name,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectItemPreview() {
    //ProjectItem(Project(1, "ime","marko", 1))
}
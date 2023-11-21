package hr.foi.aitsg

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.foi.database.APIResult
import hr.foi.database.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import hr.foi.database.DataViewModel
import hr.foi.database.Project
import androidx.navigation.compose.rememberNavController
import hr.foi.aitsg.composables.CircularLoadingBar

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ListofProjects(navController: NavHostController, viewModel: DataViewModel, onMenuClick: (page:String) -> Unit) {
    val user : User
    var id : Int? = 8
    if(Authenticated.loggedInUser!=null){
        user = Authenticated.loggedInUser!!
        id=user.id_user
    }
    if(id!=null){
        viewModel.getProjects(id)
    }
    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    coroutine.launch{
        viewModel.uiState.collectLatest { data ->
            when(data){
                is APIResult.Error -> {
                    isLoading = false
                    Log.e("Error data", "mess ${data}")
                }
                APIResult.Loading -> {
                    isLoading = true
                    Log.e("Error Data", "loading")
                }
                is APIResult.Success -> {
                    isLoading = false
                    projects=data.data as List<Project>
                }
            }
        }
    }
    if (isLoading){
        CircularLoadingBar()
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ProjectList(projects, navController, onMenu = {onMenuClick("workspaces")})
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectList(projects: List<Project>, navController: NavHostController, onMenu: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {onMenu()}) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null, tint = Color.White)
                        }

                        Text(
                            text = "Projekti",
                            color = MaterialTheme.colorScheme.onSecondary,

                        )

                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.tertiary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("addProject") },
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        if(projects.isNotEmpty()){
            ProjectListView(projects)
        }
    }
}

@Composable
fun ProjectListView(projects: List<Project>) {
    LazyColumn{
        item {
            Spacer(modifier = Modifier.height(65.dp))
        }
        items(projects) { project ->
            ProjectItem(project)
        }
    }
}

@Composable
fun ProjectItem(project: Project) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.file),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(5.dp)
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
    ProjectItem(Project(1, "ime","marko", 1))
}

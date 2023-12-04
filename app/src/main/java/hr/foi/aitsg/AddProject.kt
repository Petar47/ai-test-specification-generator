package hr.foi.aitsg

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.foi.database.DataViewModel
import hr.foi.database.Project
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProject(navController: NavHostController, viewModel: DataViewModel) {
    //var showMessage by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {SnackbarHostState()}

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigate("workspaces")
                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.White, modifier = Modifier.size(45.dp))
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(text = "Dodavanje novog projekta", color = Color.White, textAlign = TextAlign.Center)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                var nazivProjekta by remember { mutableStateOf("") }
                TextField(
                    value = nazivProjekta,
                    onValueChange = { nazivProjekta = it },
                    label = { Text("Naziv projekta") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                var clanoviProjekta by remember { mutableStateOf("") }
                /*TextField(
                    value = clanoviProjekta,
                    onValueChange = { clanoviProjekta = it },
                    label = { Text("Članovi projekta (svaki odvojen novim redom)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .heightIn(min = 120.dp) // Set the desired height for multiline input
                        .background(Color.White), // Add a background color to make it clear it's multiline
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text // Specify the keyboard type as Text
                    )
                )*/
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        viewModel.insertProject(Project(null,nazivProjekta,null,
                            Authenticated.loggedInUser?.id_user
                        ))
                        //showMessage = true
                        scope.launch { snackbarHostState.showSnackbar("Projekt uspješno spremljen") }
                        navController.navigate("workspaces")
                    },
                        modifier = Modifier.fillMaxWidth().height(56.dp).padding(5.dp)
                        ) {
                        Text(
                            text ="Kreiraj",
                            fontSize = 16.sp
                            )
                    }
                    /*if (showMessage) {
                        Snackbar(
                            action = {
                                Button(
                                    onClick = {
                                        showMessage = false
                                    }
                                ) {
                                    Text(text = "OK")
                                }
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "Projekt uspješno spremljen")
                        }
                    }*/
                }
            }
        }
    )
}
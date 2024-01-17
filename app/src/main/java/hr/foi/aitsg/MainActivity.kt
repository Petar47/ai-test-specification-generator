package hr.foi.aitsg

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import dagger.hilt.android.AndroidEntryPoint
import hr.foi.aitsg.auth.searchUsers
import hr.foi.aitsg.ui.theme.AITSGTheme
import hr.foi.database.DataViewModel
import hr.foi.interfaces.TestRetriever
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.core.content.ContextCompat
import hr.foi.scanner.ScannerPage
import com.google.gson.Gson
import hr.foi.database.Project
import hr.foi.interfaces.Scanner
import hr.foi.testupload.FileScanner
import hr.foi.scanner.ScannerTestRetriever

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<DataViewModel>()
    private val _showProject: ShowProject = ShowProject() // maknuti ove crte kaj su ispot

    companion object {
        val scannersList: List<Scanner> = listOf(FileScanner(), ScannerTestRetriever())
    }

    //contains the content of the test -> its use is to save the test when the app is navigating from scanner to preview
    var testContent: String = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setPropertires - for report generator
        setProperties()
        setContent {
            AITSGTheme {
                val permissionViewModel = viewModel<PermissionViewModel>()
                val dialogQueue = permissionViewModel.visiblePermissionDialogQueue

                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        perms.keys.forEach { permission ->
                            permissionViewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }
                    }
                )



                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "login") {
                        composable("login") {
                            LoginPage(navController = navController, dataViewModel = viewModel,
                                successfulLogin = {
                                    navController.navigate("workspaces")
                                })

                        }
                        composable("register") {
                            RegistrationPage(navController = navController, viewModel = viewModel)
                        }
                        composable("addProject") {
                            AddProject(navController = navController, viewModel = viewModel)
                        }
                        composable("workspaces") {
                            ListofProjects(navController = navController, viewModel = viewModel)
                        }
                        composable("show-project/{id}") { navBackStack ->
                            val context = LocalContext.current
                            val counter = navBackStack.arguments?.getString("id")
                            _showProject.showProject(
                                navHostController = navController,
                                dataViewModel = viewModel,
                                project_id = counter,
                                context
                            )
                        }
                        composable("add-users/{id}") { navBackStack ->
                            val _project_id = navBackStack.arguments?.getString("id")
                            addUsersToProject(
                                navHostController = navController,
                                dataViewModel = viewModel,
                                project_id = _project_id,
                                deletedUser = {
                                    Toast.makeText(
                                        applicationContext,
                                        "Korisnik uspješno obrisan s projekta!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        }
                        composable("search-users/{id}") { navBackStack ->
                            val _project_id = navBackStack.arguments?.getString("id")
                            searchUsers(
                                navHostController = navController,
                                dataViewModel = viewModel,
                                id_project = _project_id,
                                addedUserToProject = {
                                    Toast.makeText(
                                        applicationContext,
                                        "Korisnik uspješno dodan u projekt!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        }
                        composable("profile") {
                            UpdateProfile(navHostController = navController, viewModel = viewModel,
                                onUpdateUser = {
                                    Toast.makeText(
                                        applicationContext,
                                        "Ažurirani podaci korisnika",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        }
                        composable("menu") {
                            MenuPage(onMenuButtonClick = { page ->
                                when (page) {
                                    "workspaces" -> navController.navigate("workspaces")
                                    "statistics" -> navController.navigate("statistics")
                                    "history" -> navController.navigate("history")
                                    else -> Toast.makeText(
                                        applicationContext,
                                        "Not implemented yet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    //TODO add navigation for profile, history and statistics
                                }
                            }, onLogOutButtonClick = {
                                Toast.makeText(
                                    applicationContext,
                                    "Log Out",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("login")
                            }, onReturnButtonClick = {
                                navController.popBackStack()
                            }, onEditProfileButtonClick = {
                                navController.navigate("profile")
                            })
                        }
                        composable("statistics") {
                            StatisticsPage(
                                viewModel = viewModel,
                                onMenuClick = {
                                    navController.navigate("menu")
                                }
                            )
                        }
                        composable("history") {
                            val context = LocalContext.current
                            History(
                                navController = navController,
                                viewModel = viewModel,
                                context
                            )
                        }
                        composable("testPreview/{id}") {
                            val coroutineScope = rememberCoroutineScope()
                            val projectId = it.arguments?.getString("id")
                            Log.d("Preview", testContent)
                            TestPreviewPage(
                                testData = testContent,
                                onClickNext = { testData ->
                                    testContent = testData
                                    navController.navigate("report-preview/$projectId")
                                    //TODO navigate to the report generation
                                },
                                onClickBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("report-preview/{id}") {
                            val projectId = it.arguments?.getString("id")
                            val openAIHandler = OpenAIHandler()
                            var response by remember { mutableStateOf<OpenAIResponse?>(null) }
                            var isLoading by remember { mutableStateOf(true) }

                            if (isLoading) {
                                LaunchedEffect(Unit) {
                                    response = openAIHandler.makeQuery(testContent)
                                    isLoading = false
                                }
                            }
                            response?.let { it1 ->
                                ReportPreviewPage(
                                    navController,
                                    viewModel,
                                    it1,
                                    projectId
                                )
                            }
                        }
                        composable("report-view/{id}/{data}") {
                            val reportId = it.arguments?.getString("id")
                            val jsonData = it.arguments?.getString("data")
                            val gson = Gson()
                            val oar = gson.fromJson(jsonData, OpenAIResponse::class.java)
                            ReportPreviewPage(
                                navController = navController,
                                viewModel = viewModel,
                                response = oar,
                                projectId = reportId
                            )

                            scannersList.map { scanner ->
                                composable(scanner.getRoute() + "{id}") { navBackStack ->
                                    multiplePermissionResultLauncher.launch(
                                        arrayOf(
                                            android.Manifest.permission.CAMERA,
                                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                                        )
                                    )
                                    val projectId = navBackStack.arguments?.getString("id")
                                    Column() {
                                        scanner.TestRetrieverUI(getTestData = { testData ->
                                            testContent = testData
                                            Log.d("Datoteka - MainAct", testContent)
                                            navController.navigate("testPreview/$projectId")
                                        })
                                    }
                                }
                            }
                        }
                    }

                    //Permission handler
                    dialogQueue
                        .reversed()
                        .forEach { permission ->
                            PermissionDialog(
                                permissionTextProvider = when (permission) {
                                    android.Manifest.permission.CAMERA -> CameraPermissionTextProvider()
                                    else -> return@forEach
                                    //TODO add more permissions if needed
                                },
                                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                    permission
                                ),
                                onDismiss = permissionViewModel::dismissDialog,
                                onOkClick = {
                                    permissionViewModel.dismissDialog()
                                    multiplePermissionResultLauncher.launch(arrayOf(permission))
                                },
                                onGoToAppSettingsClick = ::openAppSettings,
                            )
                        }
                }
            }
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

private fun setProperties() {
    System.setProperty(
        "javax.xml.stream.XMLInputFactory",
        "com.fasterxml.aalto.stax.InputFactoryImpl"
    )
    System.setProperty(
        "javax.xml.stream.XMLOutputFactory",
        "com.fasterxml.aalto.stax.OutputFactoryImpl"
    )
    System.setProperty(
        "javax.xml.stream.XMLEventFactory",
        "com.fasterxml.aalto.stax.EventFactoryImpl"
    )
    System.setProperty(
        "org.apache.poi.javax.xml.stream.XMLInputFactory",
        "com.fasterxml.aalto.stax.InputFactoryImpl"
    )
    System.setProperty(
        "org.apache.poi.javax.xml.stream.XMLOutputFactory",
        "com.fasterxml.aalto.stax.OutputFactoryImpl"
    )
    System.setProperty(
        "org.apache.poi.javax.xml.stream.XMLEventFactory",
        "com.fasterxml.aalto.stax.EventFactoryImpl"
    )
}

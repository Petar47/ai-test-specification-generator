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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import dagger.hilt.android.AndroidEntryPoint
import hr.foi.aitsg.auth.getAllProjectUsers
import hr.foi.aitsg.auth.getAllUsers
import hr.foi.aitsg.auth.searchUsers
import hr.foi.aitsg.ui.theme.AITSGTheme
import hr.foi.database.DataViewModel
import hr.foi.database.User
import hr.foi.interfaces.TestRetriever
import hr.foi.scanner.ScannerTestRetriever
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.ui.platform.LocalContext


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<DataViewModel>()
    private val _showProject: ShowProject = ShowProject()
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AITSGTheme {
                val permissionViewModel = viewModel<PermissionViewModel>()
                val dialogQueue = permissionViewModel.visiblePermissionDialogQueue

                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = {perms ->
                            perms.keys.forEach{permission ->
                                permissionViewModel.onPermissionResult(
                                    permission = permission,
                                    isGranted = perms[permission] == true
                                )
                            }
                    }
                )
                //contains the type of the retriever: scanner, import -> users selects the type and then the factory creates the type class
                var testRetrieverType: String = "scanner"
                //contains the content of the test -> its use is to save the test when the app is navigating from scanner to preview
                var testContent: String = ""


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController, startDestination = "login"){
                        composable("login"){
                            LoginPage(navController = navController, dataViewModel = viewModel,
                                successfulLogin = {
                                navController.navigate("workspaces")
                            })

                        }
                        composable("register"){
                            RegistrationPage(navController = navController, viewModel = viewModel)
                        }
                        composable("addProject"){
                            AddProject(navController = navController,viewModel =viewModel)
                        }
                        composable("workspaces"){
                            ListofProjects(navController = navController,viewModel =viewModel)
                        }
                        composable("show-project/{id}" ){ navBackStack ->
                            val counter = navBackStack.arguments?.getString("id")
                                _showProject.showProject(
                                    navHostController = navController,
                                    dataViewModel = viewModel,
                                    project_id = counter)
                            }
                        composable("add-users/{id}" ){ navBackStack ->
                            val _project_id = navBackStack.arguments?.getString("id")
                            addUsersToProject(
                                navHostController = navController,
                                dataViewModel = viewModel,
                                project_id = _project_id)
                        }
                        composable("search-users/{id}" ){ navBackStack ->
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
                        composable("profile"){
                            UpdateProfile(navHostController = navController, viewModel = viewModel,
                                onUpdateUser = {
                                    Toast.makeText(
                                        applicationContext,
                                        "Ažurirani podaci korisnika",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        }
                        composable("menu"){
                            MenuPage(onMenuButtonClick =  {page ->
                                when(page){
                                    "workspaces" -> navController.navigate("workspaces")
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

                        composable("tests"){
                            multiplePermissionResultLauncher.launch(
                                arrayOf(
                                    android.Manifest.permission.CAMERA,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            )
                            testRetrieverType="upload"
                            //on report page when the user tries to create new report then he chooses the type and navigates here
                            val testRetriever: TestRetriever = TestRetrieverFactory.getRetriever(testRetrieverType)
                            Log.d("Test","testicc")
                            Column(){
                                testRetriever.showUI(getTestData = {testData ->
                                    testContent = testData
                                    navController.navigate("testPreview")
                                })
                            }

                            //TODO add to report page when the user chooses to scan the file with camera
                            //asks for permissions

                        }
                        composable("testPreview"){
                            TestPreviewPage(
                                testData = testContent,
                                onClickNext = {testData ->
                                    testContent = testData
                                    //TODO navigate to the report generation
                                },
                                onClickBack = {
                                    navController.navigate("tests")
                                }
                            )
                        }
                    }
                }

                //Permission handler
                dialogQueue
                    .reversed()
                    .forEach {permission ->
                        PermissionDialog(
                            permissionTextProvider = when (permission) {
                                android.Manifest.permission.CAMERA -> CameraPermissionTextProvider()
                                android.Manifest.permission.READ_EXTERNAL_STORAGE -> CameraPermissionTextProvider()
                                else -> return@forEach
                                //TODO add more permissions if needed
                            },
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(permission),
                            onDismiss = permissionViewModel::dismissDialog,
                            onOkClick= {
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

fun Activity.openAppSettings(){
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

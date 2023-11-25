package hr.foi.aitsg

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import dagger.hilt.android.AndroidEntryPoint
//import hr.foi.aitsg.Manifest.*
import hr.foi.aitsg.ui.theme.AITSGTheme
import hr.foi.database.DataViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<DataViewModel>()

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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController, startDestination = "scanner"){
                        composable("login"){
                            LoginPage(navController = navController, dataViewModel = viewModel, successfulLogin = {
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
                                //TODO implement logout
                                Toast.makeText(
                                    applicationContext,
                                    "Log Out",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Authenticated.loggedInUser = null
                                navController.navigate("login")
                            }, onReturnButtonClick = {
                                navController.popBackStack()
                            }, onEditProfileButtonClick = {
                                //TODO navigate to the profile page
                                navController.navigate("profile")
                            })
                        }

                        composable("scanner"){
                            Button(
                                onClick = {
                                    //launch permission requests if any
                                    multiplePermissionResultLauncher.launch(
                                        arrayOf(
                                            android.Manifest.permission.CAMERA,
                                            //TODO add if more
                                        )
                                    )
                                }
                            ){
                                Text("Skeniraj")
                            }
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
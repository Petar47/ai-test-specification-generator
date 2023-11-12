package hr.foi.aitsg

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.activity.viewModels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import dagger.hilt.android.AndroidEntryPoint
import hr.foi.aitsg.ui.theme.AITSGTheme
import hr.foi.database.DataViewModel
import hr.foi.menu.MenuScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<DataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AITSGTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController, startDestination = "menu"){
                        composable("login"){
                            //TODO add login page and redirections when finished
                            Column(modifier = Modifier.fillMaxSize()){
                                Text("Login")
                            }
                        }
                        composable("register"){
                            //TODO add register page and redirections when finished
                            Text("Register")
                        }
                        composable("workspaces"){

                            PrikazProjekata(navController)

                        }
                        composable("profile"){
                            //TODO add workspaces page and redirections when finished
                            Text("Profile", color = MaterialTheme.colorScheme.primary)
                        }
                        composable("menu"){
                            //TODO add menu page and redirections when finished
                            MenuScreen("workspaces", onMenuButtonClick =  {page ->
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
                            }, onReturnButtonClick = {page ->
                                navController.navigate(page)
                            }, onEditProfileButtonClick = {
                                //TODO navigate to the profile page
                                navController.navigate("profile")
                            })
                        }
                    }
                }
            }
        }
    }
}
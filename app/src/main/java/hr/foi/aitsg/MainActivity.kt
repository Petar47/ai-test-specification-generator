package hr.foi.aitsg

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hr.foi.aitsg.ui.theme.AITSGTheme
import hr.foi.menu.MenuScreen

class MainActivity : ComponentActivity() {
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
                            //TODO add workspaces page and redirections when finished
                            Text("Projects")
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
                            })
                        }
                    }
                }
            }
        }
    }
}
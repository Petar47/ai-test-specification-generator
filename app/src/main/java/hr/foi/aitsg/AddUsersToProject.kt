package hr.foi.aitsg

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import hr.foi.database.DataViewModel
import androidx.compose.material3.SearchBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.foi.database.User
import hr.foi.aitsg.auth.getAllUsers

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addUsersToProject(navHostController: NavHostController, dataViewModel: DataViewModel, project_id : String?) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var users : List<User> = getAllUsers(dataViewModel)
    Scaffold {
        Column (){
            Text(text = "$project_id")
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = text,
                onQueryChange = {
                    text = it
                } ,
                onSearch = {
                    active = false
                },
                active = active,
                onActiveChange = {
                    active = it
                },
                placeholder = {
                    Text(text = "Pretraži korisnike")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                trailingIcon = {
                    if(active){
                        Icon(
                            modifier = Modifier.clickable {
                              if(text.isNotEmpty()){
                                  text = ""
                              }
                              else{
                                  active = false
                              }
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon")
                    }

                })
                {
                    users.forEach{
                        Row(modifier = Modifier.padding(all = 14.dp)){
                            Text(text = it.email)
                        }
                    }
                }
        }
    }
}


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
    var id_project = project_id!!.substring(1).split("}")[0].toInt()
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var users : List<User> = getAllUsers(dataViewModel)
    var matchingUsers = mutableListOf<User>()
    Scaffold {
        Column (){
            Text(text = "$id_project")
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = text,
                onQueryChange = {
                    text = it
                    matchingUsers.clear()
                    users.forEach{user->
                        val pattern = Regex("$it")
                        if(pattern.containsMatchIn("${user.email}"))
                        {
                            matchingUsers.add(user)
                        }
                    }
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
                    matchingUsers.forEach{
                        Row(
                            modifier = Modifier
                                .padding(all = 14.dp)
                                .clickable {
                                    insertUser(it.id_user!!.toInt(), project_id = id_project)
                                }
                        ){
                            Text(text = it.email)
                        }
                    }
                }
        }
    }
}
fun insertUser(user_id:Int, project_id:Int){

}

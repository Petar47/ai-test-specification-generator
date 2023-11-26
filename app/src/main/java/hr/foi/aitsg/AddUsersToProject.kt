package hr.foi.aitsg

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import hr.foi.aitsg.auth.deleteUser
import hr.foi.database.User
import hr.foi.aitsg.auth.getAllUsers
import hr.foi.aitsg.auth.insertUser
import hr.foi.database.Project_user

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addUsersToProject(navHostController: NavHostController, dataViewModel: DataViewModel, project_id : String?) {
    var id_project = project_id!!.substring(1).split("}")[0].toInt()
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var users : List<User> = getAllUsers(dataViewModel)
    var matchingUsers = mutableListOf<User>()
    var add_user : Boolean by remember { mutableStateOf(false) }
    var user_id : Int by remember { mutableStateOf(0)}
    var delete_user_id : Int by remember {mutableStateOf(0)}
    if(add_user){
        insertUser( user_id, id_project, dataViewModel)
        add_user=false
        active=false
    }
    if(delete_user_id != 0 ){
        deleteUser(delete_user_id, id_project,dataViewModel)
    }
    Scaffold (
        modifier = Modifier.padding(5.dp)
    ){
        Column (){
            Text(text = "$id_project")
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = text,
                onQueryChange = {
                    text = it
                    matchingUsers.clear()
                    users.forEach{ user->
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
                        ){
                            Text(text = it.email)
                            Box (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.BottomEnd
                            ){
                                Button(onClick = {
                                    user_id = it.id_user!!
                                    add_user = true
                                })
                                {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = "AddUser")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )
                {
                    Text(text = "Dodani korisnici",
                        Modifier.height(30.dp),
                        color = MaterialTheme.colorScheme.tertiary)
                    users.forEach(){
                        Row (modifier = Modifier
                            .padding(all = 5.dp)
                            .background(
                                MaterialTheme.colorScheme.tertiary,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(5.dp)
                        )
                        {
                            Text(text = it.email)
                            Box (modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    delete_user_id = it.id_user!!
                                },
                                contentAlignment = Alignment.BottomEnd)
                            {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "DeleteUserFromProject")
                            }
                        }
                    }
                }
            }
        }
    }
package hr.foi.aitsg.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import hr.foi.database.DataViewModel
import hr.foi.database.User

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun searchUsers(navHostController: NavHostController, dataViewModel: DataViewModel, id_project: String?) {
    var project_id = id_project!!.toInt()
    var users: List<User> = getAllUsers(dataViewModel)
    var user_id: Int by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var matchingUsers = mutableListOf<User>()
    var addUser by remember {mutableStateOf(false)}
    if (addUser) {
        insertUser(user_id, project_id, dataViewModel)
        addUser = false
    }
    Scaffold(
        modifier = Modifier.padding(5.dp)
    ) {
        Column() {
            Text(text = "$id_project")
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = text,
                onQueryChange = {
                    text = it
                    matchingUsers.clear()
                    users.forEach { user ->
                        val pattern = Regex("$it")
                        if (pattern.containsMatchIn("${user.email}")) {
                            matchingUsers.add(user)
                        }
                    }
                },
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
                    if (active) {
                        Icon(
                            modifier = Modifier.clickable {
                                if (text.isNotEmpty()) {
                                    text = ""
                                    navHostController.navigate("add-users/$id_project")
                                } else {
                                    active = false
                                }
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon"
                        )
                    }
                })
            {
                matchingUsers.forEach {
                    Row(
                        modifier = Modifier
                            .padding(all = 14.dp)
                    ) {
                        Text(text = it.email)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Button(onClick = {
                                addUser = true
                                user_id = it.id_user!!
                            })
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "AddUser"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
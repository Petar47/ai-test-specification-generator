package hr.foi.aitsg.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.foi.database.DataViewModel
import hr.foi.database.Project_user
import hr.foi.database.User

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun searchUsers(navHostController: NavHostController, dataViewModel: DataViewModel, id_project: String?, addedUserToProject: ()-> Unit) {
    var project_id = id_project!!.toInt()
    var users = getAllUsers(dataViewModel)
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var matchingUsers = mutableListOf<User>()

    Scaffold(
        modifier = Modifier.padding(5.dp)
    ) {
        Column() {
            Row {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back" ,
                    Modifier
                        .clickable {
                            navHostController.navigate("show-project/$id_project")
                            active = false
                        }
                        .size(50.dp)
                )
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(text="Dodaj korisnika",
                        Modifier.padding(16.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary)
                }
            }

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
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Button(onClick = {
                                dataViewModel.insertProjectUser(Project_user(project_id, it.id_user!!))
                                addedUserToProject()
                                navHostController.navigate("show-project/$id_project")
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
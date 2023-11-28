package hr.foi.aitsg.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@Composable
@SuppressLint("CoroutineCreationDuringComposition")
fun getAllProjectUsers(dataViewModel: DataViewModel, id_project: Int): List<User> {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var coroutine = rememberCoroutineScope()
    dataViewModel.getUserByProject(id_project)
    coroutine.async {
        dataViewModel.uiState.collectLatest { data ->
            when (data) {
                is APIResult.Error -> {
                    Log.e("Error Data", "error-get-all-pro-user")
                }
                APIResult.Loading -> {
                    Log.e("Loading Data", "loading-get-all-pro-users")
                }
                is APIResult.Success -> {
                    Log.e("Success Data", "success-get-all-pro-users")

                    val usersList = data.data as List<User>
                    users = usersList
                }
            }
        }
    }
    Log.e("Success Data", users.toString())
    return users
}

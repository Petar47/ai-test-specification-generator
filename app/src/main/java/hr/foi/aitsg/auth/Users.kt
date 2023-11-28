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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun getAllUsers(dataViewModel: DataViewModel): List<User> {
    val coroutine = rememberCoroutineScope()
    var users = mutableListOf<User>()
    dataViewModel.getAllUsers()
    coroutine.async {
        dataViewModel.uiState.collect { data ->
            when (data) {
                is APIResult.Error -> {
                    Log.e("Error Data", "error-getallusers")
                }
                APIResult.Loading -> {
                    Log.e("Loading Data", "loading-getallusers")
                }
                is APIResult.Success -> {
                    val usersList = data.data as List<User>
                    for (user in usersList){
                        users.add(user)
                    }
                    Log.e("Success getallusers", users.toString())
                }
            }
        }
    }
    return users
}

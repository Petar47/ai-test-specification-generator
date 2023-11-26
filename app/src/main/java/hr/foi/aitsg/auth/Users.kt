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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun getAllUsers(dataViewModel: DataViewModel): List<User> {
    val coroutine = rememberCoroutineScope()
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    dataViewModel.getAllUsers()
    coroutine.launch {
        dataViewModel.uiState.collect { data ->
            when (data) {
                is APIResult.Error -> {
                    Log.e("Error Data", "error")
                }
                APIResult.Loading -> {
                    Log.e("Error Data", "loading")
                }
                is APIResult.Success -> {
                    users = data.data as List<User>
                }
            }
        }
    }
    return users
}

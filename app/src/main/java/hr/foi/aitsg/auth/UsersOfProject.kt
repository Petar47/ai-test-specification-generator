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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@SuppressLint("CoroutineCreationDuringComposition")
fun getAllProjectUsers(dataViewModel: DataViewModel, id_project: Int, coroutine:CoroutineScope): List<User> {
    var users : List<User> = emptyList()
    dataViewModel.getUserByProject(id_project)
    coroutine.launch {
        dataViewModel.uiState.collect { data ->
            when (data) {
                is APIResult.Error -> {
                    Log.e("Error Data", "error-get-all-users")
                }
                APIResult.Loading -> {
                    Log.e("Loading Data", "loading-get-all-users")
                }
                is APIResult.Success -> {
                    users = data.data as List<User>
                }
            }
        }
    }
    return users
}

package hr.foi.aitsg.auth

import android.util.Log
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.User

suspend fun getAllUsers(dataViewModel: DataViewModel, onUsersFetched: (List<User>) -> Unit) {
    dataViewModel.getAllUsers()
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
                onUsersFetched(usersList)
                Log.e("Success getallusers", usersList.toString())
            }
        }
    }
}

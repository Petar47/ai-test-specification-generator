package hr.foi.aitsg.auth

import android.util.Log
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.User
import hr.foi.database.UserReportsWithProjects

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
                if (data.data is List<*>) {
                    val dataList = data.data as List<*>
                    if (dataList.isNotEmpty() && dataList[0] is User) {
                        val usersList = data.data as List<User>
                        onUsersFetched(usersList)
                        Log.e("Success getallusers", usersList.toString())
                    }
                }
            }
        }
    }
}

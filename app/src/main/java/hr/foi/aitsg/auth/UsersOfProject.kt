package hr.foi.aitsg.auth

import android.util.Log
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.User
suspend fun getAllProjectUsers(dataViewModel: DataViewModel, id_project: Int, onUsersFetched: (List<User>) -> Unit) {
    dataViewModel.getUserByProject(id_project)
    dataViewModel.uiState.collect { data ->
        when (data) {
            is APIResult.Error -> {
                Log.e("Error Data", "error-get-all-pro-user")
            }
            APIResult.Loading -> {
                Log.e("Loading Data", "loading-get-all-pro-users")
            }
            is APIResult.Success -> {
                val usersList = data.data as List<User>
                onUsersFetched(usersList)
                Log.e("Success getallprousers", "success-get-all-pro-users")
            }
        }
    }
}

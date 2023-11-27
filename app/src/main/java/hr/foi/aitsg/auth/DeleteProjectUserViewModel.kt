package hr.foi.aitsg.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.Project_user
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DeleteProjectUserViewModel: ViewModel() {

    @SuppressLint("CoroutineCreationDuringComposition")

    fun deleteUser(user_id:Int, project_id:Int, dataViewModel: DataViewModel, coroutine: CoroutineScope){
        var project_user = Project_user(project_id, user_id)
        dataViewModel.deleteProjectUser(project_user)
        coroutine.launch {
            dataViewModel.uiState.collectLatest { data ->
                when (data) {
                    is APIResult.Error -> {
                        Log.e("Error Data", " project-user-error")
                    }
                    APIResult.Loading -> {
                        Log.e("Error Data", "project-user-loading")
                    }
                    is APIResult.Success -> {
                        Log.e("Successful update", "project-user-success")
                    }
                }
            }
        }
    }
}
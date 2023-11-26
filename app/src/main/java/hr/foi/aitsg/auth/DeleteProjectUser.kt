package hr.foi.aitsg.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.Project_user
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun deleteUser(user_id:Int, project_id:Int, dataViewModel: DataViewModel){
    val coroutine = rememberCoroutineScope()
    var project_user = Project_user(project_id, user_id)
    dataViewModel.deleteProjectUser(project_user)
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
                    Log.e("Successful update", "success")
                }
            }
        }
    }
}
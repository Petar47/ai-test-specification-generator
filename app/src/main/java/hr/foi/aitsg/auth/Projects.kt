package hr.foi.aitsg.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.Project
import hr.foi.database.Report
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@SuppressLint("CoroutineCreationDuringComposition")
fun getProjects(
    dataViewModel: DataViewModel,
    id_user: Int,
    coroutine: CoroutineScope
): List<Project> {
    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    //var projects: List<Project> = emptyList()
    LaunchedEffect(key1 = id_user) {
        dataViewModel.getProjects(id_user)
        coroutine.launch {
            dataViewModel.uiState.collectLatest { data ->
                when (data) {
                    is APIResult.Error -> {
                        Log.e("Error Data - getProjects", "error getAllProject")
                    }

                    APIResult.Loading -> {
                        Log.e("Loading Data - getProjects", "loading getAllProject")
                    }

                    is APIResult.Success -> {
                        if(data.data is List<*>) {
                            val dataList = data.data as List<*>
                            if (dataList.isNotEmpty() && dataList[0] is Project) {
                                projects = data.data as List<Project>
                            }
                        }
                        Log.e("Success - getProjects", projects.toString())
                    }
                }
            }
        }
    }
    return projects
}
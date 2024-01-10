package hr.foi.aitsg

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
import hr.foi.database.Project
import hr.foi.database.Report
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@SuppressLint("CoroutineCreationDuringComposition")
fun getNumberOfProjectReports(dataViewModel: DataViewModel, projectId : Int) : Int {
    var counter : Int = 0
    var coroutine = rememberCoroutineScope()
    dataViewModel.noOfProjectReports(projectId)
    coroutine.launch {
        dataViewModel.uiState.collectLatest {data ->
            when (data) {
                is APIResult.Error -> {
                    Log.e("Error Data - getNumberOfProjectReports", "error getNumberOfProjectReports: ${data.message}")
                }
                APIResult.Loading -> {
                    Log.d("Loading Data - getNumberOfProjectReports", "loading getNumberOfProjectReports")
                }
                is APIResult.Success -> {
                    counter = data.data as Int
                    Log.d("Success Data", counter.toString())
                }
            }
        }
    }
    return counter
}
@Composable
@SuppressLint("CoroutineCreationDuringComposition")
fun getNumberOfAllUserReports(dataViewModel: DataViewModel): Map<Project, Int> {
    var countOfProjectReports by remember { mutableStateOf<Map<Project,Int>>(emptyMap()) }
    var coroutine = rememberCoroutineScope()
    dataViewModel.noOfAllReports(Authenticated.loggedInUser?.id_user!!)
    coroutine.launch {
        dataViewModel.uiState.collectLatest {data ->
            when (data) {
                is APIResult.Error -> {
                    Log.e("Error Data - allUserReportsCount", "error allUserReportsCount: ${data.message}")
                }
                APIResult.Loading -> {
                    Log.d("Loading Data - allUserReportsCount", "loading allUserReportsCount")
                }
                is APIResult.Success -> {
                    countOfProjectReports = data.data as Map<Project, Int>
                    Log.d("Success Data - allUserReportsCount", countOfProjectReports.toString())
                }
            }
        }
    }
    return countOfProjectReports
}
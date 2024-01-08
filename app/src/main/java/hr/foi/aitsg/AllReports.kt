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
import hr.foi.database.Report
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@SuppressLint("CoroutineCreationDuringComposition")
fun getAllReports(dataViewModel: DataViewModel): List<Report> {
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    var coroutine = rememberCoroutineScope()
        //dataViewModel.getAllReports
        coroutine.launch {
            dataViewModel.uiState.collectLatest { data ->
                when (data) {
                    is APIResult.Error -> {
                        Log.e("Error Data - getAllReports", "error getAllReports: ${data.message}")
                    }
                    APIResult.Loading -> {
                        Log.d("Loading Data - getAllReports", "loading getAllReports")
                    }
                    is APIResult.Success -> {
                        if (data.data is List<*>) {
                            val dataList = data.data as List<*>
                            if (dataList.isNotEmpty() && dataList[0] is Report) {
                                reports = data.data as List<Report>
                            }
                        }
                    }
                }
            }
        }
    return reports
}
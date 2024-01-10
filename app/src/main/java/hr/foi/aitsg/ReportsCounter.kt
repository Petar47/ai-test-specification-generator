package hr.foi.aitsg

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import hr.foi.database.DataViewModel
import hr.foi.database.Project

@SuppressLint("SuspiciousIndentation")
@Composable
fun CountReportsOfProject(dataViewModel : DataViewModel, id_project: Int) : Int{
  var numberOfProjectReports = getNumberOfProjectReports(dataViewModel, id_project)
  return numberOfProjectReports
 }
@Composable
fun CountAllReports(dataViewModel : DataViewModel): Map<String, Int> {
  var projectsReportsCount = getNumberOfAllUserReports(dataViewModel)
  var projectNameReportsCount = mutableMapOf<String, Int>()
  for (project in projectsReportsCount){
    projectNameReportsCount[project.key.name] = project.value
  }
  return projectNameReportsCount
}
package hr.foi.aitsg

import android.util.Log
import androidx.compose.runtime.Composable
import hr.foi.database.DataViewModel

@Composable
fun CountReportsOfProject(dataViewModel : DataViewModel, id_project: Int) : Int{
  var numberOfProjectReports = getNumberOfProjectReports(dataViewModel, id_project)
    Log.e("Number of project reports ", numberOfProjectReports.toString() )
    return numberOfProjectReports
 }
@Composable
fun CountAllReports(dataViewModel : DataViewModel):Int {
  var numberOfAllUserProjectReports = getNumberOfAllUserReports(dataViewModel)
  return numberOfAllUserProjectReports
}
package hr.foi.aitsg

import androidx.compose.runtime.Composable
import hr.foi.aitsg.auth.getAllProjectReports
import hr.foi.database.DataViewModel

@Composable
fun CountReportsOfProject(dataViewModel : DataViewModel, id_project: Int) : Int{
  var reports = getAllProjectReports(dataViewModel, id_project)
  return reports.size
 }
@Composable
fun CountAllReports(dataViewModel : DataViewModel):Int {
  var allReports = getAllReports(dataViewModel)
  return allReports.size
}
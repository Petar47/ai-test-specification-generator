package hr.foi.aitsg

import hr.foi.database.Project

object SelectedProjects {
    var selectedProjects = mutableListOf<Project>()
    var selectedSort: SortTypes = SortTypes(1, "naziv - silazno")
}
data class SortTypes(
    val id: Int,
    val name: String
)
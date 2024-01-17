package hr.foi.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val repository: DataRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<APIResult<*>>(APIResult.Loading)
    val uiState: StateFlow<APIResult<*>>
        get() = _uiState

    fun deleteUser(id: Int){
        viewModelScope.launch {
            repository.deleteUser(id).collectLatest { data->
                _uiState.update { data }
            }
        }
    }
    fun getAllUsers(){
        viewModelScope.launch {
            repository.findAllUsers().collectLatest { data->
                _uiState.update { data }
            }
        }
    }
    fun updateUser(id: Int, user: User){
        viewModelScope.launch {
            repository.updateUser(id, user).collectLatest { data->
                _uiState.update { data }
            }
        }
    }
    fun insertUser(user: User){
        viewModelScope.launch {
            repository.insertUser(user).collectLatest {
                data -> _uiState.update { data }
            }
        }
    }
    fun getUserByEmail(email: String){
        viewModelScope.launch {
            repository.findUserByEmail(email).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun getProjects(id: Int){
        viewModelScope.launch {
            repository.findProjects(id).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun insertProject(project: Project){
        viewModelScope.launch {
            repository.insertProject(project).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun deleteProject(id: Int){
        viewModelScope.launch {
            repository.deleteProject(id).collectLatest { data->
                _uiState.update { data }
            }
        }
    }
    fun insertProjectUser(projectUser: Project_user){
        viewModelScope.launch {
            repository.insertProjectUser(projectUser).collectLatest { data ->
                // ui state update freezes the app. The update is unnecesary because the app navigates to another page
                //_uiState.update { data }
            }
        }
    }
    fun deleteProjectUser(projectUser: Project_user){
        viewModelScope.launch {
            repository.deleteProjectUser(projectUser).collectLatest { data ->
                //_uiState.update { data }
            }
        }
    }
    fun insertReport(report: Report){
        viewModelScope.launch {
            repository.insertReport(report).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun deleteReport(id: Int){
        viewModelScope.launch {
            repository.deleteReport(id).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun getAllReports(project_id: Int){
        viewModelScope.launch {
            repository.getAllReports(project_id).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun getUserByProject(projectId: Int){
        viewModelScope.launch {
            repository.getUserByProject(projectId).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun noOfProjectReports(projectId: Int) {
        viewModelScope.launch {
            repository.noOfProjectReports(projectId).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun getUserReports(userId: Int){
        viewModelScope.launch {
            repository.getUserReports(userId).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun updateProject(projectId: Int, project: Project){
        viewModelScope.launch {
            repository.updateProject(projectId, project).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun noOfAllReports(userId: Int){
        viewModelScope.launch {
            repository.noOfAllReports(userId).collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
    fun getUserReportsWithProject(userId: Int) {
        viewModelScope.launch {
            repository.getUserReportsWithProject(userId).collectLatest {data->
                _uiState.update { data }
            }
        }
    }
}
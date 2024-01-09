package hr.foi.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface DataRepository {
    fun findAllUsers(): Flow<APIResult<List<User>>>
    fun updateUser(id: Int, user: User): Flow<APIResult<Unit>>
    fun insertUser(user: User): Flow<APIResult<Unit>>
    fun deleteUser(id: Int): Flow<APIResult<Unit>>
    fun findUserByEmail(email: String): Flow<APIResult<User>>
    fun findProjects(id: Int): Flow<APIResult<List<Project>>>
    fun insertProject(project: Project): Flow<APIResult<Unit>>
    fun deleteProject(id: Int): Flow<APIResult<Unit>>

    fun insertProjectUser(projectUser: Project_user):Flow<APIResult<Unit>>
    fun deleteProjectUser(projectUser: Project_user): Flow<APIResult<Unit>>
    fun insertReport(report: Report): Flow<APIResult<Unit>>
    fun deleteReport(reportId :Int):Flow<APIResult<Unit>>
    fun getAllReports(projectId: Int): Flow<APIResult<List<Report>>>
    fun getUserByProject(projectId: Int): Flow<APIResult<List<User>>>
    fun noOfProjectReports(projectId: Int): Flow<APIResult<Int>>
    /*fun noOfAllReports(userId: Int): Flow<APIResult<Map<String, Int>>>*/
    fun getUserReports(userId: Int): Flow<APIResult<List<Report>>>
    fun updateProject(projectId: Int, project: Project): Flow<APIResult<Unit>>
}

class DefaultDataRepository @Inject constructor(
    private val dataSource: UserDataSource
): DataRepository {
    override fun findAllUsers(): Flow<APIResult<List<User>>> {
        return dataSource.findAllUsers()
    }

    override fun updateUser(id: Int, user: User): Flow<APIResult<Unit>> {
        return dataSource.updateUser(id, user)
    }

    override fun insertUser(user: User): Flow<APIResult<Unit>> {
        return dataSource.insertUser(user)
    }

    override fun deleteUser(id: Int): Flow<APIResult<Unit>> {
        return dataSource.deleteUser(id)
    }

    override fun findUserByEmail(email: String): Flow<APIResult<User>> {
        return dataSource.findUserByEmail(email)
    }

    override fun findProjects(id: Int): Flow<APIResult<List<Project>>> {
        return dataSource.findProjects(id)
    }

    override fun insertProject(project: Project): Flow<APIResult<Unit>> {
        return dataSource.insertProject(project)
    }

    override fun deleteProject(id: Int): Flow<APIResult<Unit>> {
        return dataSource.deleteProject(id)
    }

    override fun insertProjectUser(projectUser: Project_user): Flow<APIResult<Unit>> {
        return dataSource.insertProjectUser(projectUser)
    }

    override fun deleteProjectUser(projectUser: Project_user): Flow<APIResult<Unit>> {
        return dataSource.deleteProjectUser(projectUser)
    }

    override fun insertReport(report: Report): Flow<APIResult<Unit>> {
        return dataSource.insertReport(report)
    }

    override fun deleteReport(reportId: Int): Flow<APIResult<Unit>> {
        return dataSource.deleteReport(reportId)
    }

    override fun getAllReports(projectId: Int): Flow<APIResult<List<Report>>> {
        return dataSource.getAllReports(projectId)
    }

    override fun getUserByProject(projectId: Int): Flow<APIResult<List<User>>> {
        return dataSource.getUserByProject(projectId)
    }

    override fun noOfProjectReports(projectId: Int): Flow<APIResult<Int>> {
        return dataSource.noOfProjectReports(projectId)
    }

    override fun getUserReports(userId: Int): Flow<APIResult<List<Report>>> {
        return dataSource.getUserReports(userId)
    }

    override fun updateProject(projectId: Int, project: Project): Flow<APIResult<Unit>> {
        return dataSource.updateProject(projectId, project)
    }

    /*override fun noOfAllReports(userId: Int): Flow<APIResult<Map<String, Int>>> {
        return dataSource.noOfAllReports(userId)
    }*/


}
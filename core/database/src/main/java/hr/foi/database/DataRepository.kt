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


}
package hr.foi.database

import android.service.autofill.UserData
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.FilterOperator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val supabaseClient: SupabaseClient) {

    fun deleteUser(userId: Int): Flow<APIResult<Unit>> {
        return flow{
            emit(APIResult.Loading)
            try{
                supabaseClient.postgrest["User"].delete{
                    filter("id", FilterOperator.EQ, userId)
                }
                emit(APIResult.Success(Unit))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun findAllUsers(): Flow<APIResult<List<User>>>{
        return flow{
            emit(APIResult.Loading)
            try{
                val res = supabaseClient.postgrest["User"].select()
                    val users =res.decodeList<User>()
                emit(APIResult.Success(users))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun findUserByEmail(email: String): Flow<APIResult<User>>{
        return flow{
            emit(APIResult.Loading)
            try{
                val res = supabaseClient.postgrest["User"].select{
                    filter("email", FilterOperator.EQ, email)
                }
                val user =res.decodeSingle<User>()
                emit(APIResult.Success(user))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun insertUser(user: User): Flow<APIResult<Unit>>{
        return flow{
            emit(APIResult.Loading)
            try{
                supabaseClient.postgrest["User"].insert(user){
                }
                emit(APIResult.Success(Unit))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun updateUser(userId: Int, user: User): Flow<APIResult<Unit>> {
        return flow{
            emit(APIResult.Loading)
            try{
                supabaseClient.postgrest["User"].update(user){
                    filter("id_user", FilterOperator.EQ, userId)
                }
                emit(APIResult.Success(Unit))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun findProjects(userId: Int): Flow<APIResult<List<Project>>>{
        return flow {
            emit(APIResult.Loading)
            try{
                val projectList = mutableListOf<Project>()
                val res = supabaseClient.postgrest["Project"].select{
                    filter("owner", FilterOperator.EQ, userId)
                }
                val projects =res.decodeList<Project>()
                for(pr in projects){
                    projectList.add(pr)
                }
                val projectIdList = supabaseClient.postgrest["Project_user"].select{
                    filter("id_user", FilterOperator.EQ, userId)
                }
                val projectGuest = projectIdList.decodeList<Project_user>()
                for (int in projectGuest){
                    val pr = supabaseClient.postgrest["Project"].select {
                        filter("id_project", FilterOperator.EQ, int.id_project)
                    }.decodeSingle<Project>()
                    projectList.add(pr)
                }
                emit(APIResult.Success(projectList))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun insertProject(project: Project): Flow<APIResult<Unit>>{
        return flow{
            emit(APIResult.Loading)
            try{
                supabaseClient.postgrest["Project"].insert(project){
                }
                emit(APIResult.Success(Unit))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun deleteProject(projectId: Int): Flow<APIResult<Unit>>{
        return flow{
            emit(APIResult.Loading)
            try{
                supabaseClient.postgrest["Project"].delete{
                    filter("id_project", FilterOperator.EQ, projectId)
                }
                emit(APIResult.Success(Unit))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun insertProjectUser(projectUser: Project_user): Flow<APIResult<Unit>>{
        return flow{
            emit(APIResult.Loading)
            try{
                supabaseClient.postgrest["Project_user"].insert(projectUser){
                }
                emit(APIResult.Success(Unit))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun deleteProjectUser(projectUser: Project_user): Flow<APIResult<Unit>>{
        return flow{
            emit(APIResult.Loading)
            try{
                supabaseClient.postgrest["Project_user"].delete{
                    and {
                        Project_user::id_project eq projectUser.id_project
                        Project_user::id_user eq projectUser.id_user
                    }
                }
                emit(APIResult.Success(Unit))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun insertReport(report: Report): Flow<APIResult<Unit>>{
        return flow{
            emit(APIResult.Loading)
            try{
                supabaseClient.postgrest["Report"].insert(report){
                }
                emit(APIResult.Success(Unit))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun deleteReport(reportId :Int):Flow<APIResult<Unit>>{
        return flow{
            emit(APIResult.Loading)
            try{
                supabaseClient.postgrest["Report"].delete{
                    filter("id_report", FilterOperator.EQ, reportId)
                }
                emit(APIResult.Success(Unit))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun getAllReports(projectId: Int): Flow<APIResult<List<Report>>>{
        return flow {
            emit(APIResult.Loading)
            try{
                val res = supabaseClient.postgrest["Report"].select{
                    filter("id_project", FilterOperator.EQ, projectId)
                }
                val reports =res.decodeList<Report>()
                emit(APIResult.Success(reports))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
    fun getUserByProject(projectId: Int): Flow<APIResult<List<User>>>{
        return flow {
            emit(APIResult.Loading)
            try{
                val users = mutableListOf<User>()
                val projectUserList = supabaseClient.postgrest["Project_user"].select{
                    filter("id_project", FilterOperator.EQ, projectId)
                }
                val projectGuest = projectUserList.decodeList<Project_user>()
                for (int in projectGuest){
                    val pr = supabaseClient.postgrest["User"].select {
                        filter("id_user", FilterOperator.EQ, int.id_user)
                    }.decodeSingle<User>()
                    users.add(pr)
                }
                emit(APIResult.Success(users))
            }
            catch (e: Exception){
                emit(APIResult.Error(e.message))
            }
        }
    }
}

sealed class APIResult<out R>{
    data class Success<out R>(val data: R): APIResult<R>()
    data class Error(val message: String?): APIResult<Nothing>()
    object Loading: APIResult<Nothing>()
}
package hr.foi.database

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
                val res = supabaseClient.postgrest["Project"].select{
                    filter("owner", FilterOperator.EQ, userId)
                }
                val projects =res.decodeList<Project>()
                emit(APIResult.Success(projects))
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
}

sealed class APIResult<out R>{
    data class Success<out R>(val data: R): APIResult<R>()
    data class Error(val message: String?): APIResult<Nothing>()
    object Loading: APIResult<Nothing>()
}
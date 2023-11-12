package hr.foi.database

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id_user: Int? =null,
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String
)
@Serializable
data class Project(
    val id_project: Int? = null,
    val name: String,
    val created: String?=null,
    val owner: Int?
)
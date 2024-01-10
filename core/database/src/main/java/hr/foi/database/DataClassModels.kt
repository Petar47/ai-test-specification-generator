package hr.foi.database

import kotlinx.serialization.Serializable
import java.sql.Time

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

@Serializable
data class Report(
    val id_report: Int?=null,
    val name: String,
    val description: String?=null,
    val JSON_response: String,
    val generated: String?=null,
    val saved_time: String,
    val id_project: Int,
    val user: Int?

)

@Serializable
data class Project_user(
    val id_project: Int,
    val id_user: Int
)

@Serializable
data class ProjectReportTest(
    val id_report: Int,
    val Project: Project
)
@Serializable
data class UserProjectTest(
    val Project: Project,
    val id_user: Int
)
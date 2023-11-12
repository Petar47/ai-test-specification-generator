package hr.foi.menu

class MockUser {
    val name: String
    val surname: String
    val email: String

    constructor(name: String, surname: String, email: String) {
        this.name = name
        this.surname = surname
        this.email = email
    }

    fun getFullName() : String{
        return "${this.name} ${this.surname}"
    }
}
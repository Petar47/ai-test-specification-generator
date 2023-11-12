package hr.foi.authentication

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import hr.foi.models.User

class LoginHandler : ViewModel() {
    fun LogInUser ( email : String, password : String): User {
        // provjera u korisnika u bazi podataka
        val user = User("fe", "fegtr", "fweg");
        if(user != null){
            return user
        }
        else
        {
            return user
        }
    }
}
@Composable
public fun loginfe(){

}
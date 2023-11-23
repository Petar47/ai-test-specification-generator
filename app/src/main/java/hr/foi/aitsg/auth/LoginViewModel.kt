package hr.foi.aitsg.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import hr.foi.aitsg.Authenticated
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    var message : String = ""
    fun logInUser(dataViewModel: DataViewModel, email:String, password:String,successfulLogin: () -> Unit, coroutine: CoroutineScope) : String
    {
        var user : User? = null
        var hashPassword = getHashPassword(email,password)
        dataViewModel.getUserByEmail(email)
        coroutine.launch{
            dataViewModel.uiState.collectLatest { data ->
                when(data){
                    is APIResult.Error -> {
                        message="Korisnički račun ne postoji!"
                    }
                    APIResult.Loading -> {
                        Log.e("Error Data", "loading")
                    }
                    is APIResult.Success -> {
                        user = data.data as? User
                        if (user != null) {
                            if(hashPassword == user!!.password){
                                Authenticated.loggedInUser = user
                                successfulLogin()
                            }
                            else{
                                message="Neispravna lozinka!"
                            }
                        }
                    }
                }
            }
        }
        return message
    }
}
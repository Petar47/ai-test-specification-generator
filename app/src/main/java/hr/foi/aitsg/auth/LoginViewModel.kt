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
    var isLoading : Boolean = false
    fun logInUser(dataViewModel: DataViewModel, email:String, password:String, successfulLogin: () -> Unit, coroutine: CoroutineScope)
    {
        var user : User? = null
        var hashPassword = getHashPassword(email,password)

        dataViewModel.getUserByEmail(email)
        coroutine.launch{
            dataViewModel.uiState.collectLatest { data ->
                when(data){
                    is APIResult.Error -> {
                        this@LoginViewModel.message="Korisnički račun ne postoji!"
                    }
                    APIResult.Loading -> {
                        Log.e("Error Data", "loading")
                        this@LoginViewModel.isLoading = true
                    }
                    is APIResult.Success -> {
                        this@LoginViewModel.isLoading = false
                        user = data.data as? User
                        if (user != null) {
                            if(hashPassword == user!!.password){
                                Authenticated.loggedInUser = user
                                successfulLogin()
                            }
                            else{
                                this@LoginViewModel.message="Neispravna lozinka!"
                            }
                        }
                    }
                }
            }
        }
    }
}
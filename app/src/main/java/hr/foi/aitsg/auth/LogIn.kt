package hr.foi.aitsg.auth

import android.util.Log
import hr.foi.aitsg.Authenticated
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun logInUser(dataViewModel: DataViewModel,
              email:String, password:String,
              isLoading: (loading : Boolean) -> Unit,
              successfulLogin: () -> Unit,
              coroutine: CoroutineScope,
              updateMessage: (String) -> Unit)
{
    var user : User? = null
    var hashPassword = getHashPassword(email,password)

    dataViewModel.getUserByEmail(email)
    coroutine.launch{
        dataViewModel.uiState.collectLatest { data ->
            when(data){
                is APIResult.Error -> {
                    isLoading(false)
                    updateMessage("Korisnički račun ne postoji!")
                }
                APIResult.Loading -> {
                    Log.e("Error Data", "loading")
                    isLoading(true)
                }
                is APIResult.Success -> {
                    isLoading(false)
                    user = data.data as? User
                    if (user != null) {
                        if(hashPassword == user!!.password){
                            Authenticated.loggedInUser = user
                            successfulLogin()
                        }
                        else{
                            updateMessage("Neispravna lozinka!")
                        }
                    }
                }
            }
        }
    }
}

package hr.foi.authentication

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import hr.foi.database.DataViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.lifecycleScope
import hr.foi.database.User


class LoginHandler : ViewModel() {
    @Composable
    fun LogInUser ( email : String, password : String, viewModel: DataViewModel) {
        viewModel.getUserByEmail(email)
    }


}



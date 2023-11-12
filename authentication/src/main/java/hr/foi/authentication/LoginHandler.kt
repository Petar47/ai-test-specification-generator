package hr.foi.authentication

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import hr.foi.database.DataViewModel
import hr.foi.models.User
import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.lifecycleScope


class LoginHandler : ViewModel() {
    @Composable
    fun LogInUser ( email : String, password : String, viewModel: DataViewModel): User {
        viewModel.getUserByEmail(email)
        funkcija(viewModel)
    }

    private fun funkcija(viewModel: DataViewModel) {
        lifecycleScope.
    }
}



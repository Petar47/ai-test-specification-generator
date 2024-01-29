package hr.foi.aitsg

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.foi.aitsg.ui.theme.Cyan
import hr.foi.aitsg.ui.theme.Grey
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.security.MessageDigest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfile(navHostController: NavHostController, viewModel: DataViewModel, onUpdateUser: () -> Unit){
    var email by remember { mutableStateOf("") }
    var first_name by remember { mutableStateOf(Authenticated.loggedInUser!!.first_name)}
    var last_name by remember { mutableStateOf(Authenticated.loggedInUser!!.last_name)}
    var old_password by remember { mutableStateOf("") }
    var new_password by remember { mutableStateOf("") }
    var confirm_password by remember { mutableStateOf("") }
    email = Authenticated.loggedInUser!!.email.toString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center){
        Row (
            Modifier
                .height(100.dp)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "Ažuriranje profila",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = email, color = MaterialTheme.colorScheme.inversePrimary)
        OutlinedTextField(
            value = first_name,
            onValueChange = { first_name = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Ime") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Cyan,
                unfocusedBorderColor = Grey
            )
        )
        OutlinedTextField(
            value = last_name,
            onValueChange = { last_name = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Prezime") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Cyan,
                unfocusedBorderColor = Grey
            )
        )
        OutlinedTextField(
            value = old_password,
            onValueChange = { old_password = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Stara lozinka")},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                autoCorrect = false
            ),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = hr.foi.aitsg.ui.theme.Cyan,
                unfocusedBorderColor = hr.foi.aitsg.ui.theme.Grey)
        )
        OutlinedTextField(
            value = new_password,
            onValueChange = { new_password = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Nova lozinka")},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                autoCorrect = false
            ),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = hr.foi.aitsg.ui.theme.Cyan,
                unfocusedBorderColor = hr.foi.aitsg.ui.theme.Grey)
        )
        OutlinedTextField(
            value = confirm_password,
            onValueChange = { confirm_password = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Ponovi lozinku")},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                autoCorrect = false
            ),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = hr.foi.aitsg.ui.theme.Cyan,
                unfocusedBorderColor = hr.foi.aitsg.ui.theme.Grey)
        )
        var coroutine  = rememberCoroutineScope()
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                var user = Authenticated.loggedInUser
                var newUser: User
                var password: String
                password = MessageDigest.getInstance("SHA-256").digest(old_password.toByteArray())
                    .fold("", { str, it -> str + "%02x".format(it) })
                password += MessageDigest.getInstance("SHA-256").digest(email.toByteArray())
                    .fold("", { str, it -> str + "%02x".format(it) })
                password = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
                    .fold("", { str, it -> str + "%02x".format(it) })

                if ((user!!.password == password) && (new_password == confirm_password)) {
                    password =
                        MessageDigest.getInstance("SHA-256").digest(new_password.toByteArray())
                            .fold("", { str, it -> str + "%02x".format(it) })
                    password += MessageDigest.getInstance("SHA-256").digest(email.toByteArray())
                        .fold("", { str, it -> str + "%02x".format(it) })
                    password = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
                        .fold("", { str, it -> str + "%02x".format(it) })
                    newUser = User(user!!.id_user, user.email, password, first_name, last_name)
                } else {
                    newUser = User(user!!.id_user, user.email, user.password, first_name, last_name)
                }
                viewModel.updateUser(newUser.id_user!!.toInt(), newUser)
                coroutine.launch {
                    viewModel.uiState.collectLatest { data ->
                        when (data) {
                            is APIResult.Error -> {
                                Log.e("Error in", "data")
                            }

                            APIResult.Loading -> {
                                Log.e("Error in", "loading")
                            }

                            is APIResult.Success -> {
                                Authenticated.loggedInUser = newUser
                                onUpdateUser()
                                navHostController.popBackStack()
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(5.dp)
        )
        {
            Text("Ažuriraj")
        }
    }
}
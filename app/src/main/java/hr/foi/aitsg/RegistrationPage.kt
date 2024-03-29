package hr.foi.aitsg

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.User
import java.security.MessageDigest

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPage(navController: NavHostController, viewModel: DataViewModel) {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Registracija",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // First Name
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(stringResource(R.string.first_name)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Last Name
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(stringResource(R.string.last_name)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                autoCorrect = false

            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = passwordConfirm,
            onValueChange = { passwordConfirm = it },
            label = { Text(stringResource(R.string.passwordConfirm)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                autoCorrect = false

            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = message,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        // Register Button
        Button(
            onClick = {
                if(validateData(email, password, passwordConfirm)) {
                    password = MessageDigest.getInstance("SHA-256").digest(password.toByteArray()).fold("", { str, it -> str + "%02x".format(it) })
                    password += MessageDigest.getInstance("SHA-256").digest(email.toByteArray()).fold("", { str, it -> str + "%02x".format(it) })
                    password = MessageDigest.getInstance("SHA-256").digest(password.toByteArray()).fold("", { str, it -> str + "%02x".format(it) })
                    val user = User(null, email, password, firstName, lastName)
                    viewModel.insertUser(user)
                    navController.popBackStack()
                }
                else {
                    message = "Lozinka mora sadržavati najmanje 6 znakova, jedno veliko slovo, jedno malo slovo i jednu znamenku"
                    Log.e("Fail:", "Epic fail bro")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(5.dp)
        ) {
            Text(stringResource(R.string.register),
                fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            modifier = Modifier.clickable {
                navController.navigate("login")
            }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Već imaš račun? ", fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.inversePrimary)
            Text("Prijava", color = MaterialTheme.colorScheme.primary)
        }
    }
}

fun validateData(email: String, password: String, passwordConfirm: String):Boolean {
    return password.length >= 6 && password.contains(Regex("[a-z]")) && password.contains(Regex("[A-Z]")) && password.contains(Regex("[0-9]")) && password == passwordConfirm
}

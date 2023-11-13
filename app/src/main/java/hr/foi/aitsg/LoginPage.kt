package hr.foi.aitsg

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import hr.foi.authentication.LoginHandler
import hr.foi.database.APIResult
import hr.foi.database.DataViewModel
import hr.foi.database.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.security.MessageDigest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavHostController, viewModel: DataViewModel){
    var email by remember { mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var user : User
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    )
    {
        Row (
            Modifier
                .height(250.dp)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "Prijava",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
        }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("email")},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = hr.foi.aitsg.ui.theme.Cyan,
                unfocusedBorderColor = hr.foi.aitsg.ui.theme.Grey)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("lozinka")},
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = hr.foi.aitsg.ui.theme.Black,
                unfocusedBorderColor = hr.foi.aitsg.ui.theme.Cyan)
        )
        Text(text = message)
        val coroutine = rememberCoroutineScope()
        Button(
            onClick = {
                var hashPassword : String
                viewModel.getUserByEmail(email)
                coroutine.launch{
                    viewModel.uiState.collectLatest { data ->
                        when(data){
                            is APIResult.Error -> {
                                Log.e("Error data", "mess ${data}")
                            }
                            APIResult.Loading -> {
                                Log.e("Error Data", "loading")
                            }
                            is APIResult.Success -> {
                                val user = data.data as User
                                hashPassword = MessageDigest.getInstance("SHA-256").digest(password.toByteArray()).fold("", { str, it -> str + "%02x".format(it) })
                                hashPassword += MessageDigest.getInstance("SHA-256").digest(email.toByteArray()).fold("", { str, it -> str + "%02x".format(it) })
                                hashPassword = MessageDigest.getInstance("SHA-256").digest(hashPassword.toByteArray()).fold("", { str, it -> str + "%02x".format(it) })
                                if(hashPassword == user.password){
                                    Authenticated.loggedInUser = user
                                    navController.navigate("workspaces")
                                }
                                else{
                                    message = "Neispravna lozinka!"
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(5.dp)
        )
        {
            Text(text = "Prijava")
        }
        Row(
            Modifier
                .height(150.dp)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Nemate račun?",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                fontStyle = FontStyle.Italic
            )
        }
        Button(
            onClick = {
                navController.navigate("register")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
        {

            Text(text = "Registracija")
        }
    }

}


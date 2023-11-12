package hr.foi.aitsg

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
import hr.foi.database.DataViewModel
import hr.foi.database.User



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavHostController, viewModel: DataViewModel){
    var email by remember { mutableStateOf("ihorvat@gmail.com") }
    var password by remember{ mutableStateOf("test") }
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
        Button(
            onClick = {
                //user = LoginHandler().LogInUser(email, password, viewModel)
                //Authenticated.loggedInUser = user
                //navController.navigate("home")
                //viewModel.getUserByEmail(email)
                //funkcija(viewModel)
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


package hr.foi.menu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
public fun MenuScreen(menuScreenItem: String, onMenuButtonClick: (page: String) -> Unit, onLogOutButtonClick: () -> Unit, onReturnButtonClick: (page: String) -> Unit, onEditProfileButtonClick: () -> Unit) {
    val loggedInUser = MockUser("Pero", "Peric", "pperic@email.com") //TODO add logged in user object

    val menuItems = listOf(
        "workspaces" to "Projekti",
        "history" to "Povijest",
        "statistics" to "Statistika"
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(loggedInUser, onReturn = {
            onReturnButtonClick(menuScreenItem)
        }, onEdit = {
            onEditProfileButtonClick()
        })
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                    verticalArrangement = Arrangement.Center
            ) {
                items(menuItems) { (k, v) ->
                    val color = if (k == menuScreenItem) {
                        Color.Blue
                    } else { //TODO add colors from the theme
                        Color.Gray
                    }
                    MenuItem(text=v, color=color, icon = Icons.Default.Menu) { onMenuButtonClick(k.lowercase(Locale.getDefault())) }
                    Log.d("MenuScreen", "$k - $menuScreenItem")
                }
            }
        }


        BottomAppBar { onLogOutButtonClick() }
    }
}

@Composable
fun TopAppBar(user: MockUser, onReturn: () -> Unit, onEdit: () -> Unit){ //TODO replace with real user class
    Column (
        modifier = Modifier
            .background(Color.Red) //TODO add proper color
            .fillMaxWidth()
    ){
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            IconButton(onClick = { onReturn() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Return",
                    modifier = Modifier.size(45.dp),
                    tint = Color.White // TODO add proper color
                )
            }
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserInformation(user.getFullName())
                Spacer(modifier = Modifier.height(10.dp))
                UserInformation(user.email)
            }
        }
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            contentAlignment = Alignment.BottomEnd
        ){
            IconButton(onClick = { onEdit() }) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Edit profile",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White // TODO add proper color
                    )
            }
        }
    }
}

@Composable
fun UserInformation(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White //TODO add proper color
    )
}

@Composable
fun BottomAppBar(logOutEvent: () -> Unit){
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { logOutEvent() }
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Log Out",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Odjava",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun MenuItem(
    text: String,
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen("workspaces", {}, {}, {}, {})
}

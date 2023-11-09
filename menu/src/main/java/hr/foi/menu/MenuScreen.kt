package hr.foi.menu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
public fun MenuScreen(menuScreenItem: String, onMenuButtonClick: (page: String) -> Unit) {
    val loggedInUser = "Testni korisnik" //TODO add logged in user object

    val menuItems = listOf(
        "workspaces" to "Projekti",
        "history" to "Povijest",
        "statistics" to "Statistika",
        "profile" to "Profil"
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(loggedInUser)
        Column(
            modifier = Modifier.fillMaxSize()
            .weight(1f)
            .padding(16.dp)
        ) {


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                items(menuItems) { (k, v) ->
                    val color = if (k == menuScreenItem) {
                        Color.Blue
                    } else { //TODO add colors from the theme
                        Color.Gray
                    }
                    MenuItem(text=v, color=color, icon = Icons.Default.Menu) { onMenuButtonClick(v.lowercase(Locale.getDefault())) }
                    Log.d("MenuScreen", "$k - $menuScreenItem")
                }
            }
        }


        BottomAppBar()
    }
}

@Composable
fun TopAppBar(user: String){
    Text(text = user, modifier = Modifier.padding(end = 16.dp))
    //TODO add user information
}

@Composable
fun BottomAppBar(){
    IconButton(
        onClick = {  } //TODO add logout handler
    ) {
        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Log Out")
    }
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
            .height(150.dp)
            .padding(16.dp)
            .clickable { onClick() }

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
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
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen("workspaces", {})
}

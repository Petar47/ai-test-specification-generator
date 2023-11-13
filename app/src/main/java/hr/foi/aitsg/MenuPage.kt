package hr.foi.aitsg



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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.foi.database.User
import java.util.Locale

@Composable
public fun MenuPage(menuScreenItem: String, onMenuButtonClick: (page: String) -> Unit, onLogOutButtonClick: () -> Unit, onReturnButtonClick: (page: String) -> Unit, onEditProfileButtonClick: () -> Unit) {
    val loggedInUser = Authenticated.loggedInUser as User

    val menuItems = listOf(
        "workspaces" to listOf("Projekti", R.drawable.projects_icon),
        "history" to listOf("Povijest", R.drawable.history_icon),
        "statistics" to listOf("Statistika", R.drawable.statistics_icon)
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
                        MaterialTheme.colorScheme.onSecondary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }
                    MenuItem(text=v.first().toString(), color=color, icon = ImageVector.vectorResource(id = v.last().toString().toInt())) { onMenuButtonClick(k.lowercase(Locale.getDefault())) }
                    Log.d("MenuScreen", "$k - $menuScreenItem")
                }
            }
        }


        BottomAppBar { onLogOutButtonClick() }
    }
}

@Composable
fun TopAppBar(user: User, onReturn: () -> Unit, onEdit: () -> Unit){ //TODO replace with real user class
    Column (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
            .fillMaxWidth()
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){
            IconButton(onClick = { onReturn() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Return",
                    modifier = Modifier.size(45.dp),
                    tint = MaterialTheme.colorScheme.primary
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
                UserInformation("${user.first_name} ${user.last_name}")
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
                    tint = MaterialTheme.colorScheme.primary
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
        color = MaterialTheme.colorScheme.secondary
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
                modifier = Modifier.size(30.dp),
                tint = MaterialTheme.colorScheme.inversePrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Odjava",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inversePrimary
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
            defaultElevation = 6.dp
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
                color = MaterialTheme.colorScheme.primary,
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
fun MenuPagePreview() {
    MenuPage("workspaces", {}, {}, {}, {})
}

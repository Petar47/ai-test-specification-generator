package hr.foi.aitsg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.foi.database.User
import org.apache.poi.sl.usermodel.VerticalAlignment

@Composable
fun StatisticsPage(onMenuClick: () -> Unit){
    //val loggedInUser = Authenticated.loggedInUser as User
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        TopAppBar(onMenu = {onMenuClick()})
        SavedTimeGraph()
        NumberOfReports()
        ScanningFrequency()
    }
}

@Composable
fun TopAppBar(onMenu: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClick = { onMenu() }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(45.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Statistika",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SavedTimeGraph(){
    //TODO stupicasti sa ustedjenim vremenima po projektu
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Ušteđeno vrijeme",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inversePrimary
            )
            Text(text = "<ukupno ustedjeno vrijeme>")
        }
        //Graf
    }
}

@Composable
fun NumberOfReports(){
    //TODO Broj izvjesca po projektu, piechart
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Broj generiranih izvještaja",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }
        //graf
    }
}

@Composable
fun ScanningFrequency(){
    //TODO linijski graf sa ucestalosti skeniranja
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Učestalost skeniranja",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }
        //graf
    }
}
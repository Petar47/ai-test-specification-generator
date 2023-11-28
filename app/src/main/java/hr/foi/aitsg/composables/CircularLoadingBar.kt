package hr.foi.aitsg.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircularLoadingBar(){
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .size(110.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )

        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ){
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
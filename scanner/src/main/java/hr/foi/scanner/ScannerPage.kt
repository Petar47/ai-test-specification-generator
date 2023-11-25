package hr.foi.scanner

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ScannerPage(getTestData: (testData: String) -> Unit){
    Column {
        Text("Bok ovo je test")
        Button(onClick = {getTestData("Ovo je testData vracen putem interfacea")}) {
            Text("Klikni me")
        }
    }
}
package hr.foi.scanner

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ScannerPage(getTestData: (testData: String) -> Unit){
    Column {

        Button(onClick = {
            //TODO capture photo and scan the text convert it to string and forward it using the getTestData
            getTestData("Ovo je testData vracen putem interfacea")
        }) {
            Text("Scan")
        }
    }
}
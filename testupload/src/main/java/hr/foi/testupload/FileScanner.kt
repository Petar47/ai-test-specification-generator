package hr.foi.testupload

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import hr.foi.interfaces.Scanner
import hr.foi.testupload.R
import java.io.BufferedReader
import java.io.InputStreamReader

class FileScanner : Scanner{
    @Composable
    override fun TestRetrieverUI(getTestData: (data: String) -> Unit) {
        val context = LocalContext.current
        val contentResolver = context.contentResolver
        val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val inputStream = contentResolver.openInputStream(uri)
                val reader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
                val fileContent = stringBuilder.toString()
                reader.close()
                Log.d("Datoteka",fileContent)
                getTestData(fileContent)
            }
        }
        Button(onClick = { getContent.launch("*/*") }) {
            Text("Odaberi datoteku")
        }

    }

    override fun getRoute(): String {
        return "tests/upload/"
    }
    @Composable
    override fun getIcon(): Painter {
        return painterResource(id = R.drawable.upload)
    }
}


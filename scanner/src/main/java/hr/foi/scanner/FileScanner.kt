package hr.foi.scanner

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import hr.foi.interfaces.TestRetriever
import java.io.BufferedReader
import java.io.InputStreamReader

class FileScanner : TestRetriever {
    @Composable
    override fun showUI(getTestData: (data: String) -> Unit) {
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
                Log.d("Datoteka",fileContent)
                reader.close() }
        }

    }
}


package hr.foi.scanner

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

fun readAndProcessFile(contentResolver: ContentResolver, uri: Uri) {
    val inputStream = contentResolver.openInputStream(uri)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        stringBuilder.append(line).append("\n")
    }
    val fileContent = stringBuilder.toString()
    Log.d("Datoteka",fileContent)
    reader.close()
}


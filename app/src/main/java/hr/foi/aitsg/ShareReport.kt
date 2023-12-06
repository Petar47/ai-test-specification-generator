package hr.foi.aitsg

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import java.io.File

fun sendEmail(context : Context){

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            //type = "application/vnd.ms-excel"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("iva.kustura@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Subject")
            /*val filePath = File(context.filesDir, "Download/File.xlsx")
            val fileUri = Uri.fromFile(filePath)
            putExtra(Intent.EXTRA_STREAM, fileUri) */
        }
        if (emailIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(emailIntent)
        } else {
                Toast.makeText(context, "No email app installed", Toast.LENGTH_SHORT).show()
        }

    }

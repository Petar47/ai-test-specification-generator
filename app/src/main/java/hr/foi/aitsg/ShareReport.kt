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
    val generator = ReportGenerator()
    val mockJson = "{\n" +
            "  \"isTest\": true," +
            "  \"name\": \"GoogleTest_Cplusplus\"," +
            "  \"description\": \"This test assesses C++ programming proficiency with a series of coding challenges inspired by Google's standards.\"," +
            "  \"testSteps\": [" +
            "    {" +
            "      \"step\": 1," +
            "      \"instruction\": \"Implement a function 'calculateFactorial' that computes the factorial of a given non-negative integer.\"," +
            "      \"expectedOutcome\": \"The function should accurately calculate the factorial of the input integer.\"" +
            "    }," +
            "    {" +
            "      \"step\": 2," +
            "      \"instruction\": \"Create a class 'LinkedList' with methods 'insertNode' and 'deleteNode' to manipulate a singly linked list.\"," +
            "      \"expectedOutcome\": \"The 'insertNode' and 'deleteNode' methods should correctly modify the linked list as specified.\"" +
            "    }," +
            "    {" +
            "      \"step\": 3," +
            "      \"instruction\": \"Write a function 'findMissingNumber' to identify the missing number in an array containing integers from 1 to N.\"," +
            "      \"expectedOutcome\": \"The function should return the missing number from the given array.\"" +
            "    }," +
            "    {" +
            "      \"step\": 4," +
            "      \"instruction\": \"Implement a class 'MatrixMultiplier' with a method 'multiplyMatrices' that multiplies two matrices.\"," +
            "      \"expectedOutcome\": \"The 'multiplyMatrices' method should produce the correct result of matrix multiplication for the given input matrices.\"" +
            "    }" +
            "  ]" +
            "}"
        val workbook = generator.createWorkbook(mockJson)
        val cachedPath = generator.saveExcelToTempDirectory(workbook, context)
        val fileUri = cachedPath
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            //type = "text/plain"
            type = "application/vnd.ms-excel"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("iva.kustura@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Subject")
            putExtra(Intent.EXTRA_STREAM, fileUri)
            /*val filePath = File(context.filesDir, "Download/File.xlsx") */
            Toast.makeText(context, "$cachedPath", Toast.LENGTH_SHORT).show()

        }
        if (emailIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(emailIntent)
        } else {
                Toast.makeText(context, "No email app installed", Toast.LENGTH_SHORT).show()
        }

    }

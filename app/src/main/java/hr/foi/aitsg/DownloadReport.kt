package hr.foi.aitsg

import android.content.Context
import android.util.Log
import java.io.FileOutputStream

fun DownloadReport(context : Context, id : Int?, naziv : String){
    Log.e("pero","test")
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
    val fileOut = FileOutputStream("$naziv.xlsx")
    workbook.write(fileOut)
    fileOut.close()

    workbook.close()
    println("Datoteka je uspješno spremljena kao $naziv.xlsx")
}
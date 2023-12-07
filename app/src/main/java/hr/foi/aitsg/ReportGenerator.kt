package hr.foi.aitsg

import android.content.Context
import android.provider.SyncStateContract
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ReportGenerator {
    fun saveExcel(workbook: Workbook, fileName: String, context: Context){
        val subdirectoryName = "reports"
        val cachedFileName = "$fileName.xlsx"

        // Get the internal storage directory for your app's files
        val filesDir = context.filesDir

        // Makes a hidden temp directory for storing the bitmap
        val subdirectory = File(filesDir, subdirectoryName)
        if (!subdirectory.exists()) {
            subdirectory.mkdir()
        }

        // Gets the absolute path
        val cached_path = File(subdirectory, cachedFileName).absolutePath

        try{
            val fileOut = FileOutputStream(cached_path)
            workbook.write(fileOut)
            fileOut.close()
        } catch(e: FileNotFoundException){
            Log.e("Generator", "Error: ", e)
        } catch(e: IOException){
            Log.e("Generator", "Error: ", e)
        }


    }
    fun createWorkbook(): Workbook {
        val workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet()

        createSheetHeader(sheet)


        val mockJson = "{\n" +
                "  \"isTest\": true,\n" +
                "  \"name\": \"GoogleTest_Cplusplus\",\n" +
                "  \"description\": \"This test assesses C++ programming proficiency with a series of coding challenges inspired by Google's standards.\",\n" +
                "  \"testSteps\": [\n" +
                "    {\n" +
                "      \"step\": 1,\n" +
                "      \"instruction\": \"Implement a function 'calculateFactorial' that computes the factorial of a given non-negative integer.\",\n" +
                "      \"expectedOutcome\": \"The function should accurately calculate the factorial of the input integer.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"step\": 2,\n" +
                "      \"instruction\": \"Create a class 'LinkedList' with methods 'insertNode' and 'deleteNode' to manipulate a singly linked list.\",\n" +
                "      \"expectedOutcome\": \"The 'insertNode' and 'deleteNode' methods should correctly modify the linked list as specified.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"step\": 3,\n" +
                "      \"instruction\": \"Write a function 'findMissingNumber' to identify the missing number in an array containing integers from 1 to N.\",\n" +
                "      \"expectedOutcome\": \"The function should return the missing number from the given array.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"step\": 4,\n" +
                "      \"instruction\": \"Implement a class 'MatrixMultiplier' with a method 'multiplyMatrices' that multiplies two matrices.\",\n" +
                "      \"expectedOutcome\": \"The 'multiplyMatrices' method should produce the correct result of matrix multiplication for the given input matrices.\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"

        generateTable(mockJson, sheet)

        return workbook
    }

    fun createSheetHeader(sheet: Sheet){
        val row = sheet.createRow(0)
        val HEADER_LIST = listOf("Name","Description","Test Steps.Action","Test Steps.Expected result")

        for ((index, value) in HEADER_LIST.withIndex()) {
            val columnWidth = (15 * 500)
            sheet.setColumnWidth(index, columnWidth)
            val cell = row.createCell(index)
            cell?.setCellValue(value)
            //TODO add cell style if needed
        }
    }

    fun generateTable(jsonData: String, sheet: Sheet){
        val json = JSONObject(jsonData)
        val name = json.getString("name")
        val description = json.getString("description")
        val testStepsArray = json.getJSONArray("testSteps")
        val testSteps = mutableListOf<String>()
        for (i in 0 until testStepsArray.length()) {
            val stepObject = testStepsArray.getJSONObject(i)
            val step = stepObject.getInt("step")
            val stepDescription = stepObject.getString("instruction")
            val code = stepObject.getString("expectedOutcome")
            testSteps.add("$step!!!$stepDescription!!!$code")
        }
        val firstStep = testSteps.first().split("!!!")
        addData(0, listOf(name, description, firstStep[1], firstStep[2]), sheet)
        for (i in 1 until testSteps.size){
            val step = testSteps[i].split("!!!")
            addData(0, listOf("", "", step[1], step[2]), sheet)
        }
        //TODO adapt the parser to the agreed json format
    }
    fun addData(rowIndex: Int, values: List<String>,sheet: Sheet){
        val row = sheet.createRow(rowIndex)


        for ((index, value) in values.withIndex()) {
            createCell(row, index, value)
        }
    }

    fun createCell(row: Row, columnIndex: Int, value: String?){
        val cell = row.createCell(columnIndex)
        cell?.setCellValue(value)
    }
}
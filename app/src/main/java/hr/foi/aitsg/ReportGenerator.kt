package hr.foi.aitsg

import android.content.Context
import android.provider.SyncStateContract
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ReportGenerator {
    fun saveExcelToTempDirectory(workbook: Workbook, context: Context) : String{

        val subdirectoryName = ".reports"
        val cachedFileName = "tempReport.xlsx"

        // Get the internal storage directory for your app's files
        val filesDir = context.filesDir
        Log.d("Generator", "FilesDir created")

        // Makes a directory for storing the file
        val subdirectory = File(filesDir, subdirectoryName)
        if (!subdirectory.exists()) {
            subdirectory.mkdir()
        }
        Log.d("Generator", "Directory created")

        // Gets the absolute path
        val cached_path = File(subdirectory, cachedFileName).absolutePath
        Log.d("Generator", "Path created: $cached_path")

        try{
            val fileOut = FileOutputStream(cached_path)
            workbook.write(fileOut)
            Log.d("Generator", "Workbook written to the file")
            fileOut.close()
        } catch(e: FileNotFoundException){
            Log.e("Generator", "Error: ", e)
        } catch(e: IOException){
            Log.e("Generator", "Error: ", e)
        }

        return cached_path
    }
    fun deleteTempReport(path: String){
        val cachedFile = File(path)
        if (cachedFile.exists()) {
            cachedFile.delete()
        }
    }

    fun saveExcel(workbook: Workbook, fileName: String, path: String, context: Context) : String {
        val cached_path = File(path, fileName).absolutePath

        try{
            val fileOut = FileOutputStream(cached_path)
            workbook.write(fileOut)
            Log.d("Generator", "Workbook written to the file")
            fileOut.close()
        } catch(e: FileNotFoundException){
            Log.e("Generator", "Error: ", e)
        } catch(e: IOException){
            Log.e("Generator", "Error: ", e)
        }

        return cached_path
    }
    fun createWorkbook(json: String): Workbook {
        val workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet()
        val headerStyle = getHeaderStyle(workbook)
        createSheetHeader(sheet, headerStyle)

        /*
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

         */

        val bodyCellStyle = getBodyStyle(workbook)
        generateTable(json, sheet, bodyCellStyle)

        return workbook
    }

    private fun createSheetHeader(sheet: Sheet, cellStyle: CellStyle){
        val row = sheet.createRow(0)
        val HEADER_LIST = listOf("Name","Description","Test Steps.Action","Test Steps.Expected result")

        for ((index, value) in HEADER_LIST.withIndex()) {
            val columnWidth = (15 * 500)
            sheet.setColumnWidth(index, columnWidth)
            val cell = row.createCell(index)
            cell?.setCellValue(value)
            cell.cellStyle = cellStyle
        }
    }

    private fun getHeaderStyle(workbook: Workbook): CellStyle {

        //Cell style for header row
        val cellStyle: CellStyle = workbook.createCellStyle()

        //Apply cell color
        val colorMap: IndexedColorMap = (workbook as XSSFWorkbook).stylesSource.indexedColors
        var color = XSSFColor(IndexedColors.GREY_25_PERCENT, colorMap).indexed
        cellStyle.fillForegroundColor = color
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)

        cellStyle.wrapText = true
        cellStyle.setBorderBottom(BorderStyle.THIN)
        cellStyle.setBorderTop(BorderStyle.THIN)
        cellStyle.setBorderLeft(BorderStyle.THIN)
        cellStyle.setBorderRight(BorderStyle.THIN)

        //Apply font style on cell text
        /*
        val whiteFont = workbook.createFont()
        color = XSSFColor(IndexedColors.WHITE, colorMap).indexed
        whiteFont.color = color
        whiteFont.bold = true
        cellStyle.setFont(whiteFont)
        */


        return cellStyle
    }

    private fun getBodyStyle(workbook: Workbook): CellStyle {

        //Cell style for header row
        val cellStyle: CellStyle = workbook.createCellStyle()

        cellStyle.wrapText = true
        cellStyle.setBorderBottom(BorderStyle.THIN)
        cellStyle.setBorderTop(BorderStyle.THIN)
        cellStyle.setBorderLeft(BorderStyle.THIN)
        cellStyle.setBorderRight(BorderStyle.THIN)

        cellStyle.setAlignment(HorizontalAlignment.LEFT)
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP)


        return cellStyle
    }

    private fun generateTable(jsonData: String, sheet: Sheet, bodyCellStyle: CellStyle){
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
        Log.d("Generator", "Json parsed: $name, $description, $testSteps")
        val firstStep = testSteps.first().split("!!!")
        addData(1, listOf(name, description, firstStep[1], firstStep[2]), sheet, bodyCellStyle)
        Log.d("Generator", "First row added")
        for (i in 2 until testSteps.size){
            val step = testSteps[i].split("!!!")
            addData(i, listOf("", "", step[1], step[2]), sheet, bodyCellStyle)
            Log.d("Generator", "Row added")
        }
        //TODO adapt the parser to the agreed json format
    }
    private fun addData(rowIndex: Int, values: List<String>,sheet: Sheet, style: CellStyle){
        val row = sheet.createRow(rowIndex)


        for ((index, value) in values.withIndex()) {
            createCell(row, index, value, style)
        }
    }

    private fun createCell(row: Row, columnIndex: Int, value: String?, style: CellStyle){
        val cell = row.createCell(columnIndex)
        cell?.setCellValue(value)
        cell.cellStyle = style
    }
}
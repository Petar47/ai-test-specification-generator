package hr.foi.aitsg

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import hr.foi.database.DataViewModel
import hr.foi.database.Report

@Composable
fun ReportPreviewPage(navController: NavHostController, viewModel: DataViewModel, response: OpenAIResponse, projectId: String?) {
    var id_project = projectId!!.toInt()
    var editingName by remember { mutableStateOf(response.name) }
    var editingDescription by remember { mutableStateOf(response.description) }
    var time = response.JSONresponse.length * 0.3;
    var newReport = Report(null, response.name, response.description, response.JSONresponse,null,time.toString(),id_project)
    // Use mutableStateOf for concatenatedValues
    var concatenatedValues by remember {
        mutableStateOf(response.testSteps.map { it }.toMutableList())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = editingName,
            onValueChange = {
                editingName = it
            },
            label = { Text("Name") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = editingDescription,
            onValueChange = {
                editingDescription = it
            },
            label = { Text("Description") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        for (index in response.testSteps.indices) {
            val stepData = concatenatedValues[index].split("!!!").toMutableList()

            OutlinedTextField(
                value = stepData.getOrNull(1) ?: "",
                onValueChange = { newStep ->
                    val updatedValues = concatenatedValues.toMutableList()
                    updatedValues[index] = stepData
                        .withIndex()
                        .joinToString("!!!") { (i, value) ->
                            if (i == 1) newStep else value
                        }
                    concatenatedValues = updatedValues
                },
                label = { Text("Step $index description") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = stepData.getOrNull(2) ?: "",
                onValueChange = { newStep ->
                    val updatedValues = concatenatedValues.toMutableList()
                    updatedValues[index] = stepData
                        .withIndex()
                        .joinToString("!!!") { (i, value) ->
                            if (i == 2) newStep else value
                        }
                    concatenatedValues = updatedValues
                },
                label = { Text("Step $index code") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = stepData.getOrNull(3) ?: "",
                onValueChange = { newStep ->
                    val updatedValues = concatenatedValues.toMutableList()
                    updatedValues[index] = stepData
                        .withIndex()
                        .joinToString("!!!") { (i, value) ->
                            if (i == 3) newStep else value
                        }
                    concatenatedValues = updatedValues
                },
                label = { Text("Step $index expected result") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                response.name = editingName
                response.description = editingDescription
                response.testSteps = concatenatedValues
                viewModel.insertReport(newReport)
                navController.navigate("show-project/" + "${newReport.id_project}")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Save changes")
        }
    }
}
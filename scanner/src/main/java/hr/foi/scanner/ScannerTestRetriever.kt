package hr.foi.scanner

import androidx.compose.runtime.Composable
import hr.foi.interfaces.TestRetriever

class ScannerTestRetriever: TestRetriever {

    @Composable
    override fun showUI(getTestData: (data: String) -> Unit) {
        ScannerPage(getTestData = {testData ->
            getTestData(testData)
        })
    }
}
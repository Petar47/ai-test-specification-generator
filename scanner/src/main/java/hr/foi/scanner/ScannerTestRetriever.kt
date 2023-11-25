package hr.foi.scanner

import androidx.compose.runtime.Composable
import hr.foi.interfaces.TestRetriever

class ScannerTestRetriever: TestRetriever {
    private var testString: String = ""
    override fun getTest(): String {
        return testString
    }

    @Composable
    override fun showUI() {
        ScannerPage(getTestData = {testData ->
            testString = testData
        })
    }
}
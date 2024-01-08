package hr.foi.scanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import hr.foi.interfaces.Scanner
import hr.foi.interfaces.TestRetriever

class ScannerTestRetriever: Scanner {

    @Composable
    override fun TestRetrieverUI(getTestData: (data: String) -> Unit) {
        ScannerPage(getTestData = {testData ->
            getTestData(testData)
        })
    }

    override fun getRoute(): String {
        return "tests/scanner/"
    }
    @Composable
    override fun getIcon(): Painter {
        return painterResource(id = R.drawable.camera)
    }

}
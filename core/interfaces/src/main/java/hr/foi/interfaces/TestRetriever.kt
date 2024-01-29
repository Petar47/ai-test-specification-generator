package hr.foi.interfaces

import androidx.compose.runtime.Composable

interface TestRetriever {

    @Composable
    fun TestRetrieverUI(getTestData: (data: String) -> Unit)
}
package hr.foi.interfaces

import androidx.compose.runtime.Composable

interface TestRetriever {

    @Composable
    fun showUI(getTestData: (data: String) -> Unit)
}
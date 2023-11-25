package hr.foi.interfaces

import androidx.compose.runtime.Composable

interface TestRetriever {
    fun getTest(): String

    @Composable
    fun showUI()
}
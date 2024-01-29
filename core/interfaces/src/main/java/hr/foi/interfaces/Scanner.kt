package hr.foi.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

interface Scanner {
    fun getRoute(): String
    @Composable
    fun getIcon(): Painter

    @Composable
    fun TestRetrieverUI(getTestData: (data: String) -> Unit)
}
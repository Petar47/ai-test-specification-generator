package hr.foi.aitsg

import hr.foi.interfaces.TestRetriever
import hr.foi.scanner.FileScanner
import hr.foi.scanner.ScannerTestRetriever

class TestRetrieverFactory {
    companion object{
        fun getRetriever(retrieverType: String): TestRetriever {
            return when (retrieverType){
                "scanner" -> ScannerTestRetriever()
                "upload" -> FileScanner()
                else -> ScannerTestRetriever()
            }
        }
    }
}
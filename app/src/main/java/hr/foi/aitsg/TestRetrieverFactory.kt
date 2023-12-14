package hr.foi.aitsg

import hr.foi.interfaces.Scanner
import hr.foi.interfaces.TestRetriever
import hr.foi.scanner.FileScanner
import hr.foi.scanner.ScannerTestRetriever

enum class TestRetrieverType(id: String){
    SCANNER("scanner"),
    UPLOAD("upload")
}

class TestRetrieverFactory {
    companion object{
        fun getRetriever(retrieverType: TestRetrieverType): Scanner {
            return when (retrieverType){
                TestRetrieverType.SCANNER -> ScannerTestRetriever()
                TestRetrieverType.UPLOAD -> FileScanner()
            }
        }
    }
}


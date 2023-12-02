package hr.foi.aitsg

class OpenAIHandler() {
    val response = OpenAIResponse()
    val openaiKey = BuildConfig.OPENAI_KEY
    private var query = ""
    fun makeQuery(input: String) {
        if(input.startsWith("#include")) {
            query =
                "U nastavku ti dajem jedan Google Test (C++), ti generiraj ime testa, opis testa i izdvoji testne korake. Objasni što se u testu radi i što se želi postići u implementacijskom kodu:\n\n$input"
        }
        else if(input.startsWith("***")) {
            query =
                "U nastavku ti dajem jedan Robot Framework test, ti generiraj ime testa, opis testa i izdvoji testne korake. Objasni što se u testu radi i što se želi postići u implementacijskom kodu:\n\n$input"
        }
        else {
            query =
                "U nastavku ti dajem jedan test, ti generiraj ime testa, opis testa i izdvoji testne korake. Objasni što se u testu radi i što se želi postići u implementacijskom kodu:\n\n$input"
        }
    }
    private fun sendQuery(): OpenAIResponse {
        //TODO sljedeći sprint implementirati slanje na OpenAI
        return response
    }
}
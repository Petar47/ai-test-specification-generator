package hr.foi.aitsg

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import org.json.JSONObject

class OpenAIHandler() {
    private val openaiKey = BuildConfig.OPENAI_KEY
    private val openAI = OpenAI(openaiKey)
    val modelId = ModelId("gpt-3.5-turbo-1106")
    private var query = ""
    suspend fun makeQuery(input: String): OpenAIResponse? {
        if(input.startsWith("#include")) {
            query =
                "U nastavku ti dajem jedan Google test (C++), ti generiraj ime testa, opis testa, i izdvoji testne korake. Objasni što se u testu radi i što se želi postići u implementacijskom kodu. Format neka bude pripremljen za objekt OpenAIResponse zapisan u JSON notaciji koji u sebi sadrži atribute name, description i testSteps. testSteps je polje stringova, a svaki string je zapisan u CSV obliku gdje je !!! delimiter, a zapis je oblika step!!!description!!!code!!!expectedResult (step je broj koraka, description opis koraka, code je ili dio relevantnog koda ili objašnjenje istog, a expected result je očekivani rezultat ili opis očekivanog ishoda. Mora imati četiri polja, znači !!! delimiter se pojavljuje točno tri puta. Ako je bilo koje od tih polja prazno, stavi crticu). Potreban je samo JSON, nikakvi dodatni komentari. Ovo je kod:\n\n$input"
            val response = sendQuery(query)
            return createResponseObject(response)
        }
        else if(input.startsWith("***")) {
            query =
                "U nastavku ti dajem jedan Robot Framework test, ti generiraj ime testa, opis testa, i izdvoji testne korake. Objasni što se u testu radi i što se želi postići u implementacijskom kodu. Format neka bude pripremljen za objekt OpenAIResponse zapisan u JSON notaciji koji u sebi sadrži atribute name, description i testSteps. testSteps je polje stringova, a svaki string je zapisan u CSV obliku gdje je !!! delimiter, a zapis je oblika step!!!description!!!code!!!expectedResult (step je broj koraka, description opis koraka, code je ili dio relevantnog koda ili objašnjenje istog, a expected result je očekivani rezultat ili opis očekivanog ishoda. Mora imati četiri polja, znači !!! delimiter se pojavljuje točno tri puta. Ako je bilo koje od tih polja prazno, stavi crticu). Potreban je samo JSON, nikakvi dodatni komentari. Ovo je kod:\n\n$input"
            val response = sendQuery(query)
            return createResponseObject(response)
        }
        else {
            return null
        }
    }

    private suspend fun sendQuery(input: String): String {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo-1106"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = input
                )
            )
        )
        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
        return completion.choices.first().message.content.toString()
    }

    public fun createResponseObject(response: String): OpenAIResponse {
        val responseObject = OpenAIResponse()
        val jsonObject = JSONObject(response)
        Log.e("JSONObject", jsonObject.toString())
        responseObject.name = jsonObject.getString("name")
        responseObject.description = jsonObject.getString("description")
        val testStepsArray = jsonObject.getJSONArray("testSteps")
        Log.e("Steps: ", testStepsArray.toString())
        val testSteps = mutableListOf<String>()
        for (i in 0 until testStepsArray.length()) {
            testSteps.add(testStepsArray.getString(i))
        }
        responseObject.testSteps = testSteps
        Log.e("Steps: ", responseObject.testSteps.toString())
        responseObject.JSONresponse = response
        return responseObject
    }
}
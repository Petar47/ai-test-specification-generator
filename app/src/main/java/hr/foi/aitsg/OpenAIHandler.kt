package hr.foi.aitsg

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import org.json.JSONObject

class OpenAIHandler() {
    val response = OpenAIResponse()
    val openaiKey = BuildConfig.OPENAI_KEY
    val openAI = OpenAI(openaiKey)
    val modelId = ModelId("gpt-3.5-turbo-0613")
    private var query = ""
    suspend fun makeQuery(input: String) {
        if(input.startsWith("#include")) {
            query =
                "U nastavku ti dajem jedan Google test (C++), ti generiraj ime testa, opis testa i izdvoji testne korake. Objasni što se u testu radi i što se želi postići u implementacijskom kodu. Format neka bude pripremljen za objekt OpenAIResponse zapisan u JSON notaciji koji u sebi sadrži atribute name, decription i testSteps. Potreban je samo JSON, nikakvi dodatni komentari. Ovo je kod:\n\n$input"
            var response = sendQuery(query)
            CreateResponseObject(response)
        }
        else if(input.startsWith("***")) {
            query =
                "U nastavku ti dajem jedan Robot Framework test, ti generiraj ime testa, opis testa i izdvoji testne korake. Objasni što se u testu radi i što se želi postići u implementacijskom kodu. Format neka bude pripremljen za objekt OpenAIResponse zapisan u JSON notaciji koji u sebi sadrži atribute name, decription i testSteps. Potreban je samo JSON, nikakvi dodatni komentari. Ovo je kod:\n\n$input"
            var response = sendQuery(query)
            CreateResponseObject(response)
        }
        else {
            query =
                "U nastavku ti dajem jedan test, ti generiraj ime testa, opis testa i izdvoji testne korake. Objasni što se u testu radi i što se želi postići u implementacijskom kodu. Format neka bude pripremljen za objekt OpenAIResponse zapisan u JSON notaciji koji u sebi sadrži atribute name, decription i testSteps. Potreban je samo JSON, nikakvi dodatni komentari. Ovo je kod:\n\n$input"
            var response = sendQuery(query)
            CreateResponseObject(response)
        }
    }

    private suspend fun sendQuery(input: String): String {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
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

    private fun CreateResponseObject(response: String) {
        val jsonObject = JSONObject(response)
        OpenAIResponse().name = jsonObject.getString("name")
        OpenAIResponse().description = jsonObject.getString("description")
        val testStepsArray = jsonObject.getJSONArray("testSteps")
        val testSteps = mutableListOf<String>()
        for (i in 0 until testStepsArray.length()) {
            val stepObject = testStepsArray.getJSONObject(i)
            val step = stepObject.getInt("step")
            val stepDescription = stepObject.getString("description")
            val code = stepObject.getString("code")
            testSteps.add("$step!!!$stepDescription!!!$code")
        }
        OpenAIResponse().testSteps = testSteps
    }
}
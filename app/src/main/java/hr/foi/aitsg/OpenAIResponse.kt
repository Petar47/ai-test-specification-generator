package hr.foi.aitsg

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponse (
    var name: String = "",
    var description: String = "",
    var testSteps: List<String> = listOf(),
    var JSONresponse: String = ""
)
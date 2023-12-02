package hr.foi.aitsg

class OpenAIResponse {
    var name: String = ""
        get() = field
        set(value) {
            field = value
        }

    var description: String = ""
        get() = field
        set(value) {
            field = value
        }

    var testSteps: List<String> = listOf()
        get() = field
        set(value) {
            field = value
        }
}
package com.example.plantdiseasedetection // change as per your actual package

object AIConfig {
    const val AI_API_URL = "https://api.together.xyz/v1/chat/completions"
    const val AI_MODEL = "mistralai/Mistral-7B-Instruct-v0.2"
    const val AI_MAX_TOKENS = 1500
    const val AI_TEMPERATURE = 0.7

    const val AI_API_KEY = "b68f2588587cb665eb94e89cff6ddafce235a0c570566909f9049fc4837d64be"  // 🔐 Replace with your real key

    fun isConfigured(): Boolean {
        return !AI_API_KEY.contains("b68f2588587cb665eb94e89cff6ddafce235a0c570566909f9049fc4837d64be")
    }

}

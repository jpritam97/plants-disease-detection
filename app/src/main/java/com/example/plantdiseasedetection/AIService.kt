package com.example.plantdiseasedetection

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class DiseaseInfo(val symptoms: String, val management: String)

class AIService {
    // Initialize OkHttpClient with timeouts for robustness
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun getPlantDiseaseInfo(diseaseName: String): DiseaseInfo = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Give symptoms and management of this plant disease in 1 line each:
                Disease: $diseaseName
                Return response as JSON with keys "symptoms" and "management"
            """.trimIndent()

            val requestBody = JSONObject().apply {
                put("model", AIConfig.AI_MODEL)
                put("max_tokens", AIConfig.AI_MAX_TOKENS)
                put("temperature", AIConfig.AI_TEMPERATURE)
                put("messages", JSONArray().put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                }))
            }.toString().toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url(AIConfig.AI_API_URL)
                .addHeader("Authorization", "Bearer ${AIConfig.AI_API_KEY}")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            // Use 'use' to ensure response body is closed
            client.newCall(request).execute().use { response ->
                val body = response.body?.string()
                if (!response.isSuccessful || body == null) {
                    throw Exception("AI request failed: ${response.code}")
                }

                val rawResult = JSONObject(body)
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")

                // Debug log
                Log.d("AI_RESPONSE", "Raw content: $rawResult")

                // Try parsing JSON directly
                var symptoms: String
                var management: String

                try {
                    val parsed = JSONObject(rawResult)
                    symptoms = parsed.optString("symptoms", "Not available")
                    management = parsed.optString("management", "Not available")
                } catch (e: Exception) {
                    // Fallback to regex if JSON parsing fails
                    val symptomsRegex = Regex("(?i)symptoms[:\\s]+(.+)")
                    val managementRegex = Regex("(?i)management[:\\s]+(.+)")

                    symptoms = symptomsRegex.find(rawResult)?.groupValues?.get(1)?.trim() ?: "Not available"
                    management = managementRegex.find(rawResult)?.groupValues?.get(1)?.trim() ?: "Not available"
                }

                DiseaseInfo(symptoms, management)
            }
        } catch (e: Exception) {
            Log.e("AIService", "Failed to fetch disease info for $diseaseName: ${e.message}")
            throw e // Rethrow to let caller handle
        }
    }
}
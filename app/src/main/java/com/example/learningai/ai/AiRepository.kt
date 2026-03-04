package com.example.learningai.ai

import android.content.Context
import android.util.Log
import com.example.learningai.BuildConfig
import com.example.learningai.localDB.QuestionDao
import com.example.learningai.localDB.QuestionEntity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

// ================= 1. MODELS (OpenAI/Groq Format) =================

data class GroqRequest(
    val model: String = "llama-3.3-70b-versatile",
    val messages: List<GroqMessage>,
    val temperature: Float = 0.7f,
    @SerializedName("response_format")
    val responseFormat: ResponseFormat? = null
)
data class GroqMessage(val role: String, val content: String)
data class ResponseFormat(val type: String)
data class GroqResponse(val choices: List<GroqChoice>)
data class GroqChoice(val message: GroqMessage)

data class AiQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)

// ================= 2. INTERFACE =================

interface GroqApiService {
    @POST("v1/chat/completions")
    suspend fun generateContent(
        @Header("Authorization") authHeader: String,
        @Body request: GroqRequest
    ): GroqResponse
}

// ================= 3. REPOSITORY =================

class AiRepository(private val context: Context) {

    companion object {
        private const val TAG = "AI_DEBUG"
        private const val BASE_URL = "https://api.groq.com/openai/"
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val apiService: GroqApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroqApiService::class.java)
    }

    private fun getAuthToken(): String = "Bearer ${BuildConfig.GEMINI_API_KEY}"

    // --- Chat Feature ---
    suspend fun chat(userMsg: String): String {
        return try {
            val request = GroqRequest(
                messages = listOf(
                    GroqMessage("system", "You are a helpful AI tutor. Answer briefly."),
                    GroqMessage("user", userMsg)
                )
            )
            val response = apiService.generateContent(getAuthToken(), request)
            response.choices.firstOrNull()?.message?.content?.trim() ?: "No response"
        } catch (e: Exception) {
            Log.e(TAG, "Chat Error: ${e.message}")
            "Error: ${e.localizedMessage}"
        }
    }

    // --- Question Generation ---
    suspend fun generateAndSaveQuestions(
        classroomId: String,
        subject: String,
        count: Int,
        dao: QuestionDao
    ) {
        try {
            // "JSON" word in prompt is MANDATORY for Groq JSON mode
            val prompt = """
                Generate $count multiple choice questions for "$subject" in JSON format.
                Return ONLY a JSON array of objects. 
                Structure: [{"question":"", "options":["","","",""], "correctIndex":0}]
            """.trimIndent()

            val request = GroqRequest(
                messages = listOf(GroqMessage("user", prompt)),
                responseFormat = ResponseFormat("json_object")
            )

            val response = apiService.generateContent(getAuthToken(), request)
            val rawJson = response.choices.firstOrNull()?.message?.content?.trim() ?: ""

            if (rawJson.isNotEmpty()) {
                val questions: List<AiQuestion> = try {
                    val type = object : TypeToken<List<AiQuestion>>() {}.type
                    if (rawJson.startsWith("[")) {
                        Gson().fromJson(rawJson, type)
                    } else {
                        // Handle if Groq wraps JSON in a key like {"questions": [...]}
                        val jsonObject = Gson().fromJson(rawJson, JsonObject::class.java)
                        val key = jsonObject.keySet().firstOrNull() ?: ""
                        Gson().fromJson(jsonObject.get(key), type)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Parse Error: ${e.message}")
                    emptyList()
                }

                val entities = questions.map {
                    QuestionEntity(
                        classroomId = classroomId,
                        question = it.question,
                        optionA = it.options.getOrElse(0) { "A" },
                        optionB = it.options.getOrElse(1) { "B" },
                        optionC = it.options.getOrElse(2) { "C" },
                        optionD = it.options.getOrElse(3) { "D" },
                        correctIndex = it.correctIndex.coerceIn(0, 3)
                    )
                }
                dao.insertAll(entities)
                Log.d(TAG, "Success: ${entities.size} questions saved.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Quiz Error: ${e.message}")
        }
    }
}
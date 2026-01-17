package com.example.learningai.ai

import com.example.learningai.localDB.QuestionDao
import com.example.learningai.localDB.QuestionEntity

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import android.content.pm.PackageManager



class AiRepository(private val context: Context) {

    private fun getApiKey(): String {
        val appInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        return appInfo.metaData.getString("GEMINI_API_KEY") ?: ""
    }

    private suspend fun generateQuestionsFromAi(
        subject: String,
        count: Int
    ): String {

        val apiKey = getApiKey()
        if (apiKey.isBlank()) {
            throw IllegalStateException("Gemini API key missing")
        }

        val prompt = """
            Generate $count multiple-choice questions for the subject "$subject".

            Rules:
            - Each question must have exactly 4 options
            - Difficulty: beginner to intermediate
            - Output ONLY valid JSON (no markdown, no text)

            JSON format:
            [
              {
                "question": "Question text",
                "options": ["A", "B", "C", "D"],
                "correctIndex": 0
              }
            ]
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt)
                    )
                )
            )
        )

        val response = GeminiClient.api.generateContent(
            apiKey = apiKey,
            body = request
        )

        return response
            .candidates
            .first()
            .content
            .parts
            .first()
            .text
    }

    private fun parseAiQuestions(json: String): List<AiQuestion> {
        return try {
            val cleanJson =
                json.substringAfter("[").substringBeforeLast("]") + "]"

            val type = object : TypeToken<List<AiQuestion>>() {}.type
            Gson().fromJson(cleanJson, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun generateQuestions(
        subject: String,
        count: Int
    ): List<AiQuestion> {
        return try {
            val rawJson = generateQuestionsFromAi(subject, count)
            parseAiQuestions(rawJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun generateAndSaveQuestions(
        classroomId: String,
        subject: String,
        count: Int,
        questionDao: QuestionDao
    ) {
        val aiQuestions = generateQuestions(subject, count)

        if (aiQuestions.isEmpty()) return

        val entities = aiQuestions.map {
            QuestionEntity(
                classroomId = classroomId,
                question = it.question,
                optionA = it.options[0],
                optionB = it.options[1],
                optionC = it.options[2],
                optionD = it.options[3],
                correctIndex = it.correctIndex
            )
        }

        questionDao.insertAll(entities)
    }
    suspend fun insertDummyQuestions(
        classroomId: String,
        questionDao: QuestionDao
    ) {
        val list = listOf(
            QuestionEntity(
                classroomId = classroomId,
                question = "What is Android?",
                optionA = "Operating System",
                optionB = "Browser",
                optionC = "Game",
                optionD = "Language",
                correctIndex = 0
            ),
            QuestionEntity(
                classroomId = classroomId,
                question = "Jetpack Compose is used for?",
                optionA = "Networking",
                optionB = "UI Development",
                optionC = "Database",
                optionD = "API calls",
                correctIndex = 1
            )
        )

        questionDao.insertAll(list)
    }
}
package com.example.learningai.ai

import android.content.Context
import com.example.learningai.R
import com.example.learningai.localDB.QuestionDao
import com.example.learningai.localDB.QuestionEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AiRepository(
    private val context: Context
) {

    private suspend fun generateQuestionsFromAi(
        subject: String,
        count: Int
    ): String {

        val apiKey = context.getString(R.string.gemini_api_key)

        val prompt = """
            Generate $count multiple-choice questions for the subject "$subject".

            Rules:
            - Each question must have 4 options
            - Difficulty: beginner to intermediate
            - Output ONLY JSON

            JSON format:
            [
              {
                "question": "",
                "options": ["", "", "", ""],
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
        val type = object : TypeToken<List<AiQuestion>>() {}.type
        return Gson().fromJson(json, type)
    }

    suspend fun generateQuestions(
        subject: String,
        count: Int
    ): List<AiQuestion> {
        val rawJson = generateQuestionsFromAi(subject, count)
        return parseAiQuestions(rawJson)
    }

    suspend fun generateAndSaveQuestions(
        classroomId: String,
        subject: String,
        count: Int,
        questionDao: QuestionDao
    ) {
        val aiQuestions = generateQuestions(subject, count)

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
}
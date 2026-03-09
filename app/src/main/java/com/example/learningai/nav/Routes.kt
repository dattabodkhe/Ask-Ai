package com.example.learningai.nav

object Routes {
    const val HOME = "home"
    const val ROLE_SELECTION = "role_selection"
    const val LOGIN = "login"
    const val CLASSROOM = "classroom"
    const val CREATE_CLASSROOM = "create_classroom"
    const val JOIN_CLASSROOM = "join_classroom"
    const val CLASSROOM_CHAT = "classroom_chat"
    const val CREATE_AI_QUESTION = "create_ai_question"
    const val PREVIEW_QUESTIONS = "preview_questions"
    const val QUESTIONSCREEN = "question"
    const val RESULT = "result"
    const val CHAT = "chat"
    const val USER_PROFILE = "profile"
    const val CONTACTS = "contacts"
    const val SELECT_CLASSROOM = "select_classroom"
    const val PRIVACY_POLICY = "privacy_policy"
    const val SETTINGS = "settings"

    fun resultRoute(
        classroomId: String,
        score: Int,
        totalQuestions: Int,
        userId: String
    ) = "$RESULT/$classroomId/$score/$totalQuestions/$userId"
}
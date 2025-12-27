package com.example.learningai.Admin



import com.example.learningai.model.InterviewQuestion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object AdminRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun isAdminUser(): Boolean {
        val email = FirebaseAuth.getInstance().currentUser?.email ?: return false

        val doc = db.collection("admin_config")
            .document("admins")
            .get()
            .await()

        val emails = doc.get("emails") as? List<*> ?: emptyList<String>()
        return emails.contains(email)
    }
    suspend fun addQuestion(question: InterviewQuestion){
        db.collection("interview_question")
            .add(question)
            .await()
    }
}
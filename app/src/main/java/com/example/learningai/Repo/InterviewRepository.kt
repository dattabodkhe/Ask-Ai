package com.example.learningai.Repo

import com.example.learningai.model.InterviewQuestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InterviewRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getQuestionsBySubject(subjectId: String): Flow<List<InterviewQuestion>> =
        callbackFlow {

            val listener = firestore
                .collection("interview_questions")
                .whereEqualTo("subjectId", subjectId)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val questions = snapshot?.toObjects(InterviewQuestion::class.java)
                        ?: emptyList()

                    trySend(questions)
                }

            awaitClose {
                listener.remove()
            }
        }
}
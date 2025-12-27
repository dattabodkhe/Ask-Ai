package com.example.learningai.Admin




import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun isAdminUser(): Boolean {
    val user = FirebaseAuth.getInstance().currentUser ?: return false
    val email = user.email ?: return false

    val snapshot = FirebaseFirestore.getInstance()
        .collection("admin_config")
        .document("admins")
        .get()
        .await()

    val admins = snapshot.get("emails") as? List<*> ?: emptyList<String>()

    return admins.contains(email)
}
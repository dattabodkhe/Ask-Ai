package com.example.learningai.MVVM

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.learningai.R
import com.example.learningai.model.AuthState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AuthViewModel : ViewModel() {

    /* ---------------- Firebase ---------------- */

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    /* ---------------- UI State ---------------- */

    private val _authState =
        MutableStateFlow<AuthState>(AuthState.Idle)

    val authState: StateFlow<AuthState> = _authState


    private val _isLoggedIn =
        MutableStateFlow(false)

    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn


    /* ---------------- Google Client ---------------- */

    private lateinit var googleClient: GoogleSignInClient


    /* ---------------- Auth Listener ---------------- */

    private val authListener =
        FirebaseAuth.AuthStateListener { firebaseAuth ->

            _isLoggedIn.value =
                firebaseAuth.currentUser != null
        }


    /* ---------------- Init ---------------- */

    init {

        auth.addAuthStateListener(authListener)
    }


    override fun onCleared() {

        super.onCleared()

        auth.removeAuthStateListener(authListener)
    }


    /* ---------------- Init Google ---------------- */

    fun initGoogle(context: Context) {

        val clientId =
            context.getString(R.string.default_web_client_id)

        val gso =
            GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )
                .requestIdToken(clientId)
                .requestEmail()
                .build()

        googleClient =
            GoogleSignIn.getClient(context, gso)
    }


    /* ---------------- Get Login Intent ---------------- */

    fun getGoogleLoginIntent(): Intent {

        if (!::googleClient.isInitialized) {

            throw IllegalStateException(
                "Google client not initialized"
            )
        }

        return googleClient.signInIntent
    }


    /* ---------------- Handle Google Result ---------------- */

    fun handleGoogleResult(
        data: Intent?,
        onError: (String) -> Unit
    ) {

        _authState.value = AuthState.Loading

        val task =
            GoogleSignIn.getSignedInAccountFromIntent(data)

        try {

            val account =
                task.getResult(ApiException::class.java)

            val token = account.idToken
                ?: throw Exception("Token null")

            firebaseAuthWithGoogle(token, onError)

        } catch (e: Exception) {

            val msg =
                e.message ?: "Google Login Failed"

            _authState.value =
                AuthState.Error(msg)

            onError(msg)
        }
    }


    /* ---------------- Firebase Google Login ---------------- */

    private fun firebaseAuthWithGoogle(
        idToken: String,
        onError: (String) -> Unit
    ) {

        val credential =
            GoogleAuthProvider.getCredential(
                idToken,
                null
            )

        auth
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    _authState.value =
                        AuthState.Success

                } else {

                    val msg =
                        task.exception?.message
                            ?: "Google Auth Failed"

                    _authState.value =
                        AuthState.Error(msg)

                    onError(msg)
                }
            }
    }


    /* ---------------- Email Login ---------------- */

    fun loginWithEmail(
        email: String,
        password: String
    ) {

        if (email.isBlank() || password.isBlank()) {

            _authState.value =
                AuthState.Error("Email or Password empty")

            return
        }

        _authState.value = AuthState.Loading


        auth
            .signInWithEmailAndPassword(
                email,
                password
            )
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    _authState.value =
                        AuthState.Success

                } else {

                    val msg =
                        task.exception?.message
                            ?: "Email Login Failed"

                    _authState.value =
                        AuthState.Error(msg)
                }
            }
    }


    /* ---------------- Signup ---------------- */

    fun signUpWithEmail(
        email: String,
        password: String
    ) {

        if (email.isBlank() || password.length < 6) {

            _authState.value =
                AuthState.Error("Password must be 6+ chars")

            return
        }

        _authState.value = AuthState.Loading


        auth
            .createUserWithEmailAndPassword(
                email,
                password
            )
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    _authState.value =
                        AuthState.Success

                } else {

                    val msg =
                        task.exception?.message
                            ?: "Signup Failed"

                    _authState.value =
                        AuthState.Error(msg)
                }
            }
    }


    /* ---------------- Logout ---------------- */

    fun logout() {

        if (::googleClient.isInitialized) {
            googleClient.signOut()
        }

        auth.signOut()

        _authState.value =
            AuthState.Idle
    }
}

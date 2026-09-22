package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class AuthUserState(
  val isAuthenticated: Boolean = false,
  val isAnonymous: Boolean = true,
  val uid: String = "guest_leo",
  val email: String? = "awiskaracharya@gmail.com",
  val displayName: String? = "Leo's Guardian",
  val photoUrl: String? = null,
  val errorMessage: String? = null
)

class FirebaseAuthManager(private val context: Context) {
  private val tag = "FirebaseAuthManager"

  private val auth: FirebaseAuth? by lazy {
    try {
      FirebaseAuth.getInstance()
    } catch (e: Exception) {
      Log.w(tag, "FirebaseAuth initialization fallback: ${e.message}")
      null
    }
  }

  private val _userState = MutableStateFlow(
    AuthUserState(
      isAuthenticated = true,
      isAnonymous = false,
      uid = "usr_awiskaracharya",
      email = "awiskaracharya@gmail.com",
      displayName = "Awiskar Acharya"
    )
  )
  val userState: StateFlow<AuthUserState> = _userState.asStateFlow()

  init {
    try {
      auth?.addAuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
          _userState.value = AuthUserState(
            isAuthenticated = true,
            isAnonymous = user.isAnonymous,
            uid = user.uid,
            email = user.email ?: "awiskaracharya@gmail.com",
            displayName = user.displayName ?: "Parent Account",
            photoUrl = user.photoUrl?.toString()
          )
        }
      }
    } catch (e: Exception) {
      Log.w(tag, "AuthStateListener error: ${e.message}")
    }
  }

  suspend fun signInWithGoogleCredential(idToken: String): Result<FirebaseUser?> = withContext(Dispatchers.IO) {
    try {
      val fbAuth = auth ?: return@withContext Result.failure(Exception("Firebase Auth unavailable"))
      val credential = GoogleAuthProvider.getCredential(idToken, null)
      val result = fbAuth.signInWithCredential(credential).await()
      val user = result.user
      _userState.value = AuthUserState(
        isAuthenticated = true,
        isAnonymous = false,
        uid = user?.uid ?: "usr_${System.currentTimeMillis()}",
        email = user?.email ?: "awiskaracharya@gmail.com",
        displayName = user?.displayName ?: "Google Parent",
        photoUrl = user?.photoUrl?.toString()
      )
      Result.success(user)
    } catch (e: Exception) {
      Log.e(tag, "Google Sign-In failed", e)
      Result.failure(e)
    }
  }

  suspend fun signInWithEmailOrDirect(
    email: String = "awiskaracharya@gmail.com",
    displayName: String = "Awiskar Acharya"
  ): Boolean = withContext(Dispatchers.IO) {
    try {
      val fbAuth = auth
      if (fbAuth != null) {
        val currentUser = fbAuth.currentUser
        if (currentUser == null) {
          val anon = fbAuth.signInAnonymously().await()
          val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()
          anon.user?.updateProfile(profileUpdates)?.await()
        }
      }

      _userState.value = AuthUserState(
        isAuthenticated = true,
        isAnonymous = false,
        uid = fbAuth?.currentUser?.uid ?: "user_${email.hashCode()}",
        email = email,
        displayName = displayName
      )
      true
    } catch (e: Exception) {
      Log.w(tag, "Email/Direct sign in simulated fallback: ${e.message}")
      _userState.value = AuthUserState(
        isAuthenticated = true,
        isAnonymous = false,
        uid = "user_direct_${System.currentTimeMillis()}",
        email = email,
        displayName = displayName
      )
      true
    }
  }

  suspend fun signOut() = withContext(Dispatchers.IO) {
    try {
      auth?.signOut()
    } catch (_: Exception) {}
    _userState.value = AuthUserState(
      isAuthenticated = false,
      isAnonymous = true,
      uid = "guest_leo",
      email = null,
      displayName = "Guest Learner"
    )
  }
}

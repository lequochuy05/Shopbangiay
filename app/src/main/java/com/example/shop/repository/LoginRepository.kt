package com.example.shop.repository

import com.example.shop.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("UserAccount")

    /**
     * Login with Email / Password
     */
    fun loginWithEmail(email: String, password: String, callback: (Task<AuthResult>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task)
            }
    }

    /**
     * Get data from Firebase realtime Database
     */
    fun getUserData(firebaseUid: String, callback: (UserModel?) -> Unit) {
        database.child(firebaseUid).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = UserModel(
                        uId = firebaseUid,
                        uEmail = snapshot.child("uEmail").value?.toString() ?: "",
                        uFirstName = snapshot.child("uFirstName").value?.toString() ?: "",
                        uLastName = snapshot.child("uLastName").value?.toString() ?: "",
                        uPhoneNumber = snapshot.child("uPhoneNumber").value?.toString() ?: ""
                    )
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    /**
     * Check email exists in database
     */
    fun checkIfEmailExists(email: String, callback: (UserModel?) -> Unit) {
        database.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { userSnapshot ->
                val user = userSnapshot.getValue(UserModel::class.java)
                if (user?.uEmail == email) {
                    callback(user)
                    return@addOnSuccessListener
                }
            }
            callback(null)
        }.addOnFailureListener { callback(null) }
    }

    /**
     * Login with google
     */
    fun loginWithGoogle(googleIdToken: String, callback: (Task<AuthResult>) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task -> callback(task) }
    }

    /**
     * Register new account
     */
    fun registerNewUser(user: UserModel) {
        database.child(user.uId).setValue(user)
    }

    fun getCurrentUser() = auth.currentUser
}
package com.example.shop.repository

import com.example.shop.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RegisterRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("UserAccount")

    fun registerUser(firstName: String, lastName: String, phone: String, email: String, password: String, callback: (Boolean, String) -> Unit) {

        database.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var newUserId = 1

                if (snapshot.exists()) {
                    val lastUser = snapshot.children.first()
                    val lastUserId = lastUser.key?.toIntOrNull() ?: 0
                    newUserId = lastUserId + 1
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = UserModel(newUserId, firstName, lastName, phone, email)

                            // Lưu vào Database với userId tăng dần
                            database.child(newUserId.toString()).setValue(user)
                                .addOnSuccessListener { callback(true, "Register Success") }
                                .addOnFailureListener { e -> callback(false, "Failed to save user: ${e.message}") }
                        } else {
                            callback(false, "Register Failed: ${task.exception?.message}")
                        }
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, "Error: ${error.message}")
            }
        })
    }
}

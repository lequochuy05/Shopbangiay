package com.example.shop.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.model.CategoryModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    fun loadCategory(): LiveData<MutableList<CategoryModel>> {

        val categoryLiveData = MutableLiveData<MutableList<CategoryModel>>()
        val ref = firebaseDatabase.getReference("Category")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
            val lists = mutableListOf<CategoryModel>()
            for (childSnapshot in snapshot.children) {
                val list = childSnapshot.getValue(CategoryModel::class.java)
                if (list != null) {
                    lists.add(list)
                }
            }
                categoryLiveData.value = lists
                Log.d("Firebase", "Load Category Success")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Load Category Failed: ${error.message}")
            }


        })
        return categoryLiveData
    }


}
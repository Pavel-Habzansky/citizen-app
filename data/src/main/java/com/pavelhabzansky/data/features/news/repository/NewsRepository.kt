package com.pavelhabzansky.data.features.news.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pavelhabzansky.domain.features.news.repository.INewsRepository

class NewsRepository : INewsRepository {

    override suspend fun connectFirebase() {
        val database = FirebaseDatabase.getInstance().reference

        database.child("users").child("1").setValue("Hello world")

        Log.i("Database", database.root.toString())

        val userRef = database.child("users")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.i("DataChange", "Data change triggered - Key: ${p0.key}, Value: ${p0.value}")
            }

            override fun onCancelled(p0: DatabaseError) {
                return
            }
        })
    }

}
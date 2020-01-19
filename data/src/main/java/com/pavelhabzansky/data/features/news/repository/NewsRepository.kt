package com.pavelhabzansky.data.features.news.repository

import com.google.firebase.database.*
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.news.repository.INewsRepository
import timber.log.Timber

class NewsRepository(
    private val citiesReference: DatabaseReference
) : INewsRepository {


    override suspend fun connectFirebase() {
        val cityReference = citiesReference.child("pilsen")

        Timber.i(cityReference.key)

        cityReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val city = snapshot.getValue(CityDO::class.java)
                Timber.i(snapshot.value?.toString())
                Timber.i(city?.name)
                city?.key = requireNotNull(snapshot.key)
                Timber.i(city?.key)
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.i(error.message)
            }
        })

//        val database = FirebaseDatabase.getInstance().reference
//
//        database.child("users").child("1").setValue("Hello world")
//
//        Log.i("Database", database.root.toString())
//
//        val userRef = database.child("users")
//
//        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                Log.i("DataChange", "Data change triggered - Key: ${p0.key}, Value: ${p0.value}")
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//                return
//            }
//        })
    }

}
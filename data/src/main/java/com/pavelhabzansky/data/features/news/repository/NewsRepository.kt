package com.pavelhabzansky.data.features.news.repository

import com.google.firebase.database.*
import com.pavelhabzansky.data.features.news.api.RssApi
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.news.repository.INewsRepository
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber

class NewsRepository(
    private val citiesReference: DatabaseReference
) : INewsRepository {


    override suspend fun connectFirebase() {
        val cityReference = citiesReference.child("pilsen")

        cityReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val city = snapshot.getValue(CityDO::class.java)
                city?.key = requireNotNull(snapshot.key)
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.i(error.message)
            }
        })
    }

    override suspend fun loadNews() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.plzen.eu/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

        val rssApi = retrofit.create(RssApi::class.java)

        val response = rssApi.fetchNews().execute()
        Timber.i(response.body().toString())
    }



}
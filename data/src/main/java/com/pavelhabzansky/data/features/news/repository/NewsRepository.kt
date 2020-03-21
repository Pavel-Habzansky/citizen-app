package com.pavelhabzansky.data.features.news.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pavelhabzansky.data.core.*
import com.pavelhabzansky.data.features.cities.dao.CityDao
import com.pavelhabzansky.data.features.api.RssApi
import com.pavelhabzansky.data.features.api.RssItem
import com.pavelhabzansky.data.features.news.dao.NewsDao
import com.pavelhabzansky.data.features.news.entities.NewsEntity
import com.pavelhabzansky.data.features.news.mapper.NewsMapper
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.news.domain.NewsDO
import com.pavelhabzansky.domain.features.news.repository.INewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber
import kotlin.math.*

class NewsRepository(
        private val cityDao: CityDao,
        private val newsDao: NewsDao,
        private val cityReference: DatabaseReference
) : INewsRepository {

    override suspend fun getNews(city: CityInformationDO): List<NewsDO> {
        val items = fetchNewsFromApi(
                feed = requireNotNull(city.rssFeed), url = requireNotNull(city.rssUrl)
        )

        return items.map {
            NewsDO(
                    title = it.title,
                    description = it.description,
                    url = it.link,
                    date = it.pubDate
            )
        }
    }

    override suspend fun loadNews(force: Boolean) {
        if (newsDao.getCount() != 0 && !force) {
            return
        }

        val residentialCity = cityDao.getResidential()

        residentialCity?.let {
            val items = fetchNewsFromApi(feed = it.rssFeed, url = it.rssUrl)

            val news = items.map {
                NewsEntity(
                        title = it.title,
                        description = it.description,
                        url = it.link,
                        date = it.pubDate
                )
            }

            val all = newsDao.getAll()
            val updated = news.subtract(all).plus(all).take(NEWS_LOAD_SIZE)

            newsDao.removeAll()
            newsDao.insertAll(news = updated)
        }
    }

    private fun deg2rad(deg: Double): Double {
        return deg * PI
    }

    override suspend fun loadTouristNews(lat: Double, lng: Double): LiveData<List<NewsDO>> {
        var cities: List<CityInformationDO>
        val mutableNews = MutableLiveData<List<NewsDO>>()

        cityReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cities = snapshot.children.map {
                    val name = it.child(CITY_CHILD_NAME).value?.toString() ?: ""
                    val id = it.child(CITY_CHILD_ID).value?.toString() ?: ""
                    val www = it.child(CITY_CHILD_WWW).value?.toString() ?: ""
                    val rssFeed = it.child(CITY_CHILD_RSS_FEED).value?.toString() ?: ""
                    val rssUrl = it.child(CITY_CHILD_RSS_URL).value?.toString() ?: ""
                    val wikiInfo = it.child(CITY_CHILD_WIKI)
                    val population = wikiInfo.child(WIKI_CHILD_CITIZENS).value?.toString()?.toLong()
                    val description = wikiInfo.child(WIKI_CHILD_HEADLINE).value?.toString()
                    val logo = wikiInfo.child(WIKI_CHILD_LOGO).value?.toString()
                    val lat = wikiInfo.child(WIKI_CHILD_GPS).child(GPS_CHILD_LAT).value?.toString()
                            ?.toDoubleOrNull()
                    val lng = wikiInfo.child(WIKI_CHILD_GPS).child(GPS_CHILD_LNG).value?.toString()
                            ?.toDoubleOrNull()

                    CityInformationDO(
                            key = it.key ?: "",
                            id = id,
                            name = name,
                            population = population,
                            lat = lat,
                            lng = lng,
                            description = description,
                            www = www,
                            rssFeed = rssFeed,
                            rssUrl = rssUrl
                    )
                }

                var leastD: Double = Double.MAX_VALUE
                var closest: CityInformationDO? = null
                cities.forEach {
                    val dlat = deg2rad(requireNotNull(it.lat) - lat)
                    val dlon = deg2rad(requireNotNull(it.lng) - lng)
                    val a = sin(dlat / 2) * sin(dlat / 2) +
                            cos(deg2rad(lat)) * cos(deg2rad(requireNotNull(it.lat))) *
                            sin(dlon / 2) * sin(dlon / 2)
                    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
                    val d = EARTH_RAD * c
                    if (d < leastD) {
                        leastD = d
                        closest = it
                    }
                }

                closest?.let {
                    GlobalScope.launch(Dispatchers.IO) {
                        val items = fetchNewsFromApi(feed = requireNotNull(it.rssFeed), url = requireNotNull(it.rssUrl))
                        val news = items.map {
                            NewsDO(
                                    title = it.title,
                                    description = it.description,
                                    url = it.link,
                                    date = it.pubDate
                            )
                        }
                        mutableNews.postValue(news)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })

        return mutableNews
    }

    override suspend fun loadCachedNews(): LiveData<List<NewsDO>> {
        val entities = newsDao.getAllLive()

        return entities.transform {
            it.map { NewsMapper.mapFrom(from = it) }
        }
    }

    override suspend fun loadNewsItem(title: String): LiveData<NewsDO> {
        newsDao.setAsRead(title = title)
        val entity = newsDao.getNewsLiveData(title = title)

        return entity.transform {
            NewsMapper.mapFrom(from = it)
        }
    }

    private fun fetchNewsFromApi(feed: String, url: String): List<RssItem> {
        val retrofit = Retrofit.Builder()
                .baseUrl(feed)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

        val rssApi = retrofit.create(RssApi::class.java)

        val response = rssApi.fetchNews(url)
                .execute()

        Timber.i(response.body()?.channel?.item?.size?.toString())

        return response.body()
                ?.channel
                ?.item
                ?.take(NEWS_LOAD_SIZE) ?: emptyList()
    }

}
package com.pavelhabzansky.data.features.news.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.pavelhabzansky.data.core.NEWS_LOAD_SIZE
import com.pavelhabzansky.data.features.cities.dao.CityDao
import com.pavelhabzansky.data.features.api.RssApi
import com.pavelhabzansky.data.features.api.RssItem
import com.pavelhabzansky.data.features.news.dao.NewsDao
import com.pavelhabzansky.data.features.news.entities.NewsEntity
import com.pavelhabzansky.data.features.news.mapper.NewsMapper
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.news.domain.NewsDO
import com.pavelhabzansky.domain.features.news.repository.INewsRepository
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber

class NewsRepository(
    private val cityDao: CityDao,
    private val newsDao: NewsDao
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

            val read = newsDao.getAllRead()
            val updated = news.subtract(read).toList()

            newsDao.insertAll(news = updated)
        }
    }

    override suspend fun loadCachedNews(): LiveData<List<NewsDO>> {
        val entities = newsDao.getAllLive()

        return Transformations.map(entities) {
            it.map { NewsMapper.mapFrom(from = it) }
        }
    }

    override suspend fun loadNewsItem(title: String): LiveData<NewsDO> {
        newsDao.setAsRead(title = title)
        val entity = newsDao.getNewsLiveData(title = title)

        return Transformations.map(entity) {
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
package com.pavelhabzansky.citizenapp

import com.pavelhabzansky.citizenapp.features.cities.detail.view.mapper.CityInformationVOMapper
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityInformationVO
import com.pavelhabzansky.citizenapp.features.cities.search.view.mapper.AutoCompleteMapper
import com.pavelhabzansky.citizenapp.features.cities.search.view.mapper.LastSearchesVOMapper
import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.LastCitySearchVO
import com.pavelhabzansky.citizenapp.features.map.view.mapper.IssueTypeVOMapper
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueTypeVO
import com.pavelhabzansky.citizenapp.features.news.view.mapper.NewsVOMapper
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.domain.CityInformationDO
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO
import com.pavelhabzansky.domain.features.news.domain.NewsDO
import org.junit.Assert
import org.junit.Test

class MapperTests {

    @Test
    fun autoCompleteMapperTest() {
        val city = CityDO(key = "Key", name = "Name")

        val autoComplete = AutoCompleteMapper.mapFrom(from = city)

        Assert.assertNotNull(autoComplete)

        Assert.assertEquals(city.key, autoComplete.key)
        Assert.assertEquals(city.name, autoComplete.name)
    }

    @Test
    fun cityInfoVOMapperFromTest() {
        val dom = CityInformationDO(
                key = "Key", id = "ABCDEF", name = "Name",
                description = "Desc", population = 123,
                lat = 0.0, lng = 0.0, www = "www", rssFeed = "feed",
                rssUrl = "rssUrl"
        )

        val vo = CityInformationVOMapper.mapFrom(from = dom)

        Assert.assertNotNull(vo)

        Assert.assertEquals(dom.key, vo.key)
        Assert.assertEquals(dom.id, vo.id)
        Assert.assertEquals(dom.name, vo.name)
        Assert.assertEquals(dom.description, vo.description)
        Assert.assertEquals(dom.lat, vo.lat)
        Assert.assertEquals(dom.lng, vo.lng)
        Assert.assertEquals(dom.population, vo.population)
        Assert.assertEquals(dom.www, vo.www)
        Assert.assertEquals(dom.rssFeed, vo.rssFeed)
        Assert.assertEquals(dom.rssUrl, vo.rssUrl)
    }

    @Test
    fun cityInfoVOMapperToTest() {
        val vo = CityInformationVO(
                key = "Key", id = "ABCDEF", name = "Name",
                description = "Desc", population = 123,
                lat = 0.0, lng = 0.0, www = "www", rssFeed = "feed",
                rssUrl = "rssUrl"
        )

        val dom = CityInformationVOMapper.mapTo(to = vo)

        Assert.assertNotNull(dom)

        Assert.assertEquals(vo.key, dom.key)
        Assert.assertEquals(vo.id, dom.id)
        Assert.assertEquals(vo.name, dom.name)
        Assert.assertEquals(vo.description, dom.description)
        Assert.assertEquals(vo.lat, dom.lat)
        Assert.assertEquals(vo.lng, dom.lng)
        Assert.assertEquals(vo.population, dom.population)
        Assert.assertEquals(vo.www, dom.www)
        Assert.assertEquals(vo.rssFeed, dom.rssFeed)
        Assert.assertEquals(vo.rssUrl, dom.rssUrl)
    }

    @Test
    fun issueTypeVOMapperFromTest() {
        IssueTypeVO.values().forEach {
            val dom = IssueTypeVOMapper.mapFrom(it)

            Assert.assertNotNull(dom)

            Assert.assertEquals(it.type, dom.type)
        }
    }

    @Test
    fun issueTypeVOMapperToTest() {
        com.pavelhabzansky.domain.features.issues.domain.IssueType.values().forEach {
            val vo = IssueTypeVOMapper.mapTo(it)

            Assert.assertNotNull(vo)

            if (it != com.pavelhabzansky.domain.features.issues.domain.IssueType.UNKNOWN) {
                Assert.assertEquals(it.type, vo.type)
            }
        }
    }

    @Test
    fun lastSearchVOFromMapper() {
        val dom = LastSearchItemDO(key = "Key", name = "Name")

        val vo = LastSearchesVOMapper.mapFrom(dom)

        Assert.assertNotNull(vo)

        Assert.assertEquals(dom.key, vo.key)
        Assert.assertEquals(dom.name, vo.name)
    }

    @Test
    fun lastSearchVOToMapper() {
        val vo = LastCitySearchVO(key = "Key", name = "Name")

        val dom = LastSearchesVOMapper.mapTo(vo)

        Assert.assertNotNull(dom)

        Assert.assertEquals(vo.key, dom.key)
        Assert.assertEquals(vo.name, dom.name)
    }

    @Test
    fun newsVOFromMapperTest() {
        val dom = NewsDO(
                title = "Title", description = "Desc",
                url = "url", date = "date", read = false
        )

        val vo = NewsVOMapper.mapFrom(dom)

        Assert.assertNotNull(vo)
        Assert.assertNull(vo.date)

        Assert.assertEquals(dom.title, vo.title)
        Assert.assertEquals(dom.read, vo.read)
        Assert.assertEquals(dom.url, vo.url)
        Assert.assertEquals(dom.description, vo.description)
    }



}
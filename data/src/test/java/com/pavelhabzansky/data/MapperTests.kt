package com.pavelhabzansky.data

import com.pavelhabzansky.data.features.api.Issue
import com.pavelhabzansky.data.features.api.PlacePhoto
import com.pavelhabzansky.data.features.cities.entities.CityEntity
import com.pavelhabzansky.data.features.cities.entities.LastSearchCityEntity
import com.pavelhabzansky.data.features.cities.mapper.CityMapper
import com.pavelhabzansky.data.features.cities.mapper.LastSearchMapper
import com.pavelhabzansky.data.features.issues.entities.IssueEntity
import com.pavelhabzansky.data.features.issues.mapper.IssueMapper
import com.pavelhabzansky.data.features.issues.model.Gps
import com.pavelhabzansky.data.features.news.entities.NewsEntity
import com.pavelhabzansky.data.features.news.mapper.NewsMapper
import com.pavelhabzansky.data.features.places.entities.PlaceEntity
import com.pavelhabzansky.data.features.places.mapper.PhotoMapper
import com.pavelhabzansky.data.features.places.mapper.PlaceMapper
import com.pavelhabzansky.domain.features.cities.domain.CityDO
import com.pavelhabzansky.domain.features.cities.domain.LastSearchItemDO
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.domain.IssueType
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MapperTests {

    @Test
    fun placeFromMapperTest() {
        val entity = PlaceEntity(
                placeId = "ID", location = Gps(), name = "Name",
                vicinity = "Vicinity", open = false, rating = 4.3
        )

        val dom = PlaceMapper.mapFrom(entity)

        assertNotNull(dom)

        assertEquals(entity.placeId, dom.placeId)
        assertEquals(entity.name, dom.name)
        assertEquals(entity.vicinity, dom.vicinity)
        assertEquals(entity.open, dom.open)
        assertEquals(entity.rating, dom.rating)

        assertTrue(entity.location.lat == dom.lat)
        assertTrue(entity.location.lng == dom.lng)
    }

    @Test
    fun photoFromMapperTest() {
        val photo = PlacePhoto(
                height = 0,
                width = 0,
                photoRef = "REFERENCE"
        )

        val entity = PhotoMapper.mapFrom(photo)

        assertNotNull(entity)

        assertEquals(photo.height, entity.height)
        assertEquals(photo.width, entity.width)
        assertEquals(photo.photoRef, entity.photoRef)
    }

    @Test
    fun newsFromMapperTest() {
        val entity = NewsEntity(
                title = "Title", description = "Desc",
                url = "url", date = "date", read = false
        )

        val dom = NewsMapper.mapFrom(entity)

        assertNotNull(dom)

        assertEquals(entity.title, dom.title)
        assertEquals(entity.description, dom.description)
        assertEquals(entity.url, dom.url)
        assertEquals(entity.date, dom.date)
        assertEquals(entity.read, dom.read)
    }

    @Test
    fun lastSearchFromMapper() {
        val entity = LastSearchCityEntity(key = "Key", name = "Name", timestamp = 123)

        val dom = LastSearchMapper.mapFrom(entity)

        assertNotNull(dom)

        assertEquals(entity.key, dom.key)
        assertEquals(entity.name, dom.name)
    }

    @Test
    fun lastSearchToMapper() {
        val dom = LastSearchItemDO(key = "Key", name = "Name")

        val entity = LastSearchMapper.mapTo(dom)

        assertNotNull(entity)

        assertEquals(dom.key, entity.key)
        assertEquals(dom.name, entity.name)
    }

    @Test
    fun mapApiToIssueEntityTest() {
        val api = Issue(
                title = "Title", createTime = 123,
                type = "TRASH",
                description = "Desc",
                gps = Gps(lat = 0.0, lng = 0.0),
                img = "img.img"
        )

        val entity = IssueMapper.mapApiToIssueEntity(api)

        assertNotNull(entity)

        assertEquals(api.title, entity.title)
        assertEquals(api.createTime, entity.createTime)
        assertEquals(api.type, entity.type.type)
        assertEquals(api.description, entity.description)
        assertEquals(api.img, entity.img)

        assertTrue(api.gps.lat == entity.lat)
        assertTrue(api.gps.lng == entity.lng)
    }

    @Test
    fun mapDomToApiTest() {
        val dom = IssueDO(
                title = "Title", createTime = 123,
                type = IssueType.TRASH,
                description = "Desc", lat = 0.0,
                lng = 0.0, img = "img.img"
        )

        val api = IssueMapper.mapDomToApi(dom)

        assertNotNull(api)

        assertEquals(dom.title, api.title)
        assertEquals(dom.createTime, api.createTime)
        assertEquals(dom.type.type, api.type)
        assertEquals(dom.description, api.description)
        assertEquals(dom.img, api.img)

        assertTrue(dom.lat == api.gps.lat)
        assertTrue(dom.lng == api.gps.lng)
    }

    @Test
    fun issueMapperFromTest() {
        val entity = IssueEntity(
                title = "Title", createTime = 123,
                type = com.pavelhabzansky.data.features.issues.model.IssueType.TRASH, description = "Desc",
                lat = 0.0, lng = 0.0, img = "img.img"
        )

        val dom = IssueMapper.mapFrom(from = entity)

        assertNotNull(dom)

        assertEquals(entity.title, dom.title)
        assertEquals(entity.createTime, dom.createTime)
        assertEquals(entity.type.type, dom.type.type)
        assertEquals(entity.description, dom.description)
        assertEquals(entity.img, dom.img)

        assertTrue(entity.lat == dom.lat)
        assertTrue(entity.lng == dom.lng)
    }

    @Test
    fun issueMapperToTest() {
        val dom = IssueDO(
                title = "Title", createTime = 123,
                type = com.pavelhabzansky.domain.features.issues.domain.IssueType.TRASH,
                description = "Desc", lat = 0.0,
                lng = 0.0, img = "img.img"
        )

        val entity = IssueMapper.mapTo(dom)

        assertNotNull(entity)

        assertEquals(dom.title, entity.title)
        assertEquals(dom.createTime, entity.createTime)
        assertEquals(dom.type.type, entity.type.type)
        assertEquals(dom.description, entity.description)
        assertEquals(dom.img, entity.img)

        assertTrue(dom.lat == entity.lat)
        assertTrue(dom.lng == entity.lng)
    }

    @Test
    fun cityMapperFromTest() {
        val entity = CityEntity(key = "Key", id = "ABCDEF", name = "Name", residential = false)

        val dom = CityMapper.mapFrom(from = entity)

        assertNotNull(dom)

        assertEquals(entity.key, dom.key)
        assertEquals(entity.name, dom.name)
        assertEquals(entity.id, dom.id)
        assertEquals(entity.residential, dom.residential)
    }

    @Test
    fun cityMapperToTest() {
        val dom = CityDO(key = "Key", id = "ABCDEF", name = "Name", residential = false)

        val entity = CityMapper.mapTo(dom)

        assertNotNull(entity)

        assertEquals(dom.key, dom.key)
        assertEquals(dom.name, entity.name)
        assertEquals(dom.id, entity.id)
        assertEquals(dom.residential, entity.residential)
    }
}

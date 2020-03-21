package com.pavelhabzansky.citizenapp

import com.pavelhabzansky.citizenapp.core.*
import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.LastCitySearchVO
import org.junit.Assert
import org.junit.Test
import java.util.*

class UtilsTests {

    @Test
    fun dateToFormatStringTest() {
        val date = Date(1584819388288)
        val formatted = date.toFormattedString()

        Assert.assertNotNull(formatted)

        Assert.assertEquals("21.03.2020 08:36", formatted)
    }

    @Test
    fun timestampToStringTest() {
        val time = 1584819388288L
        val string = time.timestampToString()

        Assert.assertNotNull(string)

        Assert.assertEquals("21.03.2020 08:36", string)
    }

    @Test
    fun toJsonTest() {
        val lastSearch = LastCitySearchVO("key", "name")
        val json = lastSearch.toJson()

        val expected = "{\"key\":\"key\",\"name\":\"name\"}"

        Assert.assertNotNull(json)

        Assert.assertEquals(expected, json.trim())
    }

    @Test
    fun fromJsonTest() {
        val json = "{\"key\":\"key\",\"name\":\"name\"}"
        val search = json.fromJson(LastCitySearchVO::class.java)

        Assert.assertNotNull(search)

        Assert.assertEquals("key", search.key)
        Assert.assertEquals("name", search.name)
    }

    @Test
    fun fromRssDateTest() {
        val rss = "Sat, 21 Mar 2020 20:46:24 GMT"
        val date = rss.fromRssDate()

        Assert.assertNotNull(date)

        date?.let {
            val cal = Calendar.Builder()
                    .setInstant(it)
                    .build()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minutes = cal.get(Calendar.MINUTE)
            val second = cal.get(Calendar.SECOND)

            Assert.assertEquals(2020, year)
            Assert.assertEquals(2, month)
            Assert.assertEquals(21, day)
            Assert.assertEquals(21, hour)
            Assert.assertEquals(46, minutes)
            Assert.assertEquals(24, second)

        }
    }

}
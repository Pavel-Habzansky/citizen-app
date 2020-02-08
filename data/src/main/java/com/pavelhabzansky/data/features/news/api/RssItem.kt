package com.pavelhabzansky.data.features.news.api

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class RssItem(
    @field:Element(required = false)
    var title: String = "",
    @field:Element(required = false)
    var link: String = "",
    @field:Element(required = false)
    var description: String = "",
    @field:Element(required = false)
    var pubDate: String = ""
) {

    override fun toString(): String {
        return "RssItem [title=$title, link=$link, pubDate=$pubDate, description=$description]"
    }

}
package com.pavelhabzansky.data.features.news.api

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "channel", strict = false)
data class RssChannel(
    @field:Element(required = false)
    var title: String = "",
    @field:Element(required = false)
    var image: RssImage? = null,
    @field:ElementList(inline = true, required = false)
    var item: MutableList<RssItem> = mutableListOf()
) {

    override fun toString(): String {
        return "Channel [image=$image, item=$item]"
    }

}
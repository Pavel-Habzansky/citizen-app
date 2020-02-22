package com.pavelhabzansky.data.features.api

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class RssFeed(
    @field:Element
    var channel: RssChannel? = null
) {

    override fun toString(): String {
        return "RssFeed [channel = $channel]"
    }

}
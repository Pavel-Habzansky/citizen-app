package com.pavelhabzansky.data.features.api

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "image", strict = false)
data class RssImage(
    @field:Element(required = false)
    var url: String = "",
    @field:Element(required = false)
    var width: String = "",
    @field:Element(required = false)
    var height: String = ""
) {

    override fun toString(): String {
        return "RssImage [url=$url, width=$width, height=$height]"
    }

}
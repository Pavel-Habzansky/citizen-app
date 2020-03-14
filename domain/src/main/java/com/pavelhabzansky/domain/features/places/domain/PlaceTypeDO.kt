package com.pavelhabzansky.domain.features.places.domain

enum class PlaceTypeDO(val type: String) {

    BAR("bar"),
    PARK("park"),
    BUS("bus_station"),
    CAFE("cafe"),
    CHURCH("church"),
    MUSEUM("museum"),
    POLICE("police"),
    PARKING("parking"),
    TOURIST_ATTRACTION("tourist_attraction");

    companion object {

        fun fromString(type: String): PlaceTypeDO {
            return when (type) {
                BAR.type -> BAR
                PARK.type -> PARK
                BUS.type -> BUS
                CAFE.type -> CAFE
                CHURCH.type -> CHURCH
                MUSEUM.type -> MUSEUM
                POLICE.type -> POLICE
                PARKING.type -> PARKING
                else -> TOURIST_ATTRACTION
            }
        }

    }

}
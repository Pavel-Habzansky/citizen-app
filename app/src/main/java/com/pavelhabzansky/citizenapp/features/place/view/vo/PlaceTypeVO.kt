package com.pavelhabzansky.citizenapp.features.place.view.vo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO

enum class PlaceTypeVO(val type: String, @DrawableRes val icon: Int, @StringRes val text: Int) {
    BAR("bar", R.drawable.ic_bar, R.string.place_type_bar),
    PARK("park", R.drawable.ic_park, R.string.place_type_park),
    BUS("bus_station", R.drawable.ic_bus, R.string.place_type_bus),
    CAFE("cafe", R.drawable.ic_cafe, R.string.place_type_cafe),
    CHURCH("church", R.drawable.ic_church, R.string.place_type_church),
    MUSEUM("museum", R.drawable.ic_museum, R.string.place_type_museum),
    POLICE("police", R.drawable.ic_police, R.string.place_type_police),
    PARKING("parking", R.drawable.ic_parking, R.string.place_type_parking),
    TOURIST_ATTRACTION("tourist_attraction", R.drawable.ic_see_tourist, R.string.place_type_poi);

    companion object {

        fun fromDomain(dom: PlaceTypeDO): PlaceTypeVO {
            return when (dom) {
                PlaceTypeDO.BAR -> BAR
                PlaceTypeDO.PARK -> PARK
                PlaceTypeDO.BUS -> BUS
                PlaceTypeDO.CAFE -> CAFE
                PlaceTypeDO.CHURCH -> CHURCH
                PlaceTypeDO.MUSEUM -> MUSEUM
                PlaceTypeDO.POLICE -> POLICE
                PlaceTypeDO.PARKING -> PARKING
                PlaceTypeDO.TOURIST_ATTRACTION -> TOURIST_ATTRACTION
            }
        }

    }
}
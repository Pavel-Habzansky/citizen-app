package com.pavelhabzansky.citizenapp.features.place.view.vo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO

enum class PlaceTypeVO(val type: String, @DrawableRes val icon: Int, @StringRes val text: Int) {
    BAR("bar", R.drawable.pin_bar, R.string.place_type_bar),
    PARK("park", R.drawable.pin_park, R.string.place_type_park),
    BUS("bus_station", R.drawable.pin_bus, R.string.place_type_bus),
    CAFE("cafe", R.drawable.pin_cafe, R.string.place_type_cafe),
    CHURCH("church", R.drawable.pin_church, R.string.place_type_church),
    MUSEUM("museum", R.drawable.pin_museum, R.string.place_type_museum),
    POLICE("police", R.drawable.pin_police, R.string.place_type_police),
    PARKING("parking", R.drawable.pin_parking, R.string.place_type_parking),
    TOURIST_ATTRACTION("tourist_attraction", R.drawable.pin_camera, R.string.place_type_poi);

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
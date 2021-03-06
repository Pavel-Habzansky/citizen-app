package com.pavelhabzansky.citizenapp.features.place.view.vo

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.pavelhabzansky.citizenapp.features.issues.list.view.vo.MapItem

data class PlaceVO(
        val placeId: String,
        val lat: Double,
        val lng: Double,
        val name: String,
        val vicinity: String,
        val open: Boolean?,
        val rating: Double?,
        val type: PlaceTypeVO
) : ClusterItem, MapItem {

    override fun getPosition() = LatLng(lat, lng)

    override fun getTitle() = name

    override fun getSnippet() = placeId
}
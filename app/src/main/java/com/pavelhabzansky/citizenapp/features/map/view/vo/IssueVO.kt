package com.pavelhabzansky.citizenapp.features.map.view.vo

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.pavelhabzansky.citizenapp.features.issues.list.view.vo.MapItem

data class IssueVO(
    val issueTitle: String,
    val createTime: Long,
    val type: IssueTypeVO,
    val description: String,
    val lat: Double,
    val lng: Double,
    val img: String
) : ClusterItem, MapItem {

    override fun getPosition() = LatLng(lat, lng)

    override fun getSnippet() = description

    override fun getTitle() = issueTitle

}
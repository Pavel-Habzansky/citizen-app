package com.pavelhabzansky.citizenapp.features.map.view

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceVO

class PlaceClusterRenderer(
        val context: Context,
        googleMap: GoogleMap,
        clusterManager: ClusterManager<ClusterItem>
) : DefaultClusterRenderer<ClusterItem>(context, googleMap, clusterManager) {

    override fun onBeforeClusterItemRendered(item: ClusterItem?, markerOptions: MarkerOptions?) {
        // TODO Let IssueVO and PlaceVO implement API to expose joint properties

        if (item is PlaceVO) {
            item.type.icon.let {
                val drawableIcon = ContextCompat.getDrawable(context, it)
                drawableIcon?.let { markerOptions?.icon(obtainIcon(it)) }
            }

            markerOptions?.snippet(item.snippet)
            markerOptions?.title(item.title)
            markerOptions?.visible(true)
        }

        if(item is IssueVO) {
            item.type.icon.let {
                val drawableIcon = ContextCompat.getDrawable(context, it)
                drawableIcon?.let { markerOptions?.icon(obtainIcon(it)) }
            }

            markerOptions?.snippet(item.snippet)
            markerOptions?.title(item.title)
            markerOptions?.visible(true)
        }

    }

}
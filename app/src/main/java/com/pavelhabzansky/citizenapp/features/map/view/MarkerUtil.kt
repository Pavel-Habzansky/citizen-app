package com.pavelhabzansky.citizenapp.features.map.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun obtainIcon(icon: Drawable): BitmapDescriptor {
    return BitmapDescriptorFactory.fromBitmap(icon.bitmapFromVector())
}

fun Drawable.bitmapFromVector(): Bitmap {
    val bitmap = Bitmap.createBitmap(
            intrinsicWidth,
            intrinsicHeight,
            Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)

    return bitmap

}
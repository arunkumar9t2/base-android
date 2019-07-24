package dev.arunkumar.common.view

import android.graphics.Outline
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.view.View
import android.view.ViewOutlineProvider

inline fun View.outline(crossinline outlineBuilder: View.(outline: Outline) -> Unit) {
    if (SDK_INT >= LOLLIPOP) {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) = view.outlineBuilder(outline)
        }
        clipToOutline = true
    }
}
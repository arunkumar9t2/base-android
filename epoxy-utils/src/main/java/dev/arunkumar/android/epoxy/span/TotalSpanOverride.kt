package dev.arunkumar.android.epoxy.span

import com.airbnb.epoxy.EpoxyModel

object TotalSpanOverride : EpoxyModel.SpanSizeOverrideCallback {
    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int) = totalSpanCount
}
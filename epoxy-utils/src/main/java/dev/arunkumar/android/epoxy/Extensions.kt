package dev.arunkumar.android.epoxy

import com.airbnb.epoxy.EpoxyRecyclerView
import dev.arunkumar.common.recyclerview.GridCalculator
import dev.arunkumar.common.recyclerview.applyTo as baseApply

fun GridCalculator.applyTo(epoxyRecyclerView: EpoxyRecyclerView) {
  baseApply(epoxyRecyclerView)
  epoxyRecyclerView.setItemSpacingPx(gridData().itemSpacing)
}
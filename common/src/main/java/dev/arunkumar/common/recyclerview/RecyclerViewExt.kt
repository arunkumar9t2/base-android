package dev.arunkumar.common.recyclerview

import androidx.annotation.LayoutRes
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.arunkumar.common.context.dpToPx

fun GridCalculator.applyTo(recyclerView: RecyclerView) {
    gridData().let { (column, itemSpacing) ->
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, column)
            updatePadding(left = itemSpacing, right = itemSpacing, top = itemSpacing / 2)
        }
    }
}

fun RecyclerView.autoGridSpacing(
    @LayoutRes referenceViewId: Int,
    parentWidth: () -> Int = { context.resources.displayMetrics.widthPixels },
    minimumSpacing: Int = context.dpToPx(2.0)
) = GridCalculator(context, referenceViewId, parentWidth, minimumSpacing).applyTo(this)
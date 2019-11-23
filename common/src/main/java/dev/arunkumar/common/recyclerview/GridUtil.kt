package dev.arunkumar.common.recyclerview

import android.content.Context
import android.view.View
import android.view.View.MeasureSpec.UNSPECIFIED
import androidx.annotation.LayoutRes
import androidx.annotation.Px
import dev.arunkumar.common.context.dpToPx

data class GridData(
    val columns: Int, // No of columns to display
    @param:Px val itemSpacing: Int // Spacing between each item
)

class GridCalculator(
    private val context: Context,
    @LayoutRes
    referenceViewId: Int,
    private val parentWidth: () -> Int = {
        context.resources.displayMetrics.widthPixels
    },
    @Px
    private val minimumSpacing: Int = context.dpToPx(2.0)
) {

    @Px
    private val eachItemWidth: Int = View
        .inflate(context, referenceViewId, null)
        .apply { measure(UNSPECIFIED, UNSPECIFIED) }
        .measuredWidth

    @Px
    private fun calcExtraSpace(noOfColumns: Int): Int =
        parentWidth() - (noOfColumns * eachItemWidth)

    @Px
    private fun itemSpacing(noOfColumns: Int) = calcExtraSpace(noOfColumns) / (noOfColumns + 1)

    /**
     * Calculates the no of column that can be displayed, taking [minimumSpacing] into account.
     */
    private fun calculateNoOfColumns(): Int {
        var noOfColumns = parentWidth() / eachItemWidth
        while (itemSpacing(noOfColumns) < minimumSpacing) {
            noOfColumns--
        }
        return noOfColumns
    }

    fun gridData(): GridData = calculateNoOfColumns().let { columns ->
        GridData(
            columns = columns,
            itemSpacing = itemSpacing(columns)
        )
    }
}
package dev.arunkumar.android.home.items

import android.content.Context
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.ModelView.Size
import com.airbnb.epoxy.TextProp
import com.airbnb.epoxy.paging.PagedListEpoxyController
import dev.arunkumar.android.R
import dev.arunkumar.android.data.Item

@ModelView(autoLayout = Size.MATCH_WIDTH_WRAP_HEIGHT)
class ItemView(context: Context) : AppCompatTextView(context) {

    init {
        gravity = Gravity.CENTER
        setTextAppearance(context, R.style.TextAppearance_AppCompat_Headline)
    }

    @TextProp
    fun text(name: CharSequence) {
        text = name
    }
}

class ItemsPagingController : PagedListEpoxyController<Item>() {

    override fun addModels(models: List<EpoxyModel<*>>) {
        itemView {
            id("Header")
            text("Header")
        }
        super.addModels(models)
    }

    override fun buildItemModel(
        currentPosition: Int,
        item: Item?
    ): EpoxyModel<*> = ItemViewModel_()
        .id(item?.id)
        .text(item?.id!!)
}
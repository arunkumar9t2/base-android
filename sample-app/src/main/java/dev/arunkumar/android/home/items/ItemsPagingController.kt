package dev.arunkumar.android.home.items

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.EpoxyAsyncUtil.getAsyncBackgroundHandler
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.ModelView.Size
import com.airbnb.epoxy.TextProp
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.jakewharton.rxrelay2.PublishRelay
import dev.arunkumar.android.R
import dev.arunkumar.android.data.Item
import io.reactivex.Observable


@ModelView(autoLayout = Size.MATCH_WIDTH_WRAP_HEIGHT)
class ItemView(context: Context) : AppCompatTextView(context) {

    init {
        gravity = Gravity.CENTER
        setTextAppearance(context, R.style.TextAppearance_AppCompat_Headline)
        background = StateListDrawable().apply {
            addState(
                intArrayOf(android.R.attr.state_pressed),
                ColorDrawable(ContextCompat.getColor(context, R.color.colorAccent))
            )
        }
    }

    @TextProp
    fun text(name: CharSequence) {
        text = name
    }

    @CallbackProp
    fun onClick(onClick: OnClickListener?) {
        setOnClickListener(onClick)
    }
}

class ItemsPagingController : PagedListEpoxyController<Item>(
    modelBuildingHandler = getAsyncBackgroundHandler(),
    diffingHandler = getAsyncBackgroundHandler()
) {

    private val clicksSubject = PublishRelay.create<Item>()
    val clicks: Observable<Item> = clicksSubject.hide()

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
        .onClick { _ ->
            clicksSubject.accept(item)
        }
}
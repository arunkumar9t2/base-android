package dev.arunkumar.android.home.items

import android.app.Activity
import com.airbnb.epoxy.EpoxyAsyncUtil.getAsyncBackgroundHandler
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.jakewharton.rxrelay2.PublishRelay
import dev.arunkumar.android.data.Item
import io.reactivex.Observable


class ItemsPagingController(private val activity: Activity) : PagedListEpoxyController<Item>(
    modelBuildingHandler = getAsyncBackgroundHandler(),
    diffingHandler = getAsyncBackgroundHandler()
) {

    private val clicksSubject = PublishRelay.create<Item>()
    val clicks: Observable<Item> = clicksSubject.hide()

    override fun addModels(models: List<EpoxyModel<*>>) {

        itemView {
            val header = (models.first() as ItemViewModel_).getText(activity).first().toString()
            id(header)
            text(header)
        }

        models.forEachIndexed { index, currEpoxyModel ->

            currEpoxyModel.addTo(this)

            if (index != models.size - 1) {
                val startingChar = (currEpoxyModel as ItemViewModel_).getText(activity).first()
                val nextEpoxyModel = models[index + 1]
                val nextChar = (nextEpoxyModel as ItemViewModel_).getText(activity).first()
                if (!startingChar.equals(nextChar, ignoreCase = true)) {
                    itemView {
                        val header = nextChar.toString()
                        id(header)
                        text(header)
                    }
                }
            }
        }
    }

    override fun buildItemModel(
        currentPosition: Int,
        item: Item?
    ): EpoxyModel<*> = ItemViewModel_()
        .id(item?.id)
        .text(item?.name!!)
        .onClick { _ -> clicksSubject.accept(item) }
}
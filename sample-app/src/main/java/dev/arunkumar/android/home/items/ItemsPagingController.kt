package dev.arunkumar.android.home.items

import android.app.Activity
import com.afollestad.rxkprefs.RxkPrefs
import com.airbnb.epoxy.EpoxyAsyncUtil.getAsyncBackgroundHandler
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.jakewharton.rxrelay2.PublishRelay
import dev.arunkumar.android.dagger.activity.PerActivity
import dev.arunkumar.android.data.Item
import dev.arunkumar.android.preferences.Preference
import dev.arunkumar.android.preferences.togglePreference
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

@PerActivity
class ItemsPagingController
@Inject
constructor(
  private val activity: Activity,
  private val rxkPrefs: RxkPrefs,
  private val schedulerProvider: SchedulerProvider
) : PagedListEpoxyController<Item>(
  modelBuildingHandler = getAsyncBackgroundHandler(),
  diffingHandler = getAsyncBackgroundHandler()
) {

  private val clicksSubject = PublishRelay.create<Item>()
  val clicks: Observable<Item> = clicksSubject.hide()

  private val togglePreference: Preference<Boolean> by lazy {
    val simplePreferenceId = "togglePreference"
    Preference<Boolean>(simplePreferenceId, "Title", "Subtitle").apply {
      value = rxkPrefs.boolean(simplePreferenceId)
    }
  }

  override fun addModels(models: List<EpoxyModel<*>>) {
    togglePreference {
      id(togglePreference.id)
      preference(togglePreference)
      schedulerProvider(schedulerProvider)
      summaryProvider { "${togglePreference.subTitle.toString()}: $it" }
    }

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
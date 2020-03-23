package dev.arunkumar.android.preferences

import androidx.core.view.isGone
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModelClass
import com.jakewharton.rxbinding2.widget.checkedChanges
import dev.arunkumar.android.epoxy.model.KotlinHolder
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import io.reactivex.functions.Function
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.layout_toggle_preference_layout.*
import kotlinx.android.synthetic.main.layout_toggle_preference_layout.view.*

@EpoxyModelClass(layout = R2.layout.layout_toggle_preference_layout)
abstract class TogglePreferenceModel :
  SingleValuePreferenceModel<Boolean, Boolean, TogglePreferenceModel.ViewHolder>() {

  @EpoxyAttribute
  override lateinit var preference: Preference<Boolean>

  @EpoxyAttribute(DoNotHash)
  override var summaryProvider: Function<Boolean, CharSequence?>? = null

  @EpoxyAttribute(DoNotHash)
  override lateinit var schedulerProvider: SchedulerProvider

  override val sourceMapper: (Boolean) -> Boolean = { it }

  override fun bind(holder: ViewHolder) {
    super.bind(holder)
    holder.containerView.preferenceText.text = preference.title
    holder.bindSummary(preference.subTitle)
    untilUnbind {
      preferenceChanges
        .subscribeBy(onNext = { (checked, summary) ->
          holder.containerView.post {
            holder.containerView.preferenceToggle.isChecked = checked
            holder.bindSummary(summary)
          }
        })
    }
    untilUnbind {
      holder.containerView.preferenceToggle
        .checkedChanges()
        .skipInitialValue()
        .subscribeBy(onNext = preference.value::set)
    }
  }

  class ViewHolder : KotlinHolder() {
    fun bindSummary(summary: CharSequence?) {
      preferenceSubtitle.text = summary
      preferenceSubtitle.isGone = summary == null
    }
  }
}
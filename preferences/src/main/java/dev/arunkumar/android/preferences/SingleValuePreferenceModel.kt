package dev.arunkumar.android.preferences

import com.afollestad.rxkprefs.rxjava.observe
import dev.arunkumar.android.epoxy.model.KotlinHolder
import dev.arunkumar.android.rx.deferObservable
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.functions.Function


abstract class SingleValuePreferenceModel<S, R, ViewHolder : KotlinHolder> : RxEpoxyModel<ViewHolder>() {
  abstract var preference: Preference<S>

  open var summaryProvider: Function<R, CharSequence?>? = null

  abstract var schedulerProvider: SchedulerProvider

  abstract val sourceMapper: (S) -> R

  protected data class PreferenceChange<R>(val value: R, val summary: CharSequence?)

  protected val preferenceChanges: Observable<PreferenceChange<R>> = deferObservable {
    preference.value.observe()
      .map { source -> sourceMapper(source) }
      .map { result -> PreferenceChange(result, summaryProvider?.apply(result)) }
      .compose(schedulerProvider.ioToUi())
      .share()
  }
}
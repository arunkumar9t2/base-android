package dev.arunkumar.android.rxschedulers

import dev.arunkumar.android.transformers.DefaultCompositeTransformer
import io.reactivex.Scheduler

interface SchedulerProvider {
    val ui: Scheduler
    val single: Scheduler
    val io: Scheduler
    val pool: Scheduler
    val new: Scheduler

    fun <T> ioToUi() = DefaultCompositeTransformer<T>(io, ui)
    fun <T> singleToUi() = DefaultCompositeTransformer<T>(single, ui)
    fun <T> poolToUi() = DefaultCompositeTransformer<T>(pool, ui)
    fun <T> newToUi() = DefaultCompositeTransformer<T>(new, ui)
}
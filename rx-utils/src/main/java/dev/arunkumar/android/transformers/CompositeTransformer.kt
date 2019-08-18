package dev.arunkumar.android.transformers

import io.reactivex.*
import org.reactivestreams.Publisher

interface CompositeTransformer<Type> :
    FlowableTransformer<Type, Type>,
    SingleTransformer<Type, Type>,
    CompletableTransformer,
    ObservableTransformer<Type, Type>

class DefaultCompositeTransformer<Type>
constructor(
    private val upstreamScheduler: Scheduler,
    private val downstreamScheduler: Scheduler
) : CompositeTransformer<Type> {

    override fun apply(upstream: Flowable<Type>): Publisher<Type> {
        return upstream.subscribeOn(upstreamScheduler).observeOn(downstreamScheduler)
    }

    override fun apply(upstream: Single<Type>): SingleSource<Type> {
        return upstream.subscribeOn(upstreamScheduler).observeOn(downstreamScheduler)
    }

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(upstreamScheduler).observeOn(downstreamScheduler)
    }

    override fun apply(upstream: Observable<Type>): ObservableSource<Type> {
        return upstream.subscribeOn(upstreamScheduler).observeOn(downstreamScheduler)
    }
}
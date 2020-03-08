package dev.arunkumar.android.util.work

import dagger.Subcomponent
import dev.arunkumar.android.dagger.workmanager.WorkerSubComponent
import dev.arunkumar.android.data.DeleteItemWorker

@Subcomponent(modules = [DeleteItemWorker.Builder::class])
interface SampleWorkerSubComponent : WorkerSubComponent {
    @Subcomponent.Factory
    interface Factory : WorkerSubComponent.Factory
}
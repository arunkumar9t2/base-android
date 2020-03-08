package dev.arunkumar.android.util.work

import dagger.Module
import dagger.Subcomponent
import dev.arunkumar.android.dagger.workmanager.WorkerInjectionModule
import dev.arunkumar.android.dagger.workmanager.WorkerSubComponent
import dev.arunkumar.android.data.DeleteItemWorker

@Module
interface SampleWorkerInjectionModule : WorkerInjectionModule<WorkerSubComponent.Factory>

@Subcomponent(
    modules = [
        DeleteItemWorker.Builder::class
    ]
)
interface SampleWorkerSubComponent : WorkerSubComponent {
    @Subcomponent.Factory
    interface Factory : WorkerSubComponent.Factory
}
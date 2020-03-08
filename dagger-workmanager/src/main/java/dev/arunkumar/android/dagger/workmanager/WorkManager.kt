package dev.arunkumar.android.dagger.workmanager

import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import dagger.Binds
import dagger.BindsInstance
import dagger.MapKey
import dagger.Module
import javax.inject.Provider
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(
    FUNCTION,
    PROPERTY_GETTER,
    PROPERTY_SETTER
)
@Retention(RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

/**
 * Base interface for a dagger module that exposes a [WorkerSubComponent.Factory]
 */
@Module
interface WorkerInjectionModule<T : WorkerSubComponent.Factory> {
    @Binds
    fun workerSubcomponenFactory(factory: T): WorkerSubComponent.Factory
}

/**
 * Base interface for a dagger component that exposes a binding for all workers in a map with key
 * as the class name and value as the provider.
 */
interface WorkerSubComponent {

    fun workers(): Map<Class<out ListenableWorker>, Provider<ListenableWorker>>

    interface Factory {
        fun create(@BindsInstance workerParameters: WorkerParameters): WorkerSubComponent
    }
}
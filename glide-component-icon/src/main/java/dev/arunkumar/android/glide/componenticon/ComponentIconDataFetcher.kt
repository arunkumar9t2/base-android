package dev.arunkumar.android.glide.componenticon

import android.content.ComponentName
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher

class ComponentIconDataFetcher(
    private val componentName: ComponentName
) : DataFetcher<ComponentIcon> {

    override fun loadData(
        priority: Priority,
        callback: DataFetcher.DataCallback<in ComponentIcon>
    ) {
        callback.onDataReady(ComponentIcon(componentName))
    }

    override fun cleanup() {}

    override fun cancel() {}

    override fun getDataClass(): Class<ComponentIcon> = ComponentIcon::class.java

    override fun getDataSource(): DataSource = DataSource.LOCAL
}
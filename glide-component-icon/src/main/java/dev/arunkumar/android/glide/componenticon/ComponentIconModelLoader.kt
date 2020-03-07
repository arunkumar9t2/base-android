package dev.arunkumar.android.glide.componenticon

import android.content.ComponentName
import android.net.Uri
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import dev.arunkumar.android.glide.componenticon.ComponentIcon.Companion.URI_SCHEME_COMPONENT_ICON

class ComponentIconModelLoader : ModelLoader<Uri, ComponentIcon> {

    override fun buildLoadData(
        model: Uri,
        width: Int,
        height: Int,
        options: Options
    ) = ComponentName.unflattenFromString(model.schemeSpecificPart)?.let {
        LoadData(ObjectKey(model), ComponentIconDataFetcher(it))
    }

    override fun handles(model: Uri) = model.scheme == URI_SCHEME_COMPONENT_ICON

    class Factory : ModelLoaderFactory<Uri, ComponentIcon> {

        override fun build(multiFactory: MultiModelLoaderFactory) = ComponentIconModelLoader()

        override fun teardown() {}
    }
}
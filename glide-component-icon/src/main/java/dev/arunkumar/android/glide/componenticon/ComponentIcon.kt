package dev.arunkumar.android.glide.componenticon

import android.content.ComponentName
import android.net.Uri

class ComponentIcon(val componentName: ComponentName) {

  companion object {
    const val URI_SCHEME_COMPONENT_ICON = "component-icon"

    fun createUri(componentName: ComponentName): Uri = Uri.fromParts(
      URI_SCHEME_COMPONENT_ICON,
      componentName.flattenToString(),
      null
    )
  }
}
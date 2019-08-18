package dev.arunkumar.common.model

import androidx.annotation.StringRes

data class StringResource(
    @param:StringRes val resource: Int,
    val args: Array<String>
)
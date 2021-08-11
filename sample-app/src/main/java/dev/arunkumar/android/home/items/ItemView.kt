/*
 * Copyright 2021 Arunkumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.arunkumar.android.home.items

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import dev.arunkumar.android.R
import dev.arunkumar.common.context.dpToPx

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ItemView(context: Context) : AppCompatTextView(context) {

  init {
    gravity = Gravity.CENTER
    background = StateListDrawable().apply {
      addState(
        intArrayOf(android.R.attr.state_pressed),
        ColorDrawable(
          ContextCompat.getColor(
            context,
            R.color.colorAccent
          )
        )
      )
    }
    val dp4 = context.dpToPx(4.0)
    updatePadding(top = dp4, bottom = dp4)
  }

  @TextProp
  fun text(name: CharSequence) {
    text = name
  }

  @CallbackProp
  fun onClick(onClick: OnClickListener?) {
    setOnClickListener(onClick)
  }
}

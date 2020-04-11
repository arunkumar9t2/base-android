package dev.arunkumar.android.home.items

import android.content.Context
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import dev.arunkumar.android.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ResetDbButton(context: Context) : AppCompatButton(context) {
  init {
    gravity = Gravity.CENTER
    setTextAppearance(
      context,
      R.style.TextAppearance_AppCompat_Headline
    )
    setText(R.string.reset_db)
  }

  @CallbackProp
  fun onClick(onClick: OnClickListener?) {
    setOnClickListener(onClick)
  }
}
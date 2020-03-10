package dev.arunkumar.android.home.items

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import dev.arunkumar.android.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ItemView(context: Context) : AppCompatTextView(context) {

    init {
        gravity = Gravity.CENTER
        setTextAppearance(context,
            R.style.TextAppearance_AppCompat_Headline
        )
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
package com.uoons.india.ui.order.order_tracker.extantion

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.uoons.india.utils.UoonsAplication

fun dpToPx(dp: Float): Int? {
    return UoonsAplication.instance?.resources?.let { dpToPx(dp, it) }
}

private fun dpToPx(dp: Float, resources: Resources): Int {
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    return px.toInt()
}

fun getColorCompat(resId: Int) = UoonsAplication.instance?.let { ContextCompat.getColor(it, resId) }

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

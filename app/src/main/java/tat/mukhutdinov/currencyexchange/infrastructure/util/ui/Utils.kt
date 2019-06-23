package tat.mukhutdinov.currencyexchange.infrastructure.util.ui

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager

object Utils {

    fun pxToDp(px: Float, resources: Resources) =
        px / resources.displayMetrics.scaledDensity

    fun dpToPx(dp: Float, resources: Resources) =
        dp * resources.displayMetrics.scaledDensity

    fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}
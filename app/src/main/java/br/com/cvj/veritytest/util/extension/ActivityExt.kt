package br.com.cvj.veritytest.util.extension

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusView = currentFocus
    if (currentFocusView != null) {
        inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
    }
}

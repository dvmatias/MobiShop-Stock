package com.cmdv.core.helpers

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.ref.WeakReference

class KeyboardHelper {

    companion object {
        fun hideKeyboard(activity: WeakReference<Activity>, v: View?) {
            if (v == null) return
            val inputManager = activity.get()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}
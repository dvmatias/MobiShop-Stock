package com.cmdv.core.navigator

import android.app.Activity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat

interface Navigator {

    fun toMainScreen(activityOrigin: Activity, bundle: Bundle?, options: ActivityOptionsCompat?, finish: Boolean)
    fun toAddProductScreen(activityOrigin: Activity, bundle: Bundle? = null, options: ActivityOptionsCompat? = null, finish: Boolean = false)
    fun toEditProductScreenForResult(activityOrigin: Activity, bundle: Bundle?, options: ActivityOptionsCompat?, requestCode: Int, finish: Boolean)
    fun toSearchScreen(activityOrigin: Activity, bundle: Bundle? = null, options: ActivityOptionsCompat? = null, finish: Boolean = false)

}
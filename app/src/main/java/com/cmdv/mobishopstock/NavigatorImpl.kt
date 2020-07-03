package com.cmdv.mobishopstock

import android.app.Activity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.cmdv.core.extensions.navigateForResultTo
import com.cmdv.core.extensions.navigateTo
import com.cmdv.core.navigator.Navigator
import com.cmdv.feature.CreateProductActivity
import com.cmdv.feature.EditProductActivity
import com.cmdv.feature.ui.MainActivity

class NavigatorImpl : Navigator {

    override fun toMainScreen(activityOrigin: Activity, bundle: Bundle?, options: ActivityOptionsCompat?, finish: Boolean) {
        activityOrigin.navigateTo<MainActivity>(bundle, options, finish)
    }

    override fun toAddProductScreen(activityOrigin: Activity, bundle: Bundle?, options: ActivityOptionsCompat?, finish: Boolean) {
        activityOrigin.navigateTo<CreateProductActivity>(bundle, options, finish)
    }

    override fun toEditProductScreenForResult(
        activityOrigin: Activity,
        bundle: Bundle?,
        options: ActivityOptionsCompat?,
        requestCode: Int,
        finish: Boolean
    ) {
        activityOrigin.navigateForResultTo<EditProductActivity>(bundle, options, requestCode, finish)
    }

}
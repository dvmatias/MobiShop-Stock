package com.cmdv.core.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout

fun expandShopCartItemBody(v: View) {
    v.measure(1, 1)
    val targetHeight: Int = v.measuredHeight
    v.layoutParams.height = 1
    v.visibility = View.VISIBLE

    val va: ValueAnimator = ValueAnimator.ofInt(v.height, targetHeight)
    va.addUpdateListener { valueAnimator ->
        v.layoutParams.height = valueAnimator.animatedValue as Int
        v.requestLayout()
    }
    va.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator?) {}

        override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
            v.tag = "expanded"
            v.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        override fun onAnimationEnd(p0: Animator?) {
            v.tag = "expanded"
            v.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        override fun onAnimationCancel(p0: Animator?) {}

        override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
            v.tag = "expanding"
        }

        override fun onAnimationStart(p0: Animator?) {
            v.tag = "expanding"
        }
    })

    va.duration = 300
    va.interpolator = DecelerateInterpolator()
    va.start()
}

fun collapseShopCartItemBody(v: View) {
    val initialHeight: Int = v.measuredHeight

    val va: ValueAnimator = ValueAnimator.ofInt(initialHeight, 0)
    va.addUpdateListener { valueAnimator ->
        v.layoutParams.height = valueAnimator.animatedValue as Int
        v.requestLayout()
    }
    va.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator?) {}

        override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
            v.tag = "collapsed"
            v.visibility = View.GONE
        }

        override fun onAnimationEnd(p0: Animator?) {
            v.tag = "collapsed"
            v.visibility = View.GONE
        }

        override fun onAnimationCancel(p0: Animator?) {}

        override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
            v.tag = "collapsing"
        }

        override fun onAnimationStart(p0: Animator?) {
            v.tag = "collapsing"
        }
    })

    va.duration = 300
    va.start()
}
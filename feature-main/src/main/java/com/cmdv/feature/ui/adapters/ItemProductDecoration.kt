package com.cmdv.feature.ui.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.helpers.DimensHelper.Companion.dpToPx

private const val DIVIDER_MARGIN_START: Float = 0F
private const val DIVIDER_HEIGHT: Float = 0.7F

class ItemProductDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    private var divider: Drawable? = null

    init {
        val a = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        divider = a.getDrawable(0)
        if (divider == null) {
            Log.w(
                ItemProductDecoration::class.java.simpleName,
                "@android:attr/listDivider was not set in the theme used for this "
                        + "DividerItemDecoration. Please set that attribute all call setDrawable()"
            )
        }
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView) {
        if (parent.layoutManager == null || parent.adapter == null) {
            return
        }

        c.save()
        val left = parent.paddingLeft + dpToPx(context, DIVIDER_MARGIN_START)
        val right = parent.width
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top: Float = child.bottom - dpToPx(context, DIVIDER_HEIGHT)

            divider?.apply {
                setBounds(child.left, top.toInt(), child.right, child.bottom)
                draw(c)
            }
        }
        c.restore()
    }
}
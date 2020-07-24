package com.cmdv.feature.ui.fragments.home.tabs

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val FIRST_POSITION: Int = 0

class ShopCartItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || parent.adapter == null) {
            return
        }

        val childPosition = parent.getChildAdapterPosition(view)
        // First item
        if (FIRST_POSITION == childPosition) {
            outRect.left = 0
        } else {
            outRect.top = 20
        }
    }

}
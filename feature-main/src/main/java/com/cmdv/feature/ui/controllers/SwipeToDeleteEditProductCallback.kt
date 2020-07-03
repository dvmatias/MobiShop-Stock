package com.cmdv.feature.ui.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.feature.R
import com.cmdv.feature.ui.adapters.RecyclerProductAdapter


private const val dragNotAllowedFlag: Int = 0
private const val swipeNotAllowedFlag: Int = 0
private const val swipeLeftRightFlags: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

enum class ButtonShowedState {
    GONE,
    DELETE_VISIBLE,
    EDIT_VISIBLE
}

class SwipeToDeleteEditProductCallback(context: Context) : ItemTouchHelper.Callback() {

    private var buttonShowedState: ButtonShowedState = ButtonShowedState.GONE
    private var swipeBack: Boolean = false
    private var paint: Paint
    private var mBackground: ColorDrawable? = null
    private var backgroundDeleteColor = 0
    private var backgroundEditColor = 0
    private var deleteDrawable: Drawable? = null
    private var editDrawable: Drawable? = null
    private var intrinsicWidth = 0
    private var intrinsicHeight = 0

    init {
        mBackground = ColorDrawable()
        backgroundDeleteColor = Color.parseColor("#b80f0a")
        backgroundEditColor = Color.parseColor("#1BBF04")
        paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        deleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_item_product_delete)
        editDrawable = ContextCompat.getDrawable(context, R.drawable.ic_item_product_edit)
        intrinsicWidth = deleteDrawable!!.intrinsicWidth
        intrinsicHeight = deleteDrawable!!.intrinsicHeight
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        logErrorMessage("getMovementFlags()")
        return if (viewHolder is RecyclerProductAdapter.SectionViewHolder)
            makeMovementFlags(dragNotAllowedFlag, swipeNotAllowedFlag)
        else
            makeMovementFlags(dragNotAllowedFlag, swipeLeftRightFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean =
        false

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        logErrorMessage("setTouchListener()")
        recyclerView.setOnTouchListener { _, event ->
            swipeBack =
                event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP

            if (dX < -5) {
                buttonShowedState = ButtonShowedState.DELETE_VISIBLE
            } else if (dX > 5) {
                buttonShowedState = ButtonShowedState.EDIT_VISIBLE
            }

            if (buttonShowedState != ButtonShowedState.GONE) {
                setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                setItemsClickable(recyclerView, false)
            }
            false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView, b: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = b
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        currentlyActive: Boolean
    ) {
        logErrorMessage("setTouchDownListener()")
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, currentlyActive)
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        currentlyActive: Boolean
    ) {
        logErrorMessage("setTouchUpListener()")
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                recyclerView.setOnTouchListener { _, _ -> false }
                onChildDraw(c, recyclerView, viewHolder, 0f, dY, actionState, currentlyActive)
                setItemsClickable(recyclerView, true)
                swipeBack = false
                buttonShowedState = ButtonShowedState.GONE
            }
            false
        }
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        logErrorMessage("convertToAbsoluteDirection()")
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        logErrorMessage("onSwiped $direction")
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        logErrorMessage("getSwipeThreshold()")
        return 0.7f
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        logErrorMessage("onChildDraw()")
        if (actionState == ACTION_STATE_SWIPE) {
            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        val itemView: View = viewHolder.itemView
        val itemHeight: Int = itemView.height

        val isCancelled: Boolean = (dX == 0F && !isCurrentlyActive)

        if (isCancelled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        if (buttonShowedState == ButtonShowedState.DELETE_VISIBLE) {
            mBackground?.let {
                it.color = backgroundDeleteColor
                it.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                it.draw(c)
            }

            val deleteIconTop: Int = itemView.top + (itemHeight - intrinsicHeight) / 2
            val deleteIconMargin: Int = (itemHeight - intrinsicHeight) / 2
            val deleteIconLeft: Int = itemView.right - deleteIconMargin
            val deleteIconRight: Int = itemView.right - deleteIconMargin + intrinsicWidth
            val deleteIconBottom: Int = deleteIconTop + intrinsicHeight

            deleteDrawable?.let {
                it.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                it.draw(c)
            }

            val p = Paint()
            p.color = Color.WHITE
            p.isAntiAlias = true
            p.textSize = 50F
            val textWidth = p.measureText("Delete")
            c.drawText("Delete", deleteIconLeft.toFloat() - textWidth - 30F, deleteIconTop.toFloat() + 50F, p)

        } else if ((buttonShowedState == ButtonShowedState.EDIT_VISIBLE)) {
            mBackground?.let {
                it.color = backgroundEditColor
                it.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + dX.toInt(),
                    itemView.bottom
                )
                it.draw(c)
            }

            val editIconTop: Int = itemView.top + (itemHeight - intrinsicHeight) / 2
            val editIconMargin: Int = (itemHeight - intrinsicHeight) / 2
            val editIconLeft: Int = itemView.left + editIconMargin - intrinsicWidth
            val editIconRight: Int = itemView.left + editIconMargin
            val editIconBottom: Int = editIconTop + intrinsicHeight

            editDrawable?.let {
                it.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
                it.draw(c)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, paint)
    }

}
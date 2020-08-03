package com.cmdv.feature.ui.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.TextPaint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.core.helpers.DimensHelper
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.feature.R
import com.cmdv.feature.ui.adapters.ProductRecyclerAdapter
import kotlin.math.abs


private const val DRAG_NOT_ALLOWED_FLAGS: Int = 0
private const val SWIPE_NOT_ALLOWED_FLAG: Int = 0
private const val SWIPE_LEFT_RIGHT_FLAGS: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
private const val FULL_BACKGROUND_ALPHA: Int = 255

enum class ButtonShowedState {
    GONE,
    ADD_TO_CART_VISIBLE,
    EDIT_VISIBLE
}

class SwipeToAddToCartOrEditCallback(val context: Context, private val swipeActionListener: SwipeActionListener) : ItemTouchHelper.Callback() {

    private var buttonShowedState: ButtonShowedState = ButtonShowedState.GONE
    private var swipeBack: Boolean = false
    private var paint: Paint
    private var editBackgroundDrawable: Drawable? = null
    private var addToCartBackgroundDrawable: Drawable? = null
    private var editDrawable: Drawable? = null
    private var addToCartDrawable: Drawable? = null
    private var intrinsicWidth = 0
    private var intrinsicHeight = 0
    private var minDxToTriggerAction: Int = 0

    init {
        editBackgroundDrawable =
            ColorDrawable(ContextCompat.getColor(context, R.color.colorEditProductBackground)).mutate()
        addToCartBackgroundDrawable =
            ColorDrawable(ContextCompat.getColor(context, R.color.colorAddToCartProductBackground)).mutate()
        paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        addToCartDrawable = ContextCompat.getDrawable(context, R.drawable.ic_item_product_delete)
        editDrawable = ContextCompat.getDrawable(context, R.drawable.ic_edit_product)
        intrinsicWidth = DimensHelper.dpToPx(context, 36F).toInt()
        intrinsicHeight = DimensHelper.dpToPx(context, 36F).toInt()
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        logErrorMessage("getMovementFlags()")
        return if (viewHolder is ProductRecyclerAdapter.SectionViewHolder)
            makeMovementFlags(DRAG_NOT_ALLOWED_FLAGS, SWIPE_NOT_ALLOWED_FLAG)
        else
            makeMovementFlags(DRAG_NOT_ALLOWED_FLAGS, SWIPE_LEFT_RIGHT_FLAGS)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean =
        false

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        logErrorMessage("setTouchListener()")
        recyclerView.setOnTouchListener { _, event ->
            if (dX < -5) {
                buttonShowedState = ButtonShowedState.ADD_TO_CART_VISIBLE
            } else if (dX > 5) {
                buttonShowedState = ButtonShowedState.EDIT_VISIBLE
            }

            swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (buttonShowedState != ButtonShowedState.GONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    setItemsClickable(recyclerView, false)
                    triggerAction(dX, viewHolder.adapterPosition)
                }
            }
            false
        }
    }
    
    private fun triggerAction(dX: Float, position: Int) {
        if (abs(dX) >= minDxToTriggerAction) {
            logErrorMessage("Should trigger?? dX = $dX")
            // TODO this should trigger the action once the back animation is done. this delay emulates this. Refactor.
            Handler().postDelayed({
                if (buttonShowedState == ButtonShowedState.EDIT_VISIBLE) {
                    swipeActionListener.onActionEdit(position)
                } else if (buttonShowedState == ButtonShowedState.ADD_TO_CART_VISIBLE) {
                    swipeActionListener.onActionAddToCart(position)
                }
            }, 100)
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
            when (event.action) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_MOVE -> {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, currentlyActive)
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        @Suppress("UNUSED_PARAMETER") dX: Float,
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
        val itemWidth: Int = itemView.width
        minDxToTriggerAction = (itemWidth * 0.3).toInt()

        val isCancelled: Boolean = (dX == 0F && !isCurrentlyActive)
        if (isCancelled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        val alpha = getButtonAlpha(dX)
        if (buttonShowedState == ButtonShowedState.ADD_TO_CART_VISIBLE) {
            addToCartBackgroundDrawable?.let {
                it.alpha = alpha
                it.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                it.draw(c)
            }

            val addToCartIconTop: Int = itemView.top + (itemHeight - intrinsicHeight) / 2
            val addToCartIconMargin: Int = (itemHeight - intrinsicHeight) / 2
            val addToCartIconLeft: Int = itemView.right - addToCartIconMargin
            val addToCartIconRight: Int = itemView.right - addToCartIconMargin + intrinsicWidth
            val addToCartIconBottom: Int = addToCartIconTop + intrinsicHeight

            addToCartDrawable?.let {
                it.setBounds(addToCartIconLeft, addToCartIconTop, addToCartIconRight, addToCartIconBottom)
                it.draw(c)
            }

            val p = TextPaint()
            p.color = Color.WHITE
            p.alpha = if (alpha == 255) alpha else 0
            p.isAntiAlias = true
            p.textSize = 50F
            val textWidth: Float = p.measureText(context.getString(R.string.action_swipe_add_to_cart))
            c.drawText(context.getString(R.string.action_swipe_add_to_cart), addToCartIconLeft.toFloat() - textWidth - 30F, addToCartIconTop.toFloat() + 50F, p)

        } else if ((buttonShowedState == ButtonShowedState.EDIT_VISIBLE)) {
            editBackgroundDrawable?.let {
                it.alpha = alpha
                it.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + dX.toInt(),
                    itemView.bottom
                )
                it.draw(c)
            }

            val editIconTop: Int = itemView.top + (itemHeight - intrinsicHeight) / 2
            val editIconLeft = 50
            val editIconRight: Int = 50 + intrinsicWidth
            val editIconBottom: Int = editIconTop + intrinsicHeight

            editDrawable?.let {
                it.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
                it.draw(c)
            }

            val p = TextPaint()
            p.color = Color.WHITE
            p.alpha = if (alpha == 255) alpha else 0
            p.isAntiAlias = true
            p.textSize = 50F
            c.drawText(context.getString(R.string.action_swipe_edit), editIconRight + 50F, ((itemView.bottom.toFloat() - itemView.top.toFloat()) / 2) + itemView.top + 25F, p)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

    }

    private fun getButtonAlpha(dX: Float): Int {
        val alpha: Int = (FULL_BACKGROUND_ALPHA * (abs(dX) / minDxToTriggerAction)).toInt()
        logErrorMessage("alpha $alpha")
        return if (alpha >= FULL_BACKGROUND_ALPHA) FULL_BACKGROUND_ALPHA else if (alpha <= FULL_BACKGROUND_ALPHA * 0.7) 0 else alpha
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, paint)
    }

    /**
     * Interface to communicate actions.
     */
    interface SwipeActionListener {
        fun onActionEdit(position: Int)
        fun onActionAddToCart(position: Int)
    }

}
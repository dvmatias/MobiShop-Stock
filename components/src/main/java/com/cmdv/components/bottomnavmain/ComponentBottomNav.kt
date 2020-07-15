package com.cmdv.components.bottomnavmain

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.components.R
import com.cmdv.domain.models.ItemMainPageModel
import kotlinx.android.synthetic.main.bottom_nav_main_component.view.*

class ComponentBottomNav : ConstraintLayout, RecyclerBottomNavMainAdapter.OnItemClickListener {

    private var currentPosition: Int = -1
    private var oldPosition: Int = -1
    private var listener: OnBottomNavMainItemSelectedListener? = null
    private lateinit var adapter: RecyclerBottomNavMainAdapter

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initView(context)
    }

    /**
     * Init view.
     */
    private fun initView(context: Context) {
        View.inflate(context, R.layout.bottom_nav_main_component, this)
    }

    /**
     * Function that allows Activity to set the item selected listener.
     */
    fun setup(itemMainPageList: MutableList<ItemMainPageModel>, listener: OnBottomNavMainItemSelectedListener?) {
        this.listener = listener
        apply {
            this.adapter = RecyclerBottomNavMainAdapter(context, this, itemMainPageList)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = this@ComponentBottomNav.adapter
            }
        }
    }

    /**
     * [RecyclerBottomNavMainAdapter.OnItemClickListener] implementation.
     */
    override fun onItemClick(position: Int) {
        this.oldPosition = currentPosition
        this.currentPosition = position
        val selectedView = recyclerView.findViewHolderForLayoutPosition(currentPosition)?.itemView
        val unselectedView = recyclerView.findViewHolderForLayoutPosition(oldPosition)?.itemView
        when (position) {
            oldPosition -> listener?.onItemReselected(selectedView)
            else -> {
                listener?.apply {
                    onItemUnselected(unselectedView)
                    onItemSelected(selectedView, position)
                }
                adapter.updateSelected(selectedView, unselectedView)
                recyclerView.post { adapter.notifyDataSetChanged() }
            }
        }
    }

    /**
     * Interface to be implemented by calling Activity.
     */
    interface OnBottomNavMainItemSelectedListener {
        fun onItemSelected(view: View?, position: Int)
        fun onItemReselected(view: View?)
        fun onItemUnselected(view: View?)
    }

}
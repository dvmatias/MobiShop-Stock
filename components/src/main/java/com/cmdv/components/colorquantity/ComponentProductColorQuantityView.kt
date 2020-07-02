package com.cmdv.components.colorquantity

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.components.R
import com.cmdv.core.utils.logErrorMessage
import kotlinx.android.synthetic.main.product_color_view_component.view.*

private const val MODE_EDIT = 0
private const val MODE_NO_EDIT = 1
enum class Mode(val value: Int) {
    EDIT(MODE_EDIT),
    NO_EDIT(MODE_NO_EDIT)
}

class ComponentProductColorView : ConstraintLayout {

    val mutableLiveItemList = MutableLiveData<ArrayList<Pair<String, Int>>>()

    private var colorQuantityList: ArrayList<ColorQuantity> = arrayListOf()

    private lateinit var colorQuantityAdapter: RecyclerColorQuantityAdapter

    private var viewMode: Mode =
        Mode.NO_EDIT

    /**
     * Constructor.
     */
    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    /**
     * Constructor.
     */
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(context, attributeSet)
    }

    /**
     * Constructor.
     */
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
        initView(context, attributeSet)
    }

    /**
     * Init view.
     */
    private fun initView(context: Context, @Suppress("UNUSED_PARAMETER") attrs: AttributeSet?) {
        View.inflate(context, R.layout.product_color_view_component, this)
        setRecycler(context)
        setup(Mode.NO_EDIT, arrayListOf(), context)
    }

    /**
     * Setup recycler view.
     */
    private fun setRecycler(context: Context) {
        this.colorQuantityAdapter = RecyclerColorQuantityAdapter(context)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = colorQuantityAdapter
        }
    }

    /**
     *
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun setup(mode: Mode, colorQuantityList: ArrayList<ColorQuantity>?, context: Context) {
        setColorQuantityList(colorQuantityList)
        when (mode) {
            Mode.EDIT -> setEditMode(context)
            Mode.NO_EDIT -> setNoEditMode()
        }
    }

    private fun setColorQuantityList(colorQuantityList: ArrayList<ColorQuantity>?) {
        if (colorQuantityList == null) return
        this.colorQuantityList.apply {
            clear()
            addAll(colorQuantityList)
        }
    }

    private fun setEditMode(context: Context) {
        this.viewMode = Mode.EDIT
        setButton()

        this.colorQuantityAdapter.setEditModeAdapter()
        this.colorQuantityAdapter.mutableLiveItemList.observe(context as LifecycleOwner, Observer {
            this.mutableLiveItemList.value = it
        })
    }

    private fun setNoEditMode() {
        this.viewMode = Mode.NO_EDIT
        setButton()

        this.colorQuantityAdapter.setNoEditModeAdapter(this.colorQuantityList)
    }

    /**
     *
     */
    private fun setButton() {
        if (Mode.EDIT == this.viewMode) {
            addColorQuantityBtn.apply {
                visibility = View.VISIBLE
                setOnClickListener { onAddButtonClick() }
            }
        } else {
            addColorQuantityBtn.visibility = View.GONE
        }
    }

    /**
     *
     */
    private fun onAddButtonClick() {
        this.colorQuantityAdapter.addEditableViewIfPossible()
    }

    class ColorQuantity(
        val name: String,
        val quantity: Int
    )

}
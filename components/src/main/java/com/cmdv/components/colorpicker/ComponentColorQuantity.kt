package com.cmdv.components.colorpicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmdv.components.R
import com.cmdv.core.utils.logErrorMessage
import com.cmdv.domain.models.ProductModel
import kotlinx.android.synthetic.main.color_quantity_component.view.*

enum class Mode {
    EDIT,
    NO_EDIT
}

class ComponentColorQuantity : ConstraintLayout, OnColorQuantityClickListener {

    private var mode: Mode = Mode.NO_EDIT
    private lateinit var coloQuantityAdapter: RecyclerColoQuantityAdapter
    private var _mutableLiveColorQuantityList = MutableLiveData<ArrayList<ProductModel.ColorQuantityModel>>()
    val mutableLiveColorQuantityList: MutableLiveData<ArrayList<ProductModel.ColorQuantityModel>>
        get() = _mutableLiveColorQuantityList

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
        View.inflate(context, R.layout.color_quantity_component, this)
    }

    /**
     * Setup [ComponentColorQuantity] view.
     *
     * @param context [Context]
     * @param mode [Mode]: Edit/No Edit.
     * @param colorQuantities
     */
    fun setup(context: Context, mode: Mode, colorQuantities: ArrayList<ProductModel.ColorQuantityModel>?) {
        this.mode = mode
        when (this.mode) {
            Mode.EDIT -> {
                setEditMode(context)
            }
            Mode.NO_EDIT -> {
                setNoEditMode()
            }
        }
        setupRecycler(colorQuantities)
    }

    private fun setEditMode(context: Context) {
        buttonAddColorQuantity.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                openColorPickerDialog(null)
            }
        }
    }

    private fun setNoEditMode() {
        buttonAddColorQuantity.visibility = View.GONE
    }

    private fun setupRecycler(colorQuantities: ArrayList<ProductModel.ColorQuantityModel>?) {
        coloQuantityAdapter = RecyclerColoQuantityAdapter(this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = coloQuantityAdapter
        }
        colorQuantities?.let {
            coloQuantityAdapter.setItems(colorQuantities)
        }
    }

    /**
     * [OnColorQuantityClickListener] implementation.
     */
    override fun onClick(position: Int, colorQuantity: ProductModel.ColorQuantityModel) {
        logErrorMessage("onClick($position: Int, $colorQuantity: ColorQuantityModel)")
        if (this.mode == Mode.EDIT) {
            openColorPickerDialog(Pair(position, colorQuantity))
        }
    }

    private fun openColorPickerDialog(pairPositionColorQuantity: Pair<Int, ProductModel.ColorQuantityModel>?) {
        ComponentColorQuantityPickerDialog(context).show(pairPositionColorQuantity, onColorQuantitySetListener)
    }

    private val onColorQuantitySetListener = object : OnColorQuantitySetListener {
        override fun onColorQuantityCreated(colorQuantity: ProductModel.ColorQuantityModel) {
            coloQuantityAdapter.addItem(colorQuantity)
            updateColorQuantityList()
        }

        override fun onColorQuantityUpdated(position: Int, colorQuantity: ProductModel.ColorQuantityModel) {
            coloQuantityAdapter.updateItem(position, colorQuantity)
            updateColorQuantityList()
        }

        override fun onColorQuantityDeleted(position: Int) {
            coloQuantityAdapter.deleteItem(position)
            updateColorQuantityList()
        }
    }

    private fun updateColorQuantityList() {
        _mutableLiveColorQuantityList.value = coloQuantityAdapter.getItems()
    }

}
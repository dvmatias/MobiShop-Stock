package com.cmdv.components.colorpicker

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import com.cmdv.components.R
import com.cmdv.core.Constants.Companion.DIALOG_WIDTH_DISPLAY_PERCENTAGE
import com.cmdv.core.helpers.DisplayHelper
import com.cmdv.domain.models.ProductModel
import kotlinx.android.synthetic.main.component_colo_picker_dialog.*

internal class ComponentColorQuantityPickerDialog(context: Context) : Dialog(context) {

    private lateinit var recyclerColorAdapter: RecyclerColorsAdapter
    private lateinit var spinnerQuantityAdapter: SpinnerQuantityAdapter
    private var isUpdate: Boolean = false
    private lateinit var listener: OnColorQuantitySetListener
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.component_colo_picker_dialog)
        window?.setLayout(
            (DisplayHelper.getDisplayWidthPx() * DIALOG_WIDTH_DISPLAY_PERCENTAGE).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        buttonDelete.setOnClickListener {
            if (this.isUpdate) {
                listener.onColorQuantityDeleted(position)
            }
            dismiss()
        }
        buttonNegative.setOnClickListener { dismiss() }
        buttonPositive.setOnClickListener {
            val selectedColorValue: Int = recyclerColorAdapter.getSelectedColorValue()
            val selectedColorName: String = recyclerColorAdapter.getSelectedColorName()
            val selectedQuantity: Int = spinnerQuantity.selectedItem as Int
            val colorQuantityModel: ProductModel.ColorQuantityModel =
                ProductModel.ColorQuantityModel(selectedColorName, selectedColorValue.toString(), selectedQuantity)
            if (this.isUpdate) {
                listener.onColorQuantityUpdated(position, colorQuantityModel)
            } else {
                listener.onColorQuantityCreated(colorQuantityModel)
            }
            dismiss()
        }
    }

    fun show(pairPositionColorQuantity: Pair<Int, ProductModel.ColorQuantityModel>?, onColorQuantitySetListener: OnColorQuantitySetListener) {
        this.show()
        this.listener = onColorQuantitySetListener
        if (pairPositionColorQuantity != null) {
            this.isUpdate = true
            this.position = pairPositionColorQuantity.first
        } else {
            this.buttonDelete.visibility = View.GONE
        }
        setupColorRecycler(pairPositionColorQuantity)
        setupQuantitySpinner(pairPositionColorQuantity)
    }

    private fun setupColorRecycler(pairPositionColorQuantity: Pair<Int, ProductModel.ColorQuantityModel>?) {
        recyclerColorAdapter = RecyclerColorsAdapter(context)
        recyclerViewColors.let {
            it.layoutManager = GridLayoutManager(context, 4)
            it.adapter = recyclerColorAdapter
        }
        if (this.isUpdate) {
            recyclerColorAdapter.setSelected(pairPositionColorQuantity!!.second.value.toInt())
        }
    }

    private fun setupQuantitySpinner(pairPositionColorQuantity: Pair<Int, ProductModel.ColorQuantityModel>?) {
        spinnerQuantityAdapter = SpinnerQuantityAdapter(context)
        spinnerQuantity.adapter = spinnerQuantityAdapter
        if (this.isUpdate) {
            spinnerQuantity.setSelection(pairPositionColorQuantity!!.second.quantity - 1)
        }
    }

}
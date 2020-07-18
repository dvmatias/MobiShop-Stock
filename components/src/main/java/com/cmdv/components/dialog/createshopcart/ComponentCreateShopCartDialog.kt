package com.cmdv.components.dialog.createshopcart

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import com.cmdv.components.R
import com.cmdv.core.Constants.Companion.DIALOG_WIDTH_DISPLAY_PERCENTAGE
import com.cmdv.core.helpers.DisplayHelper
import kotlinx.android.synthetic.main.dialog_create_shop_cart_component.*

class ComponentCreateShopCartDialog(context: Context, private val listener: CreateShopCartDialogListener) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_create_shop_cart_component)

        setupWindowParams()
        setupPositiveButton()
        setupNegativeButton()
    }

    private fun setupWindowParams() {
        window?.setLayout(
            (DisplayHelper.getDisplayWidthPx() * DIALOG_WIDTH_DISPLAY_PERCENTAGE).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupPositiveButton() {
        textViewPositiveButton.setOnClickListener {
            listener.onCreateShopCartDialogPositiveClick(editTextShopCartName.text.toString())
            dismiss()
        }
    }

    private fun setupNegativeButton() {
        textViewNegativeButton.setOnClickListener {
            dismiss()
        }
    }

}
package com.cmdv.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cmdv.core.Constants.Companion.EXTRA_PRODUCT_KEY
import com.cmdv.core.utils.logInfoMessage
import com.cmdv.domain.models.ProductModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_product.*
import org.koin.android.ext.android.inject

class EditProductActivity : AppCompatActivity() {

    private val gson: Gson by inject()

    private var productOriginal: ProductModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        getExtras()

        setSupportActionBar(toolbar)
    }

    private fun getExtras() {
        intent.extras?.let {
            if (it.containsKey(EXTRA_PRODUCT_KEY)) {
                productOriginal =
                    gson.fromJson(it.getString(EXTRA_PRODUCT_KEY), ProductModel::class.java)
            }
        }
    }
}   
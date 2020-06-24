package com.cmdv.feature.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cmdv.core.navigator.Navigator
import com.cmdv.feature.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    val viewModel: MainActivityViewModel by viewModel()

    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        setupCreateProductButton()
        setupRecyclerProduct()
    }

    private fun setupCreateProductButton() {
        fab.setOnClickListener {
            navigator.toAddProductScreen(
                this,
                null,
                null,
                false
            )
        }
    }

    private fun setupRecyclerProduct() {
        // TODO
    }
}

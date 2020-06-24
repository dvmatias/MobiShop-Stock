package com.cmdv.feature.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cmdv.core.navigator.Navigator
import com.cmdv.core.utils.logInfoMessage
import com.cmdv.data.repositories.ProductRepositoryImpl
import com.cmdv.feature.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupCreateProductButton()
        setupViewModel()
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

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(this, MainActivityViewModelFactory(ProductRepositoryImpl())).get(
                MainActivityViewModel::class.java
            )
    }

    private fun setupRecyclerProduct() {
        // TODO
    }
}

package com.cmdv.feature.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmdv.data.repositories.ProductRepositoryImpl
import com.cmdv.feature.R
import com.cmdv.feature.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        setSupportActionBar(binding.toolbar)
        setupViewModel()
        setupRecyclerProduct()
    }

    private fun setupDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(this, MainActivityViewModelFactory(ProductRepositoryImpl())).get(
                MainActivityViewModel::class.java
            )
    }

    private fun setupRecyclerProduct() {
        
    }
}

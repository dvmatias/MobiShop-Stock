package com.cmdv.feature.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmdv.core.Constants
import com.cmdv.domain.repositories.ShopCartRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel(
    private val shopCartRepository: ShopCartRepository
) : ViewModel() {

}
package com.cmdv.domain.models

import androidx.fragment.app.Fragment

data class ItemMainPageModel(
    val label: String,
    val tag: String,
    val icon: Int,
    val iconSelected: Int,
    val fragment: Fragment,
    val enable: Boolean
)

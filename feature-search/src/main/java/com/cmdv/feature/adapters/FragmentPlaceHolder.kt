package com.cmdv.feature.adapters

import androidx.fragment.app.Fragment

abstract class FragmentPlaceHolder: Fragment() {

    abstract fun newInstance(): Fragment

}

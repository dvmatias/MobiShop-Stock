package com.cmdv.components

import androidx.fragment.app.Fragment

abstract class TabFragmentPlaceHolder: Fragment() {

    abstract fun newInstance(): Fragment

}

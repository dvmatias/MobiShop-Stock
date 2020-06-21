package com.cmdv.mobishopstock

import com.cmdv.core.navigator.Navigator
import org.koin.dsl.module

val appModule = module {

    single<Navigator> { NavigatorImpl() }

}
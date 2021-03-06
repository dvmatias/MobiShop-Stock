package com.cmdv.mobishopstock

import android.app.Application
import com.cmdv.core.navigator.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MobiShopStockApp : Application() {

    private lateinit var navigator: Navigator

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initNavigator()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MobiShopStockApp)
            modules(appModule, dataSourceModule, repositoryModule, viewModelModule, adapterModule, itemDecorationModule, librariesModule)
        }
    }

    private fun initNavigator() {
        navigator = NavigatorImpl()
    }

}
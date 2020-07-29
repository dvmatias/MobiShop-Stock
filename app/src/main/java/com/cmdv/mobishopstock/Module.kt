package com.cmdv.mobishopstock

import com.cmdv.core.navigator.Navigator
import com.cmdv.data.datasources.db.ShopCartDatabase
import com.cmdv.data.datasources.firebase.AuthFirebaseSourceImpl
import com.cmdv.data.datasources.firebase.FirebaseUserSourceImpl
import com.cmdv.data.repositories.*
import com.cmdv.domain.datasources.firebase.AuthFirebaseSource
import com.cmdv.domain.datasources.firebase.UserFirebaseSource
import com.cmdv.domain.repositories.*
import com.cmdv.feature.CreateProductActivityViewModel
import com.cmdv.feature.EditProductActivityViewModel
import com.cmdv.feature.SplashActivityViewModel
import com.cmdv.feature.ui.MainActivityViewModel
import com.cmdv.feature.ui.decorations.ItemProductDecoration
import com.cmdv.feature.ui.adapters.RecyclerProductAdapter
import com.cmdv.feature.ui.fragments.home.tabs.MainTabProductListFragmentViewModel
import com.cmdv.feature.ui.fragments.home.tabs.MainTabShopCartFragmentViewModel
import com.cmdv.feature.ui.fragments.sales.MainSalesFragmentViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Navigator> { NavigatorImpl() }
}

val dataSourceModule = module {
    single<AuthFirebaseSource> { AuthFirebaseSourceImpl() }
    single<UserFirebaseSource> { FirebaseUserSourceImpl() }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl() }
    single<ShopCartRepository> { ShopCartRepositoryImpl(ShopCartDatabase.getInstance(get()).shopCartDAO) }
    single<SaleRepository> { SaleRepositoryImpl() }
}

val viewModelModule = module {
    viewModel { SplashActivityViewModel(get(), get()) }

    viewModel { MainActivityViewModel(get(), get(), get()) }
    viewModel { MainTabProductListFragmentViewModel(get()) }
    viewModel { MainTabShopCartFragmentViewModel(get()) }
    viewModel { MainSalesFragmentViewModel(get()) }

    viewModel { CreateProductActivityViewModel(get()) }

    viewModel { EditProductActivityViewModel(get()) }
}

val adapterModule = module {
    single { RecyclerProductAdapter(get()) }
}

val itemDecorationModule = module {
    single {
        ItemProductDecoration(
            androidContext()
        )
    }
}

val librariesModule = module {
    single { Gson() }
}
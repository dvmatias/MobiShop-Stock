package com.cmdv.mobishopstock

import com.cmdv.core.navigator.Navigator
import com.cmdv.data.datasources.FirebaseAuthSourceImpl
import com.cmdv.data.datasources.FirebaseUserSourceImpl
import com.cmdv.data.repositories.AuthRepositoryImpl
import com.cmdv.data.repositories.ProductRepositoryImpl
import com.cmdv.data.repositories.UserRepositoryImpl
import com.cmdv.domain.datasources.FirebaseAuthSource
import com.cmdv.domain.datasources.FirebaseUserSource
import com.cmdv.domain.repositories.AuthRepository
import com.cmdv.domain.repositories.ProductRepository
import com.cmdv.domain.repositories.UserRepository
import com.cmdv.feature.CreateProductActivityViewModel
import com.cmdv.feature.EditProductActivityViewModel
import com.cmdv.feature.SplashActivityViewModel
import com.cmdv.feature.ui.MainActivityViewModel
import com.cmdv.feature.ui.decorations.ItemProductDecoration
import com.cmdv.feature.ui.adapters.RecyclerProductAdapter
import com.cmdv.feature.ui.fragments.MainProductListFragmentViewModel
import com.cmdv.feature.ui.fragments.salestabs.ShopCartSectionTabFragmentViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Navigator> { NavigatorImpl() }
}

val dataSourceModule = module {
    single<FirebaseAuthSource> { FirebaseAuthSourceImpl() }
    single<FirebaseUserSource> { FirebaseUserSourceImpl() }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl() }
}

val viewModelModule = module {
    viewModel { SplashActivityViewModel(get(), get()) }

    viewModel { MainActivityViewModel(get()) }
    viewModel { MainProductListFragmentViewModel(get()) }
    viewModel { ShopCartSectionTabFragmentViewModel() }

    viewModel { CreateProductActivityViewModel(get()) }
    viewModel { EditProductActivityViewModel(get()) }
}

val adapterModule = module {
    single<RecyclerProductAdapter> { RecyclerProductAdapter(androidContext()) }
}

val itemDecorationModule = module {
    single<ItemProductDecoration> {
        ItemProductDecoration(
            androidContext()
        )
    }
}

val librariesModule = module {
    single { Gson() }
}
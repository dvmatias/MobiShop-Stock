package com.cmdv.mobishopstock

import com.cmdv.core.navigator.Navigator
import com.cmdv.data.repositories.ProductRepositoryImpl
import com.cmdv.domain.repositories.ProductRepository
import com.cmdv.feature.CreateProductActivityViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<Navigator> { NavigatorImpl() }

}

val repositoryModule = module {

    single<ProductRepository> { ProductRepositoryImpl() }

}

val viewModelModule = module {

    viewModel { CreateProductActivityViewModel(get()) }

}
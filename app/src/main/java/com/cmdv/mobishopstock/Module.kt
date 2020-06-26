package com.cmdv.mobishopstock

import com.cmdv.core.navigator.Navigator
import com.cmdv.data.repositories.ProductRepositoryImpl
import com.cmdv.domain.repositories.ProductRepository
import com.cmdv.feature.CreateProductActivityViewModel
import com.cmdv.feature.ui.MainActivityViewModel
import com.cmdv.feature.ui.adapters.ItemProductDecoration
import com.cmdv.feature.ui.adapters.RecyclerProductAdapter
import org.koin.android.ext.koin.androidContext
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
    viewModel { MainActivityViewModel(get()) }

}

val adapterModule = module {

    single<RecyclerProductAdapter> { RecyclerProductAdapter(androidContext()) }

}

val itemDecorationModule = module {

    single<ItemProductDecoration> { ItemProductDecoration(androidContext()) }

}
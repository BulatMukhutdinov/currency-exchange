package tat.mukhutdinov.currencyexchange.currencylist.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tat.mukhutdinov.currencyexchange.currencylist.interactor.SearchCurrenciesInteractor
import tat.mukhutdinov.currencyexchange.currencylist.ui.CurrencyListLifecycleAwareViewModel
import tat.mukhutdinov.currencyexchange.currencylist.ui.boundary.SearchCurrenciesUseCase

object CurrencyListInjectionModule {

    val module = module {
        viewModel {
            CurrencyListLifecycleAwareViewModel(get(), get())
        }

        factory<SearchCurrenciesUseCase> {
            SearchCurrenciesInteractor(get())
        }
    }
}
package tat.mukhutdinov.currencyexchange.exchangeratelist.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tat.mukhutdinov.currencyexchange.exchangeratelist.ui.ExchangeRateListFragmentArgs
import tat.mukhutdinov.currencyexchange.exchangeratelist.ui.ExchangeRateListLifecycleAwareViewModel

object ExchangeRateListInjectionModule {

    val module = module {

        viewModel { (args: ExchangeRateListFragmentArgs) ->
            ExchangeRateListLifecycleAwareViewModel(args, get())
        }
    }
}
package tat.mukhutdinov.currencyexchange.exchangerate.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.ExchangeRateBoundGateway
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary.ExchangeRateConverter
import tat.mukhutdinov.currencyexchange.exchangerate.interactor.BuildExchangeRatesInteractor
import tat.mukhutdinov.currencyexchange.exchangerate.interactor.UpdateLocalStorageInteractor
import tat.mukhutdinov.currencyexchange.exchangerate.interactor.boundary.ExchangeRateGateway
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRateConverterImpl
import tat.mukhutdinov.currencyexchange.exchangerate.ui.ExchangeRateLifecycleAwareViewModel
import tat.mukhutdinov.currencyexchange.exchangerate.ui.boundary.BuildExchangeRatesUseCase
import tat.mukhutdinov.currencyexchange.exchangerate.ui.boundary.UpdateLocalStorageUseCase
import tat.mukhutdinov.currencyexchange.infrastructure.db.DataBase

object ExchangeRateInjectionModule {

    val module = module {

        viewModel { (rate: ExchangeRate) ->
            ExchangeRateLifecycleAwareViewModel(rate, get(), get())
        }

        factory<ExchangeRateGateway> {
            ExchangeRateBoundGateway(get(), get(), get(), get())
        }

        factory<UpdateLocalStorageUseCase> {
            UpdateLocalStorageInteractor(get())
        }


        factory<BuildExchangeRatesUseCase> {
            BuildExchangeRatesInteractor(get())
        }

        factory<ExchangeRateConverter> {
            ExchangeRateConverterImpl()
        }

        single {
            get<DataBase>().exchangeRateDao()
        }
    }
}
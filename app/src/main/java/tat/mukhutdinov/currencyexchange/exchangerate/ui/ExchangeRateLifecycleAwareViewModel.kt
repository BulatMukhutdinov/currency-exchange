package tat.mukhutdinov.currencyexchange.exchangerate.ui

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.exchangerate.ui.boundary.BuildExchangeRatesUseCase
import tat.mukhutdinov.currencyexchange.exchangerate.ui.boundary.UpdateLocalStorageUseCase
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.DataStateLiveData
import tat.mukhutdinov.currencyexchange.infrastructure.util.ui.getExceptionHandler

class ExchangeRateLifecycleAwareViewModel(
    private val selected: ExchangeRate,
    private val updateLocalStorageUseCase: UpdateLocalStorageUseCase,
    private val buildExchangeRateUseCase: BuildExchangeRatesUseCase
) : ViewModel(), ExchangeRateViewModel {

    override val rates = DataStateLiveData<List<ExchangeRate>>()

    private val exceptionHandler = getExceptionHandler { rates.onError(it) }

    init {
        buildRates()
    }

    override fun update() {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            updateLocalStorageUseCase.execute()

            withContext(Dispatchers.Main) {
                buildRates()
            }
        }
    }

    @MainThread
    private fun buildRates() {
        rates.onStart()

        viewModelScope.launch(exceptionHandler) {
            val rateList = withContext(Dispatchers.IO) {
                buildExchangeRateUseCase.execute(selected)
            }
            rates.onNext(rateList)
        }
    }
}
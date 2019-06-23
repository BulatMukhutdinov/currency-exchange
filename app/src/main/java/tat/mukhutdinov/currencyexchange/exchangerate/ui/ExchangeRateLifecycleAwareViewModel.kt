package tat.mukhutdinov.currencyexchange.exchangerate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val updateJob = viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                updateLocalStorageUseCase.execute()
            }
        }

        viewModelScope.launch(exceptionHandler) {
            updateJob.join()
            buildRates()
        }
    }

    private fun buildRates() {
        rates.onStart()

        viewModelScope.launch {
            val rateList = withContext(Dispatchers.IO + exceptionHandler) {
                buildExchangeRateUseCase.execute(selected)
            }

            rates.onNext(rateList)
        }
    }
}
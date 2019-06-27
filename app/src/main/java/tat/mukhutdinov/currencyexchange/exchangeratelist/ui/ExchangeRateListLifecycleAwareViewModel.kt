package tat.mukhutdinov.currencyexchange.exchangeratelist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tat.mukhutdinov.currencyexchange.currencylist.ui.boundary.SearchCurrenciesUseCase
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.DataStateLiveData
import tat.mukhutdinov.currencyexchange.infrastructure.util.ui.getExceptionHandler

class ExchangeRateListLifecycleAwareViewModel(
    args: ExchangeRateListFragmentArgs,
    searchCurrenciesUseCase: SearchCurrenciesUseCase
) : ViewModel(), ExchangeRateListViewModel {

    override var currentRatePosition: Int = 0

    override val rates = DataStateLiveData<List<ExchangeRate>>()

    private val exceptionHandler = getExceptionHandler { rates.onError(it) }

    init {
        rates.onStart()

        viewModelScope.launch(exceptionHandler) {
            val currencyList = withContext(Dispatchers.IO) {
                searchCurrenciesUseCase.execute("")
            }

            currentRatePosition = currencyList.indexOf(args.currency)

            rates.onNext(currencyList)
        }
    }
}
package tat.mukhutdinov.currencyexchange.currencylist.ui

import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.*
import tat.mukhutdinov.currencyexchange.currencylist.ui.boundary.SearchCurrenciesUseCase
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.exchangerate.ui.boundary.UpdateLocalStorageUseCase
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.DataStateLiveData
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.SingleLiveData
import tat.mukhutdinov.currencyexchange.infrastructure.util.ui.Utils
import tat.mukhutdinov.currencyexchange.infrastructure.util.ui.getExceptionHandler

class CurrencyListLifecycleAwareViewModel(
    private val searchCurrenciesUseCase: SearchCurrenciesUseCase,
    private val updateLocalStorageUseCase: UpdateLocalStorageUseCase
) : ViewModel(), CurrencyListViewModel {

    override val currencies = DataStateLiveData<List<ExchangeRate>>()

    override val directions = SingleLiveData<NavDirections>()

    override val query = MutableLiveData<String>()

    override val isListEmpty = MutableLiveData<Boolean>()

    private var searchJob: Job? = null

    private val exceptionHandler = getExceptionHandler { currencies.onError(it) }

    init {
        update()
    }

    override fun update() {
        viewModelScope.launch(exceptionHandler) {
            try {
                withContext(Dispatchers.IO) {
                    updateLocalStorageUseCase.execute()
                }
            } finally {
                onSearchClicked()
            }
        }
    }

    @MainThread
    override fun onSearchClicked(view: View?) {
        view?.let { Utils.hideKeyboard(it) }

        currencies.onStart()

        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            searchJob?.cancelAndJoin()

            searchJob = async {
                val result = searchCurrenciesUseCase.execute(query.value.orEmpty())

                withContext(Dispatchers.Main) {
                    currencies.onNext(result)
                    isListEmpty.value = result.isEmpty()
                }
            }
        }
    }

    override fun onCurrencyClicked(currency: ExchangeRate) {
        directions.value = CurrencyListFragmentDirections.toExchangeRate(currency)
    }
}
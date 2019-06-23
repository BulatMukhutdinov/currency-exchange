package tat.mukhutdinov.currencyexchange.currencylist.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val updateJob = viewModelScope.launch(exceptionHandler) {
            // https://github.com/Kotlin/kotlinx.coroutines/issues/1035
            withContext(Dispatchers.IO) {
                updateLocalStorageUseCase.execute()
            }
        }

        viewModelScope.launch(exceptionHandler) {
            updateJob.join()
            onSearchClicked()
        }
    }

    override fun onSearchClicked(view: View?) {
        view?.let { Utils.hideKeyboard(it) }

        currencies.onStart()

        viewModelScope.launch {
            withContext(Dispatchers.IO + exceptionHandler) {
                searchJob?.cancelAndJoin()

                searchJob = launch {
                    val result = searchCurrenciesUseCase.execute(query.value.orEmpty())

                    withContext(Dispatchers.Main) {
                        currencies.onNext(result)
                        isListEmpty.value = result.isEmpty()
                    }
                }
            }
        }
    }

    override fun onCurrencyClicked(currency: ExchangeRate) {
        directions.value = CurrencyListFragmentDirections.toExchangeRate(currency)
    }
}
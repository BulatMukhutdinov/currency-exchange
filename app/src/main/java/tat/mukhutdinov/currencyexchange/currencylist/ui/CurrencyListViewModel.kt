package tat.mukhutdinov.currencyexchange.currencylist.ui

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.DataStateLiveData

interface CurrencyListViewModel {

    val currencies: DataStateLiveData<List<ExchangeRate>>

    val directions: LiveData<NavDirections>

    val query: MutableLiveData<String>

    val isListEmpty: LiveData<Boolean>

    fun update()

    fun onSearchClicked(view: View? = null)

    fun onCurrencyClicked(currency: ExchangeRate)
}
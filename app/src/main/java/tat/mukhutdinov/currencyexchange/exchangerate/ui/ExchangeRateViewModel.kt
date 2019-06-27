package tat.mukhutdinov.currencyexchange.exchangerate.ui

import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.DataStateLiveData

interface ExchangeRateViewModel {

    val rates: DataStateLiveData<List<ExchangeRate>>

    fun update()
}
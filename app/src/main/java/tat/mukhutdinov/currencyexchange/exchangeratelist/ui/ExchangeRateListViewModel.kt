package tat.mukhutdinov.currencyexchange.exchangeratelist.ui

import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.DataStateLiveData

interface ExchangeRateListViewModel {

    var currentRatePosition: Int

    val rates: DataStateLiveData<List<ExchangeRate>>
}
package tat.mukhutdinov.currencyexchange.currencylist.ui.boundary

import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate

interface SearchCurrenciesUseCase {

    suspend fun execute(query: String): List<ExchangeRate>
}
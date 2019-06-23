package tat.mukhutdinov.currencyexchange.currencylist.interactor

import tat.mukhutdinov.currencyexchange.currencylist.ui.boundary.SearchCurrenciesUseCase
import tat.mukhutdinov.currencyexchange.exchangerate.interactor.boundary.ExchangeRateGateway
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate

class SearchCurrenciesInteractor(private val gateway: ExchangeRateGateway) : SearchCurrenciesUseCase {

    override suspend fun execute(query: String): List<ExchangeRate> =
        gateway.search(query)
}
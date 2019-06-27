package tat.mukhutdinov.currencyexchange.exchangerate.ui.boundary

import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate

interface BuildExchangeRatesUseCase {

    suspend fun execute(selected: ExchangeRate): List<ExchangeRate>
}
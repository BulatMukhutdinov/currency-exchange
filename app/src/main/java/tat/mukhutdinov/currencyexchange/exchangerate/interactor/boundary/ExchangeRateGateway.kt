package tat.mukhutdinov.currencyexchange.exchangerate.interactor.boundary

import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate

interface ExchangeRateGateway {

    suspend fun update()

    suspend fun search(query: String): List<ExchangeRate>

    suspend fun getAll(): List<ExchangeRate>
}
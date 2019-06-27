package tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary

import tat.mukhutdinov.currencyexchange.exchangerate.gateway.ExchangeRatesResponse
import tat.mukhutdinov.currencyexchange.exchangerate.db.ExchangeRateToEurEntity
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate

interface ExchangeRateConverter {

    fun fromResponseToEntity(response: ExchangeRatesResponse): List<ExchangeRateToEurEntity>

    fun fromEntityToModel(entity: ExchangeRateToEurEntity): ExchangeRate
}
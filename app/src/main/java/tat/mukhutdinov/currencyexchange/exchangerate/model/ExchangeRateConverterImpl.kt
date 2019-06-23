package tat.mukhutdinov.currencyexchange.exchangerate.model

import tat.mukhutdinov.currencyexchange.exchangerate.db.ExchangeRateToEurEntity
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.ExchangeRatesResponse
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary.ExchangeRateConverter
import java.math.BigDecimal

class ExchangeRateConverterImpl : ExchangeRateConverter {

    override fun fromEntityToModel(entity: ExchangeRateToEurEntity): ExchangeRate =
        ExchangeRate(
            name = entity.name,
            rate = BigDecimal(entity.rate)
        )

    override fun fromResponseToEntity(response: ExchangeRatesResponse): List<ExchangeRateToEurEntity> {
        val entities = mutableListOf(ExchangeRateToEurEntity(response.base, "1"))

        response.rates.forEach { (name, rate) ->
            entities.add(ExchangeRateToEurEntity(name, rate.toString()))
        }

        return entities
    }
}
package tat.mukhutdinov.currencyexchange.exchangerate.interactor

import tat.mukhutdinov.currencyexchange.exchangerate.interactor.boundary.ExchangeRateGateway
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.exchangerate.ui.boundary.BuildExchangeRatesUseCase
import java.math.RoundingMode

class BuildExchangeRatesInteractor(private val gateway: ExchangeRateGateway) : BuildExchangeRatesUseCase {

    override suspend fun execute(selected: ExchangeRate): List<ExchangeRate> {
        val rates = gateway.getAll()

        val adjustedRates = mutableListOf<ExchangeRate>()

        rates.forEach {
            if (it.name != selected.name) {
                adjustedRates.add(
                    ExchangeRate(
                        name = "${selected.name}/${it.name}",
                        rate = it.rate.divide(selected.rate, 10, RoundingMode.HALF_UP)
                    )
                )
            }
        }

        return adjustedRates
    }
}
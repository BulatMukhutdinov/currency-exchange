package tat.mukhutdinov.currencyexchange.exchangerate.interactor

import tat.mukhutdinov.currencyexchange.exchangerate.interactor.boundary.ExchangeRateGateway
import tat.mukhutdinov.currencyexchange.exchangerate.ui.boundary.UpdateLocalStorageUseCase

class UpdateLocalStorageInteractor(private val exchangeRateGateway: ExchangeRateGateway) : UpdateLocalStorageUseCase {

    override suspend fun execute() =
        exchangeRateGateway.update()
}
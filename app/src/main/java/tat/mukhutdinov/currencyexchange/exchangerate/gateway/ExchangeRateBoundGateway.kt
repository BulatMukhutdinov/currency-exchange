package tat.mukhutdinov.currencyexchange.exchangerate.gateway

import tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary.ExchangeRateConverter
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary.ExchangeRateDao
import tat.mukhutdinov.currencyexchange.exchangerate.interactor.boundary.ExchangeRateGateway
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.infrastructure.db.DataBase
import tat.mukhutdinov.currencyexchange.infrastructure.gateway.boundary.ExchangeRateApi

class ExchangeRateBoundGateway(
    private val exchangeRateApi: ExchangeRateApi,
    private val dataBase: DataBase,
    private val exchangeRateDao: ExchangeRateDao,
    private val exchangeRateConverter: ExchangeRateConverter
) : ExchangeRateGateway {

    override suspend fun search(query: String): List<ExchangeRate> =
        exchangeRateDao.search(query)
            .map(exchangeRateConverter::fromEntityToModel)

    override suspend fun getAll(): List<ExchangeRate> =
        exchangeRateDao.getAll()
            .map(exchangeRateConverter::fromEntityToModel)

    override suspend fun update() {
        val response = exchangeRateApi.getRates()
        val rates = exchangeRateConverter.fromResponseToEntity(response)

        dataBase.runInTransaction {
            exchangeRateDao.clear()
            exchangeRateDao.insert(*rates.toTypedArray())
        }
    }
}
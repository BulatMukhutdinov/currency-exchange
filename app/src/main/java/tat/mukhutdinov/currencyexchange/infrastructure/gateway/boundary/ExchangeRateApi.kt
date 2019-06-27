package tat.mukhutdinov.currencyexchange.infrastructure.gateway.boundary

import retrofit2.http.GET
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.ExchangeRatesResponse

interface ExchangeRateApi {

    @GET("?base=EUR")
    suspend fun getRates(): ExchangeRatesResponse
}
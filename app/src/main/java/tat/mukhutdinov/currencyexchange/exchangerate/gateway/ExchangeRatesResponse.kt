package tat.mukhutdinov.currencyexchange.exchangerate.gateway

import androidx.annotation.Keep

@Keep
data class ExchangeRatesResponse(val base: String, val date: String, val rates: Map<String, Double>)

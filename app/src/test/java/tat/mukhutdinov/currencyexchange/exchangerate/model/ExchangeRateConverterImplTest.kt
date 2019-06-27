package tat.mukhutdinov.currencyexchange.exchangerate.model

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import tat.mukhutdinov.currencyexchange.exchangerate.db.ExchangeRateToEurEntity
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.ExchangeRatesResponse
import java.math.BigDecimal

class ExchangeRateConverterImplTest {

    private lateinit var converter: ExchangeRateConverterImpl

    @Before
    fun setup() {
        converter = ExchangeRateConverterImpl()
    }


    @Test
    fun convertToModel_fromEntity() {
        val expected = ExchangeRate("EUR", BigDecimal.ONE)

        val actual = converter.fromEntityToModel(ExchangeRateToEurEntity("EUR", "1"))

        assertEquals(expected, actual)
    }

    @Test
    fun convertToEntity_fromResponse() {
        val expected = listOf(ExchangeRateToEurEntity("EUR", "1"), ExchangeRateToEurEntity("RUB", "10.0"))

        val actual = converter.fromResponseToEntity(ExchangeRatesResponse("EUR", "today", mapOf("RUB" to 10.0)))

        assertEquals(expected, actual)
    }
}
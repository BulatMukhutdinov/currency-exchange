package tat.mukhutdinov.currencyexchange.exchangerate.interactor

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import tat.mukhutdinov.currencyexchange.exchangerate.interactor.boundary.ExchangeRateGateway
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class BuildExchangeRatesInteractorTest {

    @MockK
    private lateinit var gatewayMock: ExchangeRateGateway

    @InjectMockKs
    private lateinit var interactor: BuildExchangeRatesInteractor

    private val selectedRate = ExchangeRate("RUB", BigDecimal.TEN)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun buildExchangeRates_onSuccessfulGatewayResponse() = runBlockingTest {
        coEvery {
            gatewayMock.getAll()
        } returns getRawRatesList()

        val actual = interactor.execute(selectedRate)

        assertEquals(getExpectedRatesList(), actual)
    }

    @Test(expected = RuntimeException::class)
    fun throwException_onGatewayErrorResponse() = runBlockingTest {
        coEvery {
            gatewayMock.getAll()
        } throws RuntimeException("Oops")

        interactor.execute(selectedRate)
    }

    private fun getRawRatesList(): List<ExchangeRate> {
        val eurRate = ExchangeRate("EUR", BigDecimal.ONE)
        val usdRate = ExchangeRate("USD", BigDecimal("2"))

        return listOf(selectedRate, eurRate, usdRate)
    }

    private fun getExpectedRatesList(): List<ExchangeRate> {
        return listOf(
            ExchangeRate("RUB/EUR", BigDecimal("0.1000000000")),
            ExchangeRate("RUB/USD", BigDecimal("0.2000000000"))
        )
    }
}
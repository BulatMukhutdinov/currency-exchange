package tat.mukhutdinov.currencyexchange.exchangerate.gateway

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import tat.mukhutdinov.currencyexchange.exchangerate.db.ExchangeRateToEurEntity
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary.ExchangeRateConverter
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary.ExchangeRateDao
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.infrastructure.db.DataBase
import tat.mukhutdinov.currencyexchange.infrastructure.gateway.boundary.ExchangeRateApi
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class ExchangeRateBoundGatewayTest {

    @MockK
    private lateinit var exchangeRateApiMock: ExchangeRateApi

    @MockK
    private lateinit var dataBaseMock: DataBase

    @MockK
    private lateinit var exchangeRateConverterMock: ExchangeRateConverter

    @MockK
    private lateinit var exchangeRateDaoMock: ExchangeRateDao

    @InjectMockKs
    private lateinit var gateway: ExchangeRateBoundGateway

    private val rateEntity = ExchangeRateToEurEntity("EUR", "1")
    private val rateModel = ExchangeRate("EUR", BigDecimal.ONE)
    private val rateResponse = ExchangeRatesResponse("EUR", "today", mapOf("USD" to 1.2))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun returnModels_onSearch() = runBlockingTest {
        every {
            exchangeRateDaoMock.search(any())
        } returns listOf(rateEntity)

        every {
            exchangeRateConverterMock.fromEntityToModel(rateEntity)
        } returns rateModel

        val actual = gateway.search("eur")

        assertEquals(listOf(rateModel), actual)
    }

    @Test(expected = RuntimeException::class)
    fun throwExceptionOnSearch_onDaoErrorResponse() = runBlockingTest {
        every {
            exchangeRateDaoMock.search(any())
        } throws RuntimeException("Oops")

        gateway.search("eur")
    }

    @Test(expected = RuntimeException::class)
    fun throwExceptionOnSearch_onConverterErrorResponse() = runBlockingTest {
        every {
            exchangeRateDaoMock.search(any())
        } returns listOf(rateEntity)

        every {
            exchangeRateConverterMock.fromEntityToModel(rateEntity)
        } throws RuntimeException("Oops")

        gateway.search("eur")
    }

    @Test
    fun returnModels_onGetAll() = runBlockingTest {
        every {
            exchangeRateDaoMock.getAll()
        } returns listOf(rateEntity)

        every {
            exchangeRateConverterMock.fromEntityToModel(rateEntity)
        } returns rateModel

        val actual = gateway.getAll()

        assertEquals(listOf(rateModel), actual)
    }

    @Test(expected = RuntimeException::class)
    fun throwExceptionOnGetAll_onDaoErrorResponse() = runBlockingTest {
        every {
            exchangeRateDaoMock.getAll()
        } throws RuntimeException("Oops")

        gateway.getAll()
    }

    @Test(expected = RuntimeException::class)
    fun throwExceptionOnGetAll_onConverterErrorResponse() = runBlockingTest {
        every {
            exchangeRateDaoMock.getAll()
        } returns listOf(rateEntity)

        every {
            exchangeRateConverterMock.fromEntityToModel(rateEntity)
        } throws RuntimeException("Oops")

        gateway.getAll()
    }

    @Test(expected = RuntimeException::class)
    fun throwExceptionOnUpdate_onApiErrorResponse() = runBlockingTest {
        coEvery {
            exchangeRateApiMock.getRates()
        } throws RuntimeException("Oops")

        gateway.update()
    }

    @Test(expected = RuntimeException::class)
    fun throwExceptionOnUpdate_onConverterErrorResponse() = runBlockingTest {
        coEvery {
            exchangeRateApiMock.getRates()
        } returns rateResponse

        every {
            exchangeRateConverterMock.fromResponseToEntity(rateResponse)
        } throws RuntimeException("Oops")

        gateway.update()
    }

    @Test(expected = RuntimeException::class)
    fun throwExceptionOnUpdate_onDatabaseErrorResponse() = runBlockingTest {
        coEvery {
            exchangeRateApiMock.getRates()
        } returns rateResponse

        every {
            exchangeRateConverterMock.fromResponseToEntity(rateResponse)
        } returns listOf(rateEntity)

        every {
            dataBaseMock.runInTransaction(any())
        } throws RuntimeException("Oops")

        gateway.update()
    }
}
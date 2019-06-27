package tat.mukhutdinov.currencyexchange.gateway

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tat.mukhutdinov.currencyexchange.exchangerate.db.ExchangeRateToEurEntity
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.ExchangeRateBoundGateway
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.ExchangeRatesResponse
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary.ExchangeRateConverter
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary.ExchangeRateDao
import tat.mukhutdinov.currencyexchange.infrastructure.db.DataBase
import tat.mukhutdinov.currencyexchange.infrastructure.gateway.boundary.ExchangeRateApi

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ExchangeRateBoundGatewayTest {

    @MockK
    private lateinit var exchangeRateApiMock: ExchangeRateApi

    @MockK
    private lateinit var exchangeRateConverterMock: ExchangeRateConverter

    @MockK
    private lateinit var exchangeRateDaoMock: ExchangeRateDao

    private lateinit var gateway: ExchangeRateBoundGateway

    private lateinit var databaseSpy: DataBase

    private lateinit var database: DataBase

    private val rateEntity = ExchangeRateToEurEntity("EUR", "1")
    private val rateResponse = ExchangeRatesResponse("EUR", "today", mapOf("USD" to 1.2))

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, DataBase::class.java).build()
        databaseSpy = spyk(database)

        gateway = ExchangeRateBoundGateway(exchangeRateApiMock, databaseSpy, exchangeRateDaoMock, exchangeRateConverterMock)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun daoInsertRatesIsCalled_onUpdate() = runBlockingTest {
        coEvery {
            exchangeRateApiMock.getRates()
        } returns rateResponse

        every {
            exchangeRateConverterMock.fromResponseToEntity(rateResponse)
        } returns listOf(rateEntity)

        gateway.update()

        verify { databaseSpy.runInTransaction(any()) }
        verify { exchangeRateDaoMock.clear() }
        verify { exchangeRateDaoMock.insert(rateEntity) }
    }
}
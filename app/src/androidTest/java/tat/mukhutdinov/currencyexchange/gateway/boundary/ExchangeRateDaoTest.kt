package tat.mukhutdinov.currencyexchange.gateway.boundary

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tat.mukhutdinov.currencyexchange.exchangerate.db.ExchangeRateToEurEntity
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary.ExchangeRateDao
import tat.mukhutdinov.currencyexchange.infrastructure.db.DataBase

@RunWith(AndroidJUnit4::class)
class ExchangeRateDaoTest {

    private lateinit var exchangeRateDao: ExchangeRateDao

    private lateinit var database: DataBase

    private val rateEntity1 = ExchangeRateToEurEntity("EUR", "1")
    private val rateEntity2 = ExchangeRateToEurEntity("USD", "1.2")

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, DataBase::class.java).build()
        exchangeRateDao = database.exchangeRateDao()
    }

    @Test
    fun returnAllInsertedRates_onGetAll() {
        exchangeRateDao.insert(rateEntity1, rateEntity2)

        val actual = exchangeRateDao.getAll()

        assertEquals(listOf(rateEntity1, rateEntity2), actual)
    }

    @Test
    fun returnMatchedRates_onSearch() {
        exchangeRateDao.insert(rateEntity1, rateEntity2)

        val actual = exchangeRateDao.search("EUR")

        assertEquals(listOf(rateEntity1), actual)
    }

    @Test
    fun returnNoRates_onDbCleared() {
        exchangeRateDao.insert(rateEntity1, rateEntity2)
        exchangeRateDao.clear()

        val actual = exchangeRateDao.getAll()

        assertEquals(emptyList<ExchangeRateToEurEntity>(), actual)
    }

    @After
    fun tearDown() {
        database.close()
    }
}
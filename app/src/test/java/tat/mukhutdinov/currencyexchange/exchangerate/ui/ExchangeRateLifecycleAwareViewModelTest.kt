package tat.mukhutdinov.currencyexchange.exchangerate.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineExceptionHandler
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.exchangerate.ui.boundary.BuildExchangeRatesUseCase
import tat.mukhutdinov.currencyexchange.exchangerate.ui.boundary.UpdateLocalStorageUseCase
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.DataStateLiveData
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.State
import java.math.BigDecimal

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class ExchangeRateLifecycleAwareViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var buildExchangeRatesUseCaseMock: BuildExchangeRatesUseCase

    @MockK
    private lateinit var updateLocalStorageUseCaseMock: UpdateLocalStorageUseCase

    private lateinit var viewModel: ExchangeRateLifecycleAwareViewModel

    private val selectedRate = ExchangeRate("CUR", BigDecimal.ONE)

    private val exchangeRates = listOf(selectedRate)

    private val testDispatcher = TestCoroutineDispatcher()

    private val testCoroutineScope = TestCoroutineScope()

    private val testExceptionHandler = TestCoroutineExceptionHandler()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun ratesArePosted_onStart() {
        coEvery {
            buildExchangeRatesUseCaseMock.execute(selectedRate)
        } returns exchangeRates

        viewModel = ExchangeRateLifecycleAwareViewModel(selectedRate, updateLocalStorageUseCaseMock, buildExchangeRatesUseCaseMock)

        assertData(exchangeRates, viewModel.rates)
    }

    @Test
    fun updatedRatesArePosted_onUpdate() {
        val updatedRates = exchangeRates + exchangeRates
        coEvery {
            buildExchangeRatesUseCaseMock.execute(selectedRate)
        } returnsMany listOf(exchangeRates, updatedRates)

        viewModel = ExchangeRateLifecycleAwareViewModel(selectedRate, updateLocalStorageUseCaseMock, buildExchangeRatesUseCaseMock)

        viewModel.update()

        assertData(updatedRates, viewModel.rates)
    }

    private fun <T> assertData(expectedData: T, dataStateLiveData: DataStateLiveData<T>) = runBlocking {
        // https://github.com/Kotlin/kotlinx.coroutines/issues/1292
        delay(100)
        assertEquals(expectedData, dataStateLiveData.value?.data)
        assertEquals(State.Success, dataStateLiveData.value?.state)
    }
}
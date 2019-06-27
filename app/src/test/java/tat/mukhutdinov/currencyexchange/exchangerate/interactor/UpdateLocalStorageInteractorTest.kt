package tat.mukhutdinov.currencyexchange.exchangerate.interactor

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import tat.mukhutdinov.currencyexchange.exchangerate.interactor.boundary.ExchangeRateGateway

@ExperimentalCoroutinesApi
class UpdateLocalStorageInteractorTest {

    @MockK
    private lateinit var gatewayMock: ExchangeRateGateway

    @InjectMockKs
    private lateinit var interactor: UpdateLocalStorageInteractor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun callGatewayUpdate_onExecute() = runBlockingTest {
        interactor.execute()

        coVerify(exactly = 1) { gatewayMock.update() }
    }

    @Test(expected = RuntimeException::class)
    fun throwException_onGatewayErrorResponse() = runBlockingTest {
        coEvery {
            gatewayMock.update()
        } throws RuntimeException("Oops")

        interactor.execute()
    }
}
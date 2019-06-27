package tat.mukhutdinov.currencyexchange.ui

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest
import tat.mukhutdinov.currencyexchange.R
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.exchangerate.ui.ExchangeRateFragment
import tat.mukhutdinov.currencyexchange.exchangerate.ui.ExchangeRateLifecycleAwareViewModel
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.DataStateLiveData
import java.math.BigDecimal

@RunWith(AndroidJUnit4::class)
class ExchangeRateFragmentTest : KoinTest {

    lateinit var fragmentScenario: FragmentScenario<ExchangeRateFragment>

    private val viewModel = mockk<ExchangeRateLifecycleAwareViewModel>()

    private val rateModel = ExchangeRate("EUR", BigDecimal("72.856721523"))

    private val rates = DataStateLiveData<List<ExchangeRate>>()

    @Before
    fun setup() {
        setupViewModel()

        loadKoinModules(module)

        fragmentScenario = launchFragmentInContainer(Bundle().apply { putParcelable("extra_rate", rateModel) }, R.style.AppTheme)

        fragmentScenario.onFragment {
            rates.onNext(listOf(rateModel))
        }
    }

    @Test
    fun exchangeRateAreDisplayed_onStart() {
        Espresso.onView(ViewMatchers.withText("EUR"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText("72.8567"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private val module = module(override = true) {
        viewModel { (_: ExchangeRate) ->
            viewModel
        }
    }

    private fun setupViewModel() {
        every {
            viewModel.rates
        } returns rates
    }
}
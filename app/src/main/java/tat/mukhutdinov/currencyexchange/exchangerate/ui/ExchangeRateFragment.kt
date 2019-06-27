package tat.mukhutdinov.currencyexchange.exchangerate.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.exchange_rate.rates
import kotlinx.android.synthetic.main.exchange_rate.refresh
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tat.mukhutdinov.currencyexchange.R
import tat.mukhutdinov.currencyexchange.databinding.ExchangeRateBinding
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.exchangerate.ui.adapter.ExchangeRateAdapter
import tat.mukhutdinov.currencyexchange.exchangerate.ui.adapter.ExchangeRateDiffUtilCallback
import tat.mukhutdinov.currencyexchange.infrastructure.util.ui.observeViewState
import tat.mukhutdinov.currencyexchange.infrastructure.util.ui.toastError

class ExchangeRateFragment : Fragment() {

    private lateinit var binding: ExchangeRateBinding

    private val viewModel: ExchangeRateViewModel by viewModel<ExchangeRateLifecycleAwareViewModel> {
        parametersOf(
            arguments?.getParcelable(RATE_EXTRA)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.exchange_rate, container, false)

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        setupExchangeRateList()

        setupRefresh()
    }

    private fun setupRefresh() {
        refresh.setOnRefreshListener {
            viewModel.update()
        }
    }

    private fun setupExchangeRateList() {
        val adapter = ExchangeRateAdapter()

        rates.adapter = adapter
        rates.layoutManager = LinearLayoutManager(context)
        rates.setHasFixedSize(true)

        viewModel.rates.observeViewState(
            owner = viewLifecycleOwner,
            dataCallback = {
                val diffUtilCallback = ExchangeRateDiffUtilCallback(adapter.rates, it)
                val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

                adapter.rates.clear()
                adapter.rates.addAll(it)
                diffResult.dispatchUpdatesTo(adapter)
            },
            loadingCallback = { refresh.isRefreshing = it },
            errorCallback = { context?.toastError(it) }
        )
    }

    companion object {
        private const val RATE_EXTRA = "extra_rate"

        fun create(currency: ExchangeRate): ExchangeRateFragment {
            val args = Bundle().apply { putParcelable(RATE_EXTRA, currency) }

            return ExchangeRateFragment().apply { arguments = args }
        }
    }
}
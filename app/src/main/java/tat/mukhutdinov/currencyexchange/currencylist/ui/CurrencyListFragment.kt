package tat.mukhutdinov.currencyexchange.currencylist.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.currency_list.currencyList
import kotlinx.android.synthetic.main.currency_list.query
import kotlinx.android.synthetic.main.currency_list.refresh
import org.koin.android.viewmodel.ext.android.viewModel
import tat.mukhutdinov.currencyexchange.R
import tat.mukhutdinov.currencyexchange.currencylist.ui.adapter.CurrencyListAdapter
import tat.mukhutdinov.currencyexchange.databinding.CurrencyListBinding
import tat.mukhutdinov.currencyexchange.exchangerate.ui.adapter.ExchangeRateDiffUtilCallback
import tat.mukhutdinov.currencyexchange.infrastructure.util.ui.observeViewState
import tat.mukhutdinov.currencyexchange.infrastructure.util.ui.toastError

class CurrencyListFragment : Fragment() {

    private lateinit var binding: CurrencyListBinding

    private val viewModel: CurrencyListViewModel by viewModel<CurrencyListLifecycleAwareViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.currency_list, container, false)

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        setupSearch()

        setupCurrencyList()

        setupRefresh()

        viewModel.directions.observe(viewLifecycleOwner, Observer { view.findNavController().navigate(it) })
    }

    private fun setupRefresh() {
        refresh.setOnRefreshListener {
            viewModel.update()
        }
    }

    private fun setupCurrencyList() {
        val adapter = CurrencyListAdapter(viewModel)

        currencyList.adapter = adapter
        currencyList.layoutManager = LinearLayoutManager(context)
        currencyList.setHasFixedSize(true)

        viewModel.currencies.observeViewState(
            owner = viewLifecycleOwner,
            dataCallback = {
                val diffUtilCallback = ExchangeRateDiffUtilCallback(adapter.currencies, it)
                val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

                adapter.currencies.clear()
                adapter.currencies.addAll(it)
                diffResult.dispatchUpdatesTo(adapter)
            },
            loadingCallback = { refresh.isRefreshing = it },
            errorCallback = { context?.toastError(it) }
        )
    }

    private fun setupSearch() {
        query.setOnEditorActionListener { view, actionId: Int, event: KeyEvent? ->
            if (event?.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.onSearchClicked(view)
                true
            } else {
                false
            }
        }
    }
}
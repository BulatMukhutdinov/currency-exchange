package tat.mukhutdinov.currencyexchange.exchangeratelist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.exchange_rate_list.rates
import kotlinx.android.synthetic.main.exchange_rate_list.tabs
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tat.mukhutdinov.currencyexchange.R
import tat.mukhutdinov.currencyexchange.databinding.ExchangeRateListBinding
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate
import tat.mukhutdinov.currencyexchange.exchangerate.ui.ExchangeRateFragment
import tat.mukhutdinov.currencyexchange.exchangeratelist.adapter.ZoomOutPageTransformer
import tat.mukhutdinov.currencyexchange.infrastructure.util.ui.observeViewState
import tat.mukhutdinov.currencyexchange.infrastructure.util.ui.toast

class ExchangeRateListFragment : Fragment() {

    private lateinit var binding: ExchangeRateListBinding

    private val args: ExchangeRateListFragmentArgs by navArgs()

    private val viewModel: ExchangeRateListViewModel by viewModel<ExchangeRateListLifecycleAwareViewModel> { parametersOf(args) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.exchange_rate_list, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.rates.observeViewState(
            owner = viewLifecycleOwner,
            dataCallback = {
                val pagerAdapter = PagerAdapter(this, it)

                rates.adapter = pagerAdapter
                rates.setPageTransformer(ZoomOutPageTransformer())
                rates.currentItem = viewModel.currentRatePosition

                rates.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        viewModel.currentRatePosition = position
                    }
                })

                TabLayoutMediator(tabs, rates) { tab, position ->
                    tab.text = it[position].name
                }.attach()
            },
            errorCallback = {
                context?.toast(it.localizedMessage)
            }
        )
    }

    private inner class PagerAdapter(fragment: Fragment, private val currencies: List<ExchangeRate>) : FragmentStateAdapter(fragment) {

        override fun createFragment(position: Int): Fragment =
            ExchangeRateFragment.create(currencies[position])

        override fun getItemCount(): Int = currencies.size
    }
}
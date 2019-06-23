package tat.mukhutdinov.currencyexchange.currencylist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import tat.mukhutdinov.currencyexchange.R
import tat.mukhutdinov.currencyexchange.currencylist.ui.CurrencyListViewModel
import tat.mukhutdinov.currencyexchange.databinding.CurrencyListItemBinding
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate

class CurrencyViewHolder(
    private val binding: CurrencyListItemBinding,
    private val viewModel: CurrencyListViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(item: ExchangeRate) {
        binding.currency = item
        binding.viewModel = viewModel

        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup, viewModel: CurrencyListViewModel): CurrencyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: CurrencyListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.currency_list_item, parent, false)

            return CurrencyViewHolder(binding, viewModel)
        }
    }
}
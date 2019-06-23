package tat.mukhutdinov.currencyexchange.exchangerate.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import tat.mukhutdinov.currencyexchange.R
import tat.mukhutdinov.currencyexchange.databinding.ExchangeRateItemBinding
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate

class ExchangeRateViewHolder(private val binding: ExchangeRateItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(item: ExchangeRate) {
        binding.rate = item

        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): ExchangeRateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: ExchangeRateItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.exchange_rate_item, parent, false)

            return ExchangeRateViewHolder(binding)
        }
    }
}
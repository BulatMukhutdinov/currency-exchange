package tat.mukhutdinov.currencyexchange.exchangerate.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate

class ExchangeRateAdapter : RecyclerView.Adapter<ExchangeRateViewHolder>() {

    init {
        setHasStableIds(true)
    }

    val rates: MutableList<ExchangeRate> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateViewHolder =
        ExchangeRateViewHolder.create(parent)

    override fun getItemCount(): Int =
        rates.size

    override fun onBindViewHolder(holder: ExchangeRateViewHolder, position: Int) {
        holder.bindTo(rates[position])
    }

    override fun getItemId(position: Int): Long {
        return rates[position].name.hashCode().toLong()
    }
}
package tat.mukhutdinov.currencyexchange.currencylist.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tat.mukhutdinov.currencyexchange.currencylist.ui.CurrencyListViewModel
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate

class CurrencyListAdapter(
    private val currencyListViewModel: CurrencyListViewModel
) : RecyclerView.Adapter<CurrencyViewHolder>() {

    init {
        setHasStableIds(true)
    }

    val currencies: MutableList<ExchangeRate> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder =
        CurrencyViewHolder.create(parent, currencyListViewModel)

    override fun getItemCount(): Int =
        currencies.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bindTo(currencies[position])
    }

    override fun getItemId(position: Int): Long {
        return currencies[position].name.hashCode().toLong()
    }
}
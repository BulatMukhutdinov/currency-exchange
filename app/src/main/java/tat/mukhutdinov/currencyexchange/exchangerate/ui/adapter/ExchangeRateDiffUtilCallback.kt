package tat.mukhutdinov.currencyexchange.exchangerate.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import tat.mukhutdinov.currencyexchange.exchangerate.model.ExchangeRate

class ExchangeRateDiffUtilCallback(private val oldList: List<ExchangeRate>, private val newList: List<ExchangeRate>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].name == newList[newItemPosition].name

    override fun getOldListSize(): Int =
        oldList.size

    override fun getNewListSize(): Int =
        newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].rate == newList[newItemPosition].rate
}
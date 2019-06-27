package tat.mukhutdinov.currencyexchange.exchangerate.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Parcelize
@Keep
data class ExchangeRate(val name: String, val rate: BigDecimal) : Parcelable {

    @IgnoredOnParcel
    private val decimalFormat = NumberFormat.getInstance(Locale.US)

    @IgnoredOnParcel
    val formattedRate: String

    init {
        val decimal = rate.setScale(4, BigDecimal.ROUND_DOWN)
        decimalFormat.maximumFractionDigits = 4
        decimalFormat.minimumFractionDigits = 0
        decimalFormat.isGroupingUsed = false
        formattedRate = decimalFormat.format(decimal)
    }
}
package tat.mukhutdinov.currencyexchange.exchangerate.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import tat.mukhutdinov.currencyexchange.exchangerate.db.ExchangeRateToEurEntity.Companion.TABLE_NAME

@Fts4
@Entity(tableName = TABLE_NAME)
data class ExchangeRateToEurEntity(
    @ColumnInfo(name = COLUMN_NAME)
    var name: String,
    @ColumnInfo(name = COLUMN_RATE)
    var rate: String
) {

    companion object {
        const val TABLE_NAME = "exchange_rate"
        const val COLUMN_NAME = "name"
        const val COLUMN_RATE = "rate"
    }
}
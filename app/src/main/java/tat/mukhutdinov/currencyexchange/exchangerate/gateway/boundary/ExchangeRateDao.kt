package tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import tat.mukhutdinov.currencyexchange.exchangerate.db.ExchangeRateToEurEntity

@Dao
interface ExchangeRateDao {

    @Query("SELECT * FROM ${ExchangeRateToEurEntity.TABLE_NAME} WHERE ${ExchangeRateToEurEntity.COLUMN_NAME} LIKE '%' || :query || '%' ORDER BY ${ExchangeRateToEurEntity.COLUMN_NAME}")
    fun search(query: String): List<ExchangeRateToEurEntity>

    @Query("SELECT * FROM ${ExchangeRateToEurEntity.TABLE_NAME}")
    fun getAll(): List<ExchangeRateToEurEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg rates: ExchangeRateToEurEntity)

    @Query("DELETE FROM ${ExchangeRateToEurEntity.TABLE_NAME}")
    fun clear()
}
package tat.mukhutdinov.currencyexchange.infrastructure.db

import androidx.room.Database
import androidx.room.RoomDatabase
import tat.mukhutdinov.currencyexchange.exchangerate.db.ExchangeRateToEurEntity
import tat.mukhutdinov.currencyexchange.exchangerate.gateway.boundary.ExchangeRateDao

@Database(
    entities = [
        ExchangeRateToEurEntity::class
    ],
    version = 8)
abstract class DataBase : RoomDatabase() {

    abstract fun exchangeRateDao(): ExchangeRateDao
}
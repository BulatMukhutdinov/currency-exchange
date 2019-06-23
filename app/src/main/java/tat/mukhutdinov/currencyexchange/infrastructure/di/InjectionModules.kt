package tat.mukhutdinov.currencyexchange.infrastructure.di

import tat.mukhutdinov.currencyexchange.currencylist.di.CurrencyListInjectionModule
import tat.mukhutdinov.currencyexchange.exchangerate.di.ExchangeRateInjectionModule
import tat.mukhutdinov.currencyexchange.exchangeratelist.di.ExchangeRateListInjectionModule

object InjectionModules {

    val modules = listOf(
        InfrastructureInjectionModule.module,
        ExchangeRateInjectionModule.module,
        ExchangeRateListInjectionModule.module,
        CurrencyListInjectionModule.module
    )
}
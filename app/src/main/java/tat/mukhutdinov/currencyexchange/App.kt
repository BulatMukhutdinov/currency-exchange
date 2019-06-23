package tat.mukhutdinov.currencyexchange

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tat.mukhutdinov.currencyexchange.infrastructure.di.InjectionModules

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoin()

        setupUncaughtExceptions()
    }

    private fun setupUncaughtExceptions() {
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            // send logs to a server or something
        }
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@App)

            modules(InjectionModules.modules)
        }
    }

}
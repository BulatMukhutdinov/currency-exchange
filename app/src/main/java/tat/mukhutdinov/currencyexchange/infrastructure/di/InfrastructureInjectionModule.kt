package tat.mukhutdinov.currencyexchange.infrastructure.di

import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tat.mukhutdinov.currencyexchange.infrastructure.db.DataBase
import tat.mukhutdinov.currencyexchange.infrastructure.gateway.boundary.ExchangeRateApi

object InfrastructureInjectionModule {
    private const val DATABASE_NAME = "music_manager_db"
    private const val BASE_SERVER_URL = "https://api.exchangeratesapi.io/latest/"

    val module = module {

        single {
            Room.databaseBuilder(get(), DataBase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        single<ExchangeRateApi> {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val httpClientBuilder = OkHttpClient.Builder()
            httpClientBuilder.addInterceptor(httpLoggingInterceptor)

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_SERVER_URL)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ExchangeRateApi::class.java)
        }

    }
}
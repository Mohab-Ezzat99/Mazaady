package mrandroid.mazaady.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mrandroid.mazaady.data.remote.MazaadyApi
import mrandroid.mazaady.data.repository.MazaadyRepositoryImpl
import mrandroid.mazaady.domain.repository.MazaadyRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitWithToken(): MazaadyApi {
        return Retrofit.Builder()
            .baseUrl(MazaadyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MazaadyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(mazaadyApi: MazaadyApi): MazaadyRepository {
        return MazaadyRepositoryImpl(mazaadyApi)
    }

}
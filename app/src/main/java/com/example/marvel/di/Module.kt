package com.example.marvel.di

import android.content.Context
import androidx.room.Room
import com.example.marvel.model.remote.ServiceApi
import com.example.marvel.model.remote.local.MarvelDataBase
import com.example.marvel.util.Constant
import com.example.marvel.util.Constant.BASE_URL
import com.example.marvel.util.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
     @Singleton
     @Provides
    fun providerMarvelDatabase(
         @ApplicationContext context: Context
    )= Room.databaseBuilder(
       context,
        MarvelDataBase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providerMarvelDao(dataBase: MarvelDataBase) = dataBase.marvelDao()


    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                val currentTimestamp = System.currentTimeMillis()
                val newUrl = chain.request().url
                    .newBuilder()
                    .addQueryParameter(Constant.TS, currentTimestamp.toString())
                    .addQueryParameter(Constant.APIKEY, Constant.PUBLIC_KEY)
                    .addQueryParameter(
                        Constant.HASH,
                        provideToMd5Hash(currentTimestamp.toString() + Constant.PRIVATE_KEY + Constant.PUBLIC_KEY)
                    )
                    .build()

                val newRequest = chain.request()
                    .newBuilder()
                    .url(newUrl)
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun providerServiceApi(retrofit: Retrofit): ServiceApi {
        return retrofit.create(ServiceApi::class.java)
    }

    @Singleton
    @Provides
    fun provideToMd5Hash(encrypted: String): String {
        var pass = encrypted
        var encryptedString: String? = null
        val md5: MessageDigest
        try {
            md5 = MessageDigest.getInstance("MD5")
            md5.update(pass.toByteArray(), 0, pass.length)
            pass = BigInteger(1, md5.digest()).toString(16)
            while (pass.length < 32) {
                pass = "0$pass"
            }
            encryptedString = pass
        } catch (e1: NoSuchAlgorithmException) {
            e1.printStackTrace()
        }
        return encryptedString ?: ""
    }
}
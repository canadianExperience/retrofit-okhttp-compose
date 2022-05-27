package com.me.retrofit_okhttp_compose.data.di

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.me.retrofit_okhttp_compose.data.apimanager.TodoApi
import com.me.retrofit_okhttp_compose.utils.Constants.Companion.BASE_URL
import com.me.retrofit_okhttp_compose.utils.Constants.Companion.TAG
import com.me.retrofit_okhttp_compose.utils.hasInternetConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    @Singleton
    @Provides
    fun provideHttpClient(connectivityManager: ConnectivityManager) : OkHttpClient {

        val token: String? = "my_token"

        val networkInterceptor = Interceptor {chain ->
            //HTTP header interceptor
            //Network interceptor works only when network is on

            //Task: add "Bearer" Authorization header to every call (request). Note
            //that header should be added only if a token is present.
            val request = chain.request()
            if(request.header("No-Authentication") == null){
                //Check that user is authenticated
                if(!token.isNullOrEmpty()){
                    //Check the token is present
                    val finalToken = "Bearer $token"

                    request
                        .newBuilder()
                        .addHeader("Authorization", finalToken)
                        .build()
                }
            }

            val response = chain.proceed(request);
            response
        }

        val offLineInterceptor = Interceptor { chain ->
            val request = chain.request()
            if(!hasInternetConnection(connectivityManager)){
                // Do something if no internet connection
            }

            val response = chain.proceed(request);
            response
        }

        val loggingInterceptor = HttpLoggingInterceptor{ message ->
            Log.d(TAG, "log: http log: $message")
        }.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(networkInterceptor) //Network interceptor works only when network is on
            .addInterceptor(offLineInterceptor) //works only when network is off
            .addInterceptor(loggingInterceptor) //works only when network is on or off
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)


        return client.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): TodoApi{
        return retrofit.create(TodoApi::class.java)
    }

}
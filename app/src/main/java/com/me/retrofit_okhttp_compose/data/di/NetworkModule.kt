package com.me.retrofit_okhttp_compose.data.di

import com.me.retrofit_okhttp_compose.data.apimanager.TodoApi
import com.me.retrofit_okhttp_compose.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpClient() : OkHttpClient {
//        return OkHttpClient.Builder()
//            .readTimeout(15, TimeUnit.SECONDS)
//            .connectTimeout(15, TimeUnit.SECONDS)
//            .build()

       val token: String? = "my_token"

       val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor {chain ->
                //HTTP header interceptor

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
            })
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
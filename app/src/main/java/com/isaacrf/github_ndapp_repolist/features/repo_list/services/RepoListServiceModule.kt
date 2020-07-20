package com.isaacrf.github_ndapp_repolist.features.repo_list.services

import com.google.gson.GsonBuilder
import com.isaacrf.github_ndapp_repolist.shared.TLSSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Provides Repo List Service instantiations
 */
@Module
@InstallIn(ApplicationComponent::class)
object RepoListServiceModule {

    /**
     * Provides instantiation for http client interface
     */
    @Provides
    @Singleton
    fun provideRepoListService(): RepoListService {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val socketFactory = TLSSocketFactory()
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .sslSocketFactory(socketFactory, socketFactory.getTrustManager())
            .addInterceptor(logging)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(RepoListService::class.java)
    }
}
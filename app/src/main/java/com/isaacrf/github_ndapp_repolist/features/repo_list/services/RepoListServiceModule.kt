package com.isaacrf.github_ndapp_repolist.features.repo_list.services

import com.google.gson.GsonBuilder
import com.isaacrf.github_ndapp_repolist.TLSSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object TestModule {

    @Provides
    @Singleton
    fun provideRepoListService(): RepoListService {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val socketFactory = TLSSocketFactory()

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .sslSocketFactory(socketFactory, socketFactory.getTrustManager())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(RepoListService::class.java)
    }
}
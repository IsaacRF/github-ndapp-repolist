package com.isaacrf.github_ndapp_repolist.features.repo_list.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo
import com.isaacrf.github_ndapp_repolist.features.repo_list.services.RepoListService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Layer to abstract data access
 */
@Singleton
class RepoListRepository @Inject constructor(
    private val repoListService: RepoListService
){
    /**
     * GET repos for specified organization name
     */
    fun getRepos(organizationName: String): LiveData<List<Repo>> {
        val data = MutableLiveData<List<Repo>>()
        repoListService.getRepos(organizationName, 1).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                data.value = response.body()
            }
            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                //TODO: Report error
                Log.d("INJECTION TESTING", "getRepos() - Failure")
            }
        })
        return data
    }
}
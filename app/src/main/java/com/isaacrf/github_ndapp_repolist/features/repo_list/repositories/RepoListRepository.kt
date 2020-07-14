package com.isaacrf.github_ndapp_repolist.features.repo_list.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo
import com.isaacrf.github_ndapp_repolist.features.repo_list.services.RepoListService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Layer to abstract data access
 */
class RepoListRepository {
    //TODO: Needs injection
    private val repoListService: RepoListService = TODO()

    /**
     * GET repos for specified organization name
     */
    fun getRepos(organizationName: String): LiveData<List<Repo>> {
        val data = MutableLiveData<List<Repo>>()
        repoListService.getRepos(organizationName).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                data.value = response.body()
            }
            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                TODO()
            }
        })
        return data
    }
}
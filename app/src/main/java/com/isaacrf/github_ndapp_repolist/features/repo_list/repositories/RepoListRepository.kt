package com.isaacrf.github_ndapp_repolist.features.repo_list.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo
import com.isaacrf.github_ndapp_repolist.features.repo_list.services.RepoListService
import com.isaacrf.github_ndapp_repolist.shared.NetworkResource
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
    val repoListService: RepoListService
){
    /**
     * GET repos for specified organization name
     */
    fun getRepos(organizationName: String): LiveData<NetworkResource<List<Repo>>> {
        val data = MutableLiveData<NetworkResource<List<Repo>>>()
        data.value = NetworkResource.loading(null)
        //TODO: Expose call state
        repoListService.getRepos(organizationName, 1).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                data.value = NetworkResource.success(response.body())
            }
            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                Log.d("RepoListRepository", "getRepos() - Failure")
                data.value = NetworkResource.error(t.message!!, null)
            }
        })
        return data
    }
}
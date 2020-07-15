package com.isaacrf.github_ndapp_repolist.features.repo_list.services

import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Service to handle http calls to repo list backend
 */
interface RepoListService {
    /**
     * @GET Method. Retrieves all repos for a given organization
     * @param organizationName annotation on the organizationName parameter marks it as a
     * replacement for the {organizationName} placeholder in the @GET path
     *
     */
    @Headers("Authorization: token f896ed88fa40dec6546ee665d96cb45160f0b8ee")
    @GET("/orgs/{organizationName}/repos")
    fun getRepos(
        @Path("organizationName") organizationName: String,
        @Query("page") page: Int
    ): Call<List<Repo>>
}
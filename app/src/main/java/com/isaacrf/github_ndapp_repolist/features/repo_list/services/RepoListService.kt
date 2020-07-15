package com.isaacrf.github_ndapp_repolist.features.repo_list.services

import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Service to handle http calls to repo list backend
 */
interface RepoListService {
    /**
     * @GET declares an HTTP GET request
     * @Path("organizationName") annotation on the organizationName parameter marks it as a
     * replacement for the {organizationName} placeholder in the @GET path
     */
    @GET("/orgs/{organizationName}/repos")
    fun getRepos(@Path("organizationName") organizationName: String): Call<List<Repo>>
}
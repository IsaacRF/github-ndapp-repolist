package com.isaacrf.github_ndapp_repolist.features.repo_list.models

import com.google.gson.annotations.SerializedName

/**
 * A representation of a GitHub Repo basic data
 */
data class Repo(
    private val name: String,
    private val description: String,
    private val fork: Boolean,
    @SerializedName("html_url")
    private val htmlUrl: String,
    private val owner: Owner
) {}
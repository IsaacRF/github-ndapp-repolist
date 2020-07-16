package com.isaacrf.github_ndapp_repolist.features.repo_list.models

import com.google.gson.annotations.SerializedName

/**
 * A representation of a GitHub Repo basic data
 */
data class Repo(
    val name: String,
    val description: String,
    val fork: Boolean,
    @SerializedName("html_url")
    val htmlUrl: String,
    val owner: Owner
) {}
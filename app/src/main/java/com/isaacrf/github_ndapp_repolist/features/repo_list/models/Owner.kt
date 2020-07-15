package com.isaacrf.github_ndapp_repolist.features.repo_list.models

import com.google.gson.annotations.SerializedName

/**
 * A representation of a GitHub Repo owner basic data
 */
data class Owner(
    private val login: String,
    @SerializedName("html_url")
    private val htmlUrl: String,
    @SerializedName("avatar_url")
    private val avatarUrl: String
) {}
package com.isaacrf.github_ndapp_repolist.features.repo_list.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo

/**
 * View model that controls Repo List business logic and data representation
 */
class RepoListViewModel(
    private val state: SavedStateHandle
) : ViewModel() {
    val organizationName: String = state.get("organizationName") ?:
            throw IllegalArgumentException("Missing organization name")
    //TODO: Needs repository injection
    val repoList: LiveData<List<Repo>> = TODO()
}
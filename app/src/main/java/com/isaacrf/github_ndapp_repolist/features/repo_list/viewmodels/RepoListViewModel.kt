package com.isaacrf.github_ndapp_repolist.features.repo_list.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo
import com.isaacrf.github_ndapp_repolist.features.repo_list.repositories.RepoListRepository
import com.isaacrf.github_ndapp_repolist.shared.NetworkResource

/**
 * View model that controls Repo List business logic and data representation
 */
class RepoListViewModel @ViewModelInject constructor (
    private val repoListRepository: RepoListRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    //TODO: Need to recover organization name from saved state
    private val organizationName: String = state.get("organizationName") ?:
            "Xing" //throw IllegalArgumentException("Missing organization name")
    val repoList: LiveData<NetworkResource<List<Repo>>> = repoListRepository.getRepos(organizationName)
}
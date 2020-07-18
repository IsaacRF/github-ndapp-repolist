package com.isaacrf.github_ndapp_repolist.features.repo_list.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Owner
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo
import com.isaacrf.github_ndapp_repolist.features.repo_list.repositories.RepoListRepository
import com.isaacrf.github_ndapp_repolist.shared.NetworkResource
import com.isaacrf.github_ndapp_repolist.shared.Status
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


/**
 * Repo List ViewModel test
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RepoListViewModelTest {
    @Rule @JvmField
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var repoListRepository: RepoListRepository
    @Mock
    private lateinit var state: SavedStateHandle
    @Mock
    private lateinit var observer: Observer<NetworkResource<List<Repo>>>
    private lateinit var repoListViewModel: RepoListViewModel

    private var testOrganization = "Xing"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Test Repo List not Null and observer attached`() {
        `when`(repoListRepository.getRepos(testOrganization)).thenReturn(MutableLiveData<NetworkResource<List<Repo>>>())
        repoListViewModel = RepoListViewModel(repoListRepository, state)
        repoListViewModel.repoList.observeForever(observer)

        assertNotNull("repoList LiveData is null", repoListViewModel.repoList)
        assertTrue("repoList has no observer attached", repoListViewModel.repoList.hasObservers())
    }

    @Test
    fun `Test Fetch Data success`() {
        //Mock Data
        val mockRepos: List<Repo> = listOf(
            Repo("Test 1", "Test Repo 1", true, "https://www.test.com",
                Owner("testowner1", "https://www.test.com", "https://www.test.com")
            ),
            Repo("Test 2", "Test Repo 2", true, "https://www.test.com",
                Owner("testowner2", "https://www.test.com", "https://www.test.com")
            )
        )
        val mockData: MutableLiveData<NetworkResource<List<Repo>>> = MutableLiveData(NetworkResource(Status.SUCCESS, mockRepos, ""))

        `when`(repoListRepository.getRepos(testOrganization)).thenReturn(mockData)
        repoListViewModel = RepoListViewModel(repoListRepository, state)
        repoListViewModel.repoList.observeForever(observer)

        //verify(observer).onChanged(NetworkResource.loading(null));
        verify(observer).onChanged(mockData.value)
        assertNotNull("repoList LiveData is null", repoListViewModel.repoList)
        assertNotNull("repoList value is null", repoListViewModel.repoList.value)
        assertTrue("repoList value has incorrect size. Expected: 2 / Got: $repoListViewModel.repoList.value?.size",repoListViewModel.repoList.value?.data?.size == 2)
        assertTrue(repoListViewModel.repoList.value?.data?.get(0)?.name == "Test 1")
        assertTrue(repoListViewModel.repoList.value?.data?.get(1)?.name == "Test 2")
    }

    @Test
    fun `Test Fetch Data fail`() {
        //Empty response body
        val mockRepos: List<Repo>? = null
        val mockData: LiveData<NetworkResource<List<Repo>>> = MutableLiveData<NetworkResource<List<Repo>>>(
            NetworkResource(Status.ERROR, mockRepos, "Test failure")
        )

        `when`(repoListRepository.getRepos(testOrganization)).thenReturn(mockData)
        repoListViewModel = RepoListViewModel(repoListRepository, state)
        repoListViewModel.repoList.observeForever(observer)

        verify(observer).onChanged(NetworkResource.loading(null))
        verify(observer).onChanged(mockData.value)
        assertNull(repoListViewModel.repoList.value?.data)
    }
}
package com.isaacrf.github_ndapp_repolist.features.repo_list.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Owner
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo
import com.isaacrf.github_ndapp_repolist.features.repo_list.repositories.RepoListRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.lang.RuntimeException


/**
 * Example local unit test, which will execute on the development machine (host).
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
    private lateinit var observer: Observer<List<Repo>>
    private lateinit var repoListViewModel: RepoListViewModel

    private var testOrganization = "Xing"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Test Repo List not Null and observer attached`() {
        `when`(repoListRepository.getRepos(testOrganization)).thenReturn(MutableLiveData<List<Repo>>())
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

        `when`(repoListRepository.getRepos(testOrganization)).thenReturn(MutableLiveData<List<Repo>>(mockRepos))
        repoListViewModel = RepoListViewModel(repoListRepository, state)
        repoListViewModel.repoList.observeForever(observer)

        assertNotNull("repoList LiveData is null", repoListViewModel.repoList)
        assertNotNull("repoList value is null", repoListViewModel.repoList.value)
        assertTrue("repoList value has incorrect size. Expected: 2 / Got: $repoListViewModel.repoList.value?.size",repoListViewModel.repoList.value?.size == 2)
        assertTrue(repoListViewModel.repoList.value?.get(0)?.name == "Test 1")
        assertTrue(repoListViewModel.repoList.value?.get(1)?.name == "Test 2")
    }

    @Test
    fun `Test Fetch Data fail`() {
        //Empty response body
        val mockRepos: List<Repo>? = null
        val mockData: LiveData<List<Repo>> = MutableLiveData<List<Repo>>(mockRepos)

        `when`(repoListRepository.getRepos(testOrganization)).thenReturn(mockData)
        repoListViewModel = RepoListViewModel(repoListRepository, state)
        repoListViewModel.repoList.observeForever(observer)

        assertNull(repoListViewModel.repoList.value)
    }
}
package com.isaacrf.github_ndapp_repolist.features.repo_list.services

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import junit.framework.Assert.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import javax.annotation.Resource
import kotlin.coroutines.coroutineContext

/**
 * Repo List Repository test
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RepoListServiceTest {
    @Rule @JvmField
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var gson: Gson
    private lateinit var repoListService: RepoListService

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        gson = GsonBuilder()
            .setLenient()
            .create()
        repoListService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RepoListService::class.java)
    }

    @Test
    fun `getRepos() - Call Success`() {
        enqueueResponse("ReposXingSample.json")

        val response = repoListService.getRepos("Xing", 1).execute()

        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("/orgs/Xing/repos?page=1"))

        val repo = response.body()?.get(0)
        assertNotNull(repo)
        assertThat(repo?.name, `is`("gearman-ruby"))

        val owner = repo?.owner
        assertNotNull(owner)
        assertThat(owner?.login, `is`("xing"))
        assertThat(owner?.htmlUrl, `is`("https://github.com/xing"))

        val repo2 = response.body()?.get(1)
        assertNotNull(repo2)
        assertThat(repo2?.name, `is`("gearman-server"))
    }

    @Test
    fun `getRepos() - Not Found`() {
        enqueueResponse("ReposOrganizationNotFound.json", emptyMap(), HttpURLConnection.HTTP_NOT_FOUND)

        val response = repoListService.getRepos("NonExistingOrganization", 1).execute()

        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("/orgs/NonExistingOrganization/repos?page=1"))

        assertTrue("Response code is not 404 - Not Found", response.code() == HttpURLConnection.HTTP_NOT_FOUND)
        assertNull("Response Body should be null", response.body())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Helper method to enqueue mock responses
     */
    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap(), responseCode: Int = HttpURLConnection.HTTP_OK, networkStatus: SocketPolicy? = null) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("mock_responses/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()

        if (networkStatus != null) {
            mockResponse.socketPolicy = networkStatus
        }
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
                .setResponseCode(responseCode)
        )
    }
}
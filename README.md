# Github NdApp

**Author**: [IsaacRF239](https://isaacrf.com/about)

**Minimum SDK**: API 16

**Target SDK**: API 29

Android test app built in modern Architecture that consumes GitHub API to show sample data.

![github-ndapp-demo](https://user-images.githubusercontent.com/2803925/87967017-785a4400-cabe-11ea-9890-7fe077d2a221.gif)

## Main Features

### MVVM Architecture
This project uses [MVVM](https://developer.android.com/jetpack/docs/guide) (Model - View - Viewmodel) architecture, via new [Jetpack ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) feature.

![image](https://user-images.githubusercontent.com/2803925/87967886-d3d90180-cabf-11ea-86fc-47e19eb460e7.png)

### Development Patterns
This project implements "By feature + layout" structure, [Separation of Concerns](https://en.wikipedia.org/wiki/Separation_of_concerns) pattern and [SOLID](https://en.wikipedia.org/wiki/SOLID) principles.

#### By feature + layout structure
Project architecture combines the "By feature" structure, consisting on separating all files concerning to a specific feature (for example, an app screen / section) on its own package, plus the "By layout" classic structure, separating all files serving a similar purpose on its own sub-package.

By feature structure complies with the Separation of Concerns and encapsulation patterns, also making the app highly scalable, modular and way easier to manipulate, as deleting or adding features impact only app base layer and refactor is minimum to non-existent.

![ByFeatureFolderStructure](https://user-images.githubusercontent.com/2803925/87969071-befd6d80-cac1-11ea-8b29-e421c1e3cc5c.png)

Testing project replicates the same feature structure to ease test running separation

![TestFolderStructure](https://user-images.githubusercontent.com/2803925/87969224-0126af00-cac2-11ea-9c4b-5f26ec355ef3.png)

#### LiveData / Observable Pattern
Data is handled via [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) / Observable Pattern instead of RxJava, as it's better performant and includes a series of benefits as, for example, avoiding manual app lifecycle management.

***RepoListViewModel***
```Kotlin
val repoList: LiveData<NetworkResource<List<Repo>>> = repoListRepository.getRepos(organizationName)
```

***RepoListActivity***
```Kotlin
//Observe live data changes and update UI accordingly
repoListViewModel.repoList.observe(this) {
    when(it.status) {
        Status.LOADING -> {}
        Status.SUCCESS -> {}
        Status.ERROR -> {}
    }
}
```

#### Dependency Injection
Project implements Dependency Injection (SOLI**D**) to isolate modules, avoid inter-dependencies and make testing easier

Dependency Injection is handled via [Hilt](https://developer.android.com/training/dependency-injection/hilt-android), a library that uses Dagger under the hood easing its implementation via @ annotations, and is developed and recommended to use by Google.

***GithubNdApp (App main class)***
```Kotlin
@HiltAndroidApp
class GithubNdApp : Application() {}
```

***RepoListActivity***
```Kotlin
@AndroidEntryPoint
class RepoListActivity : AppCompatActivity(), RepoListItemViewAdapter.OnRepoListener {
    private val repoListViewModel: RepoListViewModel by viewModels()
}
```

***RepoListViewModel***
```Kotlin
class RepoListViewModel @ViewModelInject constructor (
    private val repoListRepository: RepoListRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {}
```

### API Calls
API Calls are handled via [retrofit](https://square.github.io/retrofit/), declaring calls via an interface, and automatically deserialized by [Gson](https://github.com/google/gson) into model objects.

***RepoListService***
```Kotlin
@Headers("Authorization: token ???????????????")
@GET("/orgs/{organizationName}/repos")
fun getRepos(
    @Path("organizationName") organizationName: String,
    @Query("page") page: Int
): Call<List<Repo>>
```

#### Forcing TLS1.2 on Android 4.1 (API 16) - 5.0 (API 21)
Android mininum SDK has been stablished in API 16 to avoid security conflicts against GitHub API.

GitHub API deprecated the SSLv3 and TLSv1.0 connection protocols for security reasons in favor of TLSv1.3 and TLSv1.2, as can be seen here: https://www.ssllabs.com/ssltest/analyze.html?d=api.github.com 

![GitHubAPITLS](https://user-images.githubusercontent.com/2803925/87972762-bc9e1200-cac7-11ea-97ef-79343834f69c.png)

![GitHubAPICiphers](https://user-images.githubusercontent.com/2803925/87972764-bd36a880-cac7-11ea-8b7f-cd91ea42630e.png)

As stated in Google Developer guidelines (https://developer.android.com/reference/javax/net/ssl/SSLSocket.html), TLSv1.2 protocol is only available in Android 4.1+ (not enabled by default, requiring some tweaks), and in Android 5.0+ (enabled by default). So any Android version below 4.1 uses TLSv1.0 or TLSv1.1, and fails to establish a secure SSL connection with the API, being unable to retrieve any data.

![AndroidTLSSupport](https://user-images.githubusercontent.com/2803925/87972766-bd36a880-cac7-11ea-9a15-66ceeb48607c.png)

The required tweaks to run the api calls on API 14 -> 21 are done via a custom [TLS Socked Factory](https://github.com/xing/test_android_isaacrf/blob/master/app/src/main/java/com/isaacrf/github_ndapp_repolist/shared/TLSSocketFactory.kt) that forces the enabling of TLSv1.2 when available, and also forces the ciphers required by the API.

### Image rendering and caching
Repo owner avatar image is rendered and cached using [EpicBitmapRenderer](https://github.com/IsaacRF/EpicBitmapRenderer), a Java library I also developed.

![EpicBitmapRenderer Icon](http://isaacrf.com/libs/epicbitmaprenderer/images/EpicBitmapRenderer-Icon.png)

### Testing
All business logic and services are tested using [Mockito](https://site.mockito.org/) and okhttp [Mockwebserver](https://github.com/square/okhttp/tree/master/mockwebserver).

Every test is isolated, and all API calls are mocked to avoid test results to depend on external sources, becoming unrealiable and possibly leading to unexpected results.

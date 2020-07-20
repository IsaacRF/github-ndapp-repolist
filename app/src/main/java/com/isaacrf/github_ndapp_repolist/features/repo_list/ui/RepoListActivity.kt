package com.isaacrf.github_ndapp_repolist.features.repo_list.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.isaacrf.github_ndapp_repolist.R
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo
import com.isaacrf.github_ndapp_repolist.features.repo_list.viewmodels.RepoListViewModel
import com.isaacrf.github_ndapp_repolist.shared.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.repo_list_content.*


@AndroidEntryPoint
class RepoListActivity : AppCompatActivity(), RepoListItemViewAdapter.OnRepoListener {

    /*ViewModel controls business logic and data representation. A saved state factory is created
    to provide state retain across activity life cycle
     */
    private val repoListViewModel: RepoListViewModel by viewModels()
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repo_list_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            openWebPage("https://github.com/orgs/xing")
        }

        //Recycler view config
        layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            rvRepoList.context,
            layoutManager.orientation
        )
        rvRepoList.addItemDecoration(dividerItemDecoration)
        rvRepoList.layoutManager = layoutManager

        //Observe live data changes and update UI accordingly
        repoListViewModel.repoList.observe(this) {
            when(it.status) {
                Status.LOADING -> {
                    pbRepoListLoading.visibility = View.GONE
                    pbRepoListLoading.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    rvRepoList.adapter = RepoListItemViewAdapter(it.data!!, this)
                    pbRepoListLoading.visibility = View.GONE
                }
                Status.ERROR -> {
                    txtError.text = it.message
                    pbRepoListLoading.visibility = View.VISIBLE
                    pbRepoListLoading.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRepoClick(repo: Repo) {
        openWebPage(repo.htmlUrl)
    }

    override fun onRepoLongClick(repo: Repo) {
        val options = arrayOf(
            "${repo.name} Repo Site",
            "${repo.owner.login} Owner Site"
        )

        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.dialog_repo_navigation_title))
            .setItems(options) { dialog, which ->
                when(which) {
                    0 -> openWebPage(repo.htmlUrl)
                    1 -> openWebPage(repo.owner.htmlUrl)
                }
            }
            .setIcon(R.mipmap.ic_launcher_round)
            .show()
    }

    /**
     * Opens a web page via intent
     */
    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}
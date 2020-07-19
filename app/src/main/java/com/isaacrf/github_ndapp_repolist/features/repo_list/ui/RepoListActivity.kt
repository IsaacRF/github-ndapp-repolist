package com.isaacrf.github_ndapp_repolist.features.repo_list.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.isaacrf.github_ndapp_repolist.R
import com.isaacrf.github_ndapp_repolist.features.repo_list.viewmodels.RepoListViewModel
import com.isaacrf.github_ndapp_repolist.shared.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.repo_list_content.*


@AndroidEntryPoint
class RepoListActivity : AppCompatActivity() {

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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
                    rvRepoList.adapter = RepoListItemViewAdapter(it.data!!)
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
}
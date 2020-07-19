package com.isaacrf.github_ndapp_repolist.features.repo_list.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isaacrf.epicbitmaprenderer.core.EpicBitmapRenderer
import com.isaacrf.github_ndapp_repolist.R
import com.isaacrf.github_ndapp_repolist.features.repo_list.models.Repo
import kotlinx.android.synthetic.main.repo_list_item_view.view.*

/**
 * UI Adapter for RepoList RecyclerView items
 */
class RepoListItemViewAdapter(private val repos: List<Repo>) :
    RecyclerView.Adapter<RepoListItemViewAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repo_list_item_view, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        //...
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = repos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = repos[position]
        holder.itemView.txtRepoName.text = repo.name
        holder.itemView.txtRepoDescription.text = repo.description
        holder.itemView.txtOwner.text = repo.owner.login

        /*
            This library is also developed by me, and automatically handles image decoding
            and caching (https://github.com/IsaacRF/EpicBitmapRenderer)
         */
        EpicBitmapRenderer.decodeBitmapFromUrl(repo.owner.avatarUrl, holder.itemView.imgRepoOwner.width, holder.itemView.imgRepoOwner.height,
            { holder.itemView.imgRepoOwner.setImageBitmap(it) },
            { Log.d("RepoListActivity", "Failed to decode image $repo.owner.avatarUrl") })
    }
}
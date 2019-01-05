package com.example.stefanr.archapp.views.favouritelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.models.SiteModel
import kotlinx.android.synthetic.main.recyclerview_listitem.view.*


interface FavouriteListener {
    fun onSiteClick(site: SiteModel)
}

// Adapts the Sites to the Recycler View
class FavouriteAdapter constructor(private var sites: List<SiteModel>, private val listener: FavouriteListener
) : RecyclerView.Adapter<FavouriteAdapter.MainHolder>() {

    // Creates new View Holder, inflates the RecyclerView List Item as a Layout and returns the Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder
    {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_listitem,
                parent,
                false
            )
        )
    }

    // returns the amount of sites in sitesList
    override fun getItemCount(): Int = sites.size

    // Binds the Sites to the View Holder
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        // if(sites[holder.adapterPosition].visited){
        val site = sites[holder.adapterPosition]
        holder.bind(site, listener)
        //}
    }

    // Helper Class to exclude the View Holder Logic
    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(site: SiteModel, listener: FavouriteListener) {

            if (site.favourite == true) {
                itemView.site_name.text = site.name
                itemView.site_list_lat.text = "%.6f".format(site.location.lat)
                itemView.site_list_lng.text = "%.6f".format(site.location.lng)
                itemView.site_checkbox.isChecked = site.visited
                itemView.site_list_favourite.isChecked = site.favourite

                Glide.with(itemView.context).load(site.images[0]).into(itemView.site_imageIcon)

                itemView.setOnClickListener { listener.onSiteClick(site) }
            }
        }
    }
}

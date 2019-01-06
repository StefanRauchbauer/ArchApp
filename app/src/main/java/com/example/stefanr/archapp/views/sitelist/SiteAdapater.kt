package com.example.stefanr.archapp.views.sitelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.models.SiteModel
import kotlinx.android.synthetic.main.recyclerview_listitem.view.*


interface SiteListener {
    fun onSiteClick(site: SiteModel)
}

// Adapts the Sites to the Recycler View
class SiteAdapter constructor(
    private var sites: List<SiteModel>,
    private val listener: SiteListener
) : RecyclerView.Adapter<SiteAdapter.MainHolder>(), Filterable {

    private var siteListFiltered: List<SiteModel>

    init {
        siteListFiltered = sites
    }

    // Creates new View Holder, inflates the RecyclerView List Item as a Layout and returns the Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_listitem,
                parent,
                false
            )
        )
    }

    // Gets the Size of the Sites List
    override fun getItemCount(): Int = siteListFiltered.size

    // Binds the Sites to the View Holder
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val filtersite = siteListFiltered[holder.adapterPosition]
        //  val site = sites[holder.adapterPosition]
        //  holder.bind(site, listener)
        holder.bind(filtersite, listener)
    }

    // Helper Class to exclude the View Holder Logic
    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(site: SiteModel, listener: SiteListener) {
            itemView.site_name.text = site.name
            itemView.site_list_lat.text = "%.6f".format(site.location.lat)
            itemView.site_list_lng.text = "%.6f".format(site.location.lng)
            itemView.site_checkbox.isChecked = site.visited
            itemView.site_list_favourite.isChecked = site.favourite

            Glide.with(itemView.context).load(site.images[0]).into(itemView.site_imageIcon)

            itemView.setOnClickListener { listener.onSiteClick(site) }

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    siteListFiltered = sites
                } else {
                    val filteredList = ArrayList<SiteModel>()
                    for (row in sites) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    siteListFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = siteListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                siteListFiltered = filterResults.values as List<SiteModel>
                notifyDataSetChanged()
            }
        }
    }
}

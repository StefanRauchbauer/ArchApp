package com.example.stefanr.archapp.views.favouritelist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.views.BaseView

class FavouriteListView : BaseView(), FavouriteListener {

    // Site List Presenter
    lateinit var presenter: FavouriteListPresenter

    //User Interface
    private var toolbarFavourite: Toolbar? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_list)

        Initialize()
    }

    private fun Initialize() {
        // Initialize recycle View
        //Initialize Toolbar
        recyclerView = findViewById<View>(R.id.recycler_view_favourite) as RecyclerView
        toolbarFavourite = findViewById<View>(R.id.favourite_toolbar) as Toolbar
        initToolbar(toolbarFavourite!!, true)


        // Initialize Presenter
        presenter = initPresenter(FavouriteListPresenter(this)) as FavouriteListPresenter

        // Sets the Layout for the Recycler View
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        presenter.loadSites()

    }

    // Shows the Sites in the Recycler View
    override fun showSites(sites: List<SiteModel>) {
        var favSites: MutableList<SiteModel>
        favSites = arrayListOf()
        //loop over sites List and add every element
        //to favSites Recycler View
        for (i in sites.indices) {
            if (sites[i].favourite) {
                favSites.add(sites[i])
            }
        }


        recyclerView!!.adapter = FavouriteAdapter(favSites, this)
        recyclerView!!.adapter?.notifyDataSetChanged()
    }


    // Creates Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favourite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Handles the Click Events on the Menu Items
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            // trigger settings or logout event
            R.id.item_settings -> presenter.doShowSettings()
            R.id.item_logout -> presenter.doLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    // If Site is clicked
    override fun onSiteClick(site: SiteModel)
    {
        presenter.EditSite(site)
    }

    // Updates the Recycler View
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadSites()
        super.onActivityResult(requestCode, resultCode, data)
    }
}
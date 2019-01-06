package com.example.stefanr.archapp.views.sitelist

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.views.BaseView
import com.google.android.material.navigation.NavigationView

class SiteListView : BaseView(), SiteListener, NavigationView.OnNavigationItemSelectedListener {


    // Adapter
    lateinit var mAdapter: SiteAdapter

    // Site List Presenter
    lateinit var presenter: SiteListPresenter

    //UI
    private var toolbarMain: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var searchView: SearchView? = null
    private var navigationView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_list_drawer)

        Initialize()
    }

    private fun Initialize() {
        // Initialize UI Elements
        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        toolbarMain = findViewById<View>(R.id.main_toolbar) as Toolbar
        drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById<View>(R.id.site_list_nav_view) as NavigationView

        // Adds the appropriate Toolbar to the Activity
        //setSupportActionBar(toolbarMain)
        initToolbar(toolbarMain!!, false)

        // Navigation Drawer
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbarMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        navigationView!!.setNavigationItemSelectedListener(this)


        // Initialize Presenter
        presenter = initPresenter(SiteListPresenter(this)) as SiteListPresenter

        // Sets the Appropriate Layout for the Recycler View
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        presenter.loadSites()
    }

    // Shows the Sites in the Recycler View
    override fun showSites(sites: List<SiteModel>) {
        mAdapter = SiteAdapter(sites, this)
        recyclerView!!.adapter = mAdapter //SiteAdapter(sites, this)
        recyclerView!!.adapter?.notifyDataSetChanged()
    }


    // Handle navigation view item clicks here.
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navdraw_map -> presenter.doShowSitesMap()
            R.id.navdraw_add_site -> presenter.doAddSite()
            R.id.navdraw_favourite -> presenter.doShowFavourites()
            R.id.navdraw_navigation -> presenter.doShowNavigator()
            R.id.navdraw_settings -> presenter.doShowSettings()
            R.id.navdraw_logout -> presenter.doLogout()
        }

        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }


    // Creates Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu!!.findItem(R.id.item_search).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Integer.MAX_VALUE

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                mAdapter.filter.filter(query)

                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                mAdapter.filter.filter(query)
                return false
            }
        })
        return true

        // return super.onCreateOptionsMenu(menu)
    }

    // If Site is clicked
    override fun onSiteClick(site: SiteModel) {
        presenter.doEditSite(site)
    }

    // Updates the Recycler View
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadSites()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
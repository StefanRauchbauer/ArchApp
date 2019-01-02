package com.example.stefanr.archapp.views.createaccount

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.views.BaseView
import org.jetbrains.anko.toast

class CreateAccountView : BaseView() {

    // Site List Presenter
    lateinit var presenter: CreateAccountPresenter

    //UI elements
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressBar? = null
    private var toolbarCreateAccount: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        InitializeUiElements()
        Initialize()
    }

    private fun Initialize() {

        super.initToolbar(toolbarCreateAccount!!, false)

        // Initialize Presenter
        presenter = initPresenter(CreateAccountPresenter(this)) as CreateAccountPresenter

        // Hide Progressbar
        hideProgress()

        // Create Account Button Click Listener
        btnCreateAccount!!.setOnClickListener {
            if (etEmail!!.text.isEmpty() ||
                etPassword!!.text.isEmpty() ||
                etFirstName!!.text.isEmpty() ||
                etLastName!!.text.isEmpty()
            ) {
                toast("Enter all required Fields")
            } else {
                presenter.doCreateNewAccount(
                    etEmail!!.text.toString(),
                    etPassword!!.text.toString(),
                    etFirstName!!.text.toString(),
                    etLastName!!.text.toString()
                )
            }

        }
    }

    override fun showProgress() {
        mProgressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProgressBar!!.visibility = View.GONE
    }

    // Creates the Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_account, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Triggers the Menu Items, if the User clicks on them
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun InitializeUiElements() {
        etFirstName = findViewById<View>(R.id.et_first_name) as EditText
        etLastName = findViewById<View>(R.id.et_last_name) as EditText
        etEmail = findViewById<View>(R.id.et_emailRegister) as EditText
        etPassword = findViewById<View>(R.id.et_passwordRegister) as EditText
        btnCreateAccount = findViewById<View>(R.id.btn_register) as Button
        mProgressBar = findViewById(R.id.create_progressBar)
        toolbarCreateAccount = findViewById<View>(R.id.create_account_toolbar) as Toolbar

    }
}
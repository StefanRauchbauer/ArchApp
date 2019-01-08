package com.example.stefanr.archapp.views.forgotpassword

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar


import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.views.BaseView

class ForgotPasswordView : BaseView() {

    // Site List Presenter
    lateinit var presenter: ForgotPasswordPresenter

    //User Interface
    private var etEmail: EditText? = null
    private var btnSubmit: Button? = null
    private var toolbarForgotPassword: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        InitializeUiElements()
        Initialize()
    }

    private fun Initialize() {

        super.initToolbar(toolbarForgotPassword!!, false)

        // Initialize Presenter
        presenter = initPresenter(ForgotPasswordPresenter(this)) as ForgotPasswordPresenter


        btnSubmit!!.setOnClickListener { presenter.doSendPasswordResetEmail(etEmail!!.text.toString()) }
    }


    // Creates the Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_forgot_password, menu)
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
        etEmail = findViewById<View>(R.id.et_email) as EditText
        btnSubmit = findViewById<View>(R.id.btn_submit) as Button
        toolbarForgotPassword = findViewById<View>(R.id.forgot_password_toolbar) as Toolbar
    }

}
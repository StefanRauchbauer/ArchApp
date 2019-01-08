package com.example.stefanr.archapp.views.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.views.BaseView


class LoginView : BaseView() {

    // Site List Presenter
    lateinit var presenter: LoginPresenter

    //UI elements
    private var txtViewForgotPassword: TextView? = null
    private var txtEmail: EditText? = null
    private var txtPassword: EditText? = null
    private var btnLogin: Button? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressBar? = null

    //Firebase references
    //  private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        InitialzieUiElements()
        Initialize()
    }

    private fun Initialize() {

        // Initialize Presenter
        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

        //Hide Progressbar
        hideProgressBar()

        // Triggers whan btnLogin is  clicked on login method gets called
        btnLogin!!.setOnClickListener { presenter.login(txtEmail!!.text.toString(), txtPassword!!.text.toString()) }

        // Trigger if forgot Password textView is clicked
        txtViewForgotPassword!!.setOnClickListener { presenter.forgotPassword() }

        // Trigger event if btnCreatAccount is clicked function doCreateAccount gets called
        btnCreateAccount!!.setOnClickListener { presenter.doCreateAccount() }


    }


    override fun showProgressBar() {
        mProgressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar!!.visibility = View.GONE
    }


    private fun InitialzieUiElements() {
        txtViewForgotPassword = findViewById<View>(R.id.tv_forgot_password) as TextView
        txtEmail = findViewById<View>(R.id.et_email) as EditText
        txtPassword = findViewById<View>(R.id.et_password) as EditText
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        btnCreateAccount = findViewById<View>(R.id.btn_register_account) as Button
        mProgressBar = findViewById<View>(R.id.login_progressbar) as ProgressBar

    }
}
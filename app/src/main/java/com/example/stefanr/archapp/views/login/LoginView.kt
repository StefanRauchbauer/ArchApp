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
    private var tvForgotPassword: TextView? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
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
        hideProgress()

        // Trigger the Forgot Password Button Event
        tvForgotPassword!!.setOnClickListener { presenter.doForgotPassword() }

        // Trigger the Create Account Button Event
        btnCreateAccount!!.setOnClickListener { presenter.doCreateAccount() }

        // Trigger the Login User Button Event
        btnLogin!!.setOnClickListener { presenter.doLogin(etEmail!!.text.toString(), etPassword!!.text.toString()) }
    }


    override fun showProgress() {
        mProgressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mProgressBar!!.visibility = View.GONE
    }

    // Initialize the UI Elements
    private fun InitialzieUiElements() {
        tvForgotPassword = findViewById<View>(R.id.tv_forgot_password) as TextView
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        btnCreateAccount = findViewById<View>(R.id.btn_register_account) as Button
        mProgressBar = findViewById<View>(R.id.login_progressbar) as ProgressBar
        //  mAuth = FirebaseAuth.getInstance()
    }
}
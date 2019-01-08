package com.example.stefanr.archapp.views.forgotpassword

import android.content.ContentValues.TAG
import android.text.TextUtils
import android.util.Log
import com.example.stefanr.archapp.views.BasePresenter
import com.example.stefanr.archapp.views.BaseView
import com.example.stefanr.archapp.views.VIEW

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.toast

class ForgotPasswordPresenter(view: BaseView) : BasePresenter(view) {

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    // Initializes the Components
    init {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
    }

    fun doSendPasswordResetEmail(email: String) {
        if (!TextUtils.isEmpty(email)) {
            mAuth!!
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val message = "Email sent successfully."
                        Log.d(TAG, message)
                        view?.toast(message)
                        view?.navigateTo(VIEW.LOGIN)
                    } else {
                        Log.w(TAG, task.exception!!.message)
                        view?.toast("No Account with this E-Mail")
                    }
                }
        } else {
            view?.toast("Enter your Email-Adress")
        }
    }

    // Closes the current Activity and goes back to the previous an
    fun doCancel() {
        view?.finish()
    }
}
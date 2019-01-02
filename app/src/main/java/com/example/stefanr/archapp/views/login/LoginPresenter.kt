package com.example.stefanr.archapp.views.login

import android.content.ContentValues.TAG
import android.text.TextUtils
import android.util.Log
import com.example.stefanr.archapp.models.firebase.SiteServiceFirebase
import com.example.stefanr.archapp.views.BasePresenter
import com.example.stefanr.archapp.views.BaseView
import com.example.stefanr.archapp.views.VIEW
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast

class LoginPresenter(view: BaseView) : BasePresenter(view) {

    //Firebase references
    private var mAuth: FirebaseAuth? = null
    private var firebaseService: SiteServiceFirebase? = null

    // Initializes the Components
    init {
        mAuth = FirebaseAuth.getInstance()
        if (app.sites is SiteServiceFirebase) {
            firebaseService = app.sites as SiteServiceFirebase
        }
    }


    // Handles the Login implementation
    fun doLogin(email: String, password: String) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            // Shows Progressbar
            view?.showProgress()
            Log.d(TAG, "Logging in user.")

            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(view!!) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        if (firebaseService != null) {
                            firebaseService!!.fetchSites {
                                view?.navigateTo(VIEW.SITELIST)
                            }
                        } else {
                            view?.navigateTo(VIEW.SITELIST)
                        }
                    } else {
                        view?.toast("Sign Up Failed: ${task.exception?.message}")
                    }
                    // Hide Progressbar
                    view?.hideProgress()
                }
        } else {
            view?.toast("Enter all Details")
        }
    }

    fun doCreateAccount() {
        view?.navigateTo(VIEW.CREATESITE)
    }

    fun doForgotPassword() {
        view?.navigateTo(VIEW.FORGOTPASSWORD)
    }
}

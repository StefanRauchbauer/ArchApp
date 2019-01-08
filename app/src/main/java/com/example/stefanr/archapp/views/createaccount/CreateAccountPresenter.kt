package com.example.stefanr.archapp.views.createaccount


import android.content.ContentValues.TAG
import android.util.Log
import com.example.stefanr.archapp.views.BasePresenter
import com.example.stefanr.archapp.views.BaseView
import com.example.stefanr.archapp.views.VIEW
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.toast

class CreateAccountPresenter(view: BaseView) : BasePresenter(view) {


    //Firebase references
    //declaring variables for authentification, database connection, storage
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    // Initializes the Components
    init {
        //Initializing firebase referenzes
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
    }


    //funtion to create a new account with email,password,first and last name
    fun doCreateNewAccount(email: String, password: String, firstName: String, lastName: String) {

        // Shows Progressbar
        //connect to the firebase Instance and giving it the email +password
        view?.showProgressBar()
        mAuth!!
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(view!!) { task ->
                //task is a process we want to observe
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                    // Gets User ID
                    val userId = mAuth!!.currentUser!!.uid

                    //Verify Email
                    verifyEmail()

                    //update user profile information
                    val currentUserDb = mDatabaseReference!!.child(userId)
                    currentUserDb.child("firstName").setValue(firstName)
                    currentUserDb.child("lastName").setValue(lastName)

                    // Navigate to the Login Activity
                    view?.navigateTo(VIEW.LOGIN)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    // Hides Progressbar
                    view?.hideProgressBar()
                    view?.toast("Failed to create an Account.")
                }
            }
    }

    //sending an verification email to the user trying to create an account
    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(view!!) { task ->
                if (task.isSuccessful) {
                    view?.toast("Verification email sent to " + mUser.email)
                } else {
                    view?.toast("Failed to send verification email")
                }
            }
    }

    // Closes the current Activity and goes back to the previous an
    fun doCancel() {
        view?.finish()
    }
}
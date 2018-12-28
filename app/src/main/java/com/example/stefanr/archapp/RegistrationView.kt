package com.example.stefanr.archapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in_.*
import kotlinx.android.synthetic.main.activity_registration_view.*

class RegistrationView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_view)


        btn_register.setOnClickListener{
            register()
        }


    }

    private fun register(){

        val email = input_email_register.text.toString()
        val password = input_password_register.text.toString()
        startActivity( Intent(this,LogIn_Activity::class.java))

        val firebase= FirebaseAuth.getInstance()
        if(firebase != null) {
            Log.e("nain", "Not Null"+ firebase.toString())
            firebase.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.e("nain", "Not Successfull" + "Email " + email + "Passwort " + password)
                    return@addOnCompleteListener
                } else {
                    Log.e("nain", "Successfull")

                }
            }
        }

    }


}

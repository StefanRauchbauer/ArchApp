package com.example.stefanr.archapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in_.*

class LogIn_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_)

        btn_login.setOnClickListener{
            login()
        }

        txt_register.setOnClickListener{
            register()
        }
    }

    private fun register()
    {
        startActivity(Intent(this,RegistrationView::class.java))
    }

    private fun login()
    {
        val firebase= FirebaseAuth.getInstance()

        var email = input_email.text.toString()
        var password = input_password.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty())
        {
            firebase.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful)
                {
                    Log.e("nain", "Successfull Login")
                    startActivity(Intent(this, MenueView::class.java))
                }
            }
        }
    }
}

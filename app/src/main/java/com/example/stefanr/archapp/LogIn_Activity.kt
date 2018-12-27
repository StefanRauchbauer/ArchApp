package com.example.stefanr.archapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_log_in_.*

class LogIn_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_)

        txt_register.setOnClickListener{
            register()
        }
    }

    private fun register()
    {
        startActivity(Intent(this,RegistrationView::class.java))
    }
}

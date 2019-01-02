package com.example.stefanr.archapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.views.login.LoginView

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(3000)

                    val intent = Intent(baseContext, LoginView::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        background.start()
    }
}
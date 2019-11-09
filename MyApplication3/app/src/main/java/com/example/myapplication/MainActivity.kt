package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp


class MainActivity : AppCompatActivity() {
    //private var mAuth: FirebaseAuth? = null
    private lateinit var signUpBtn: Button
    private lateinit var loginBtn: Button
    private lateinit var dashBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        //call the method at the bottom.
        initializeViews()

        //When click the signup Buton...
        signUpBtn.setOnClickListener {
            Toast.makeText(applicationContext, "You Just Decided to Sign Up", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@MainActivity, RegistrationActivity::class.java))
        }

        loginBtn.setOnClickListener {
            Toast.makeText(applicationContext, "You Just Decided to Sign Up", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        //just so I can get to the dashboard without having to login
        dashBtn.setOnClickListener {
            Toast.makeText(applicationContext, "You used Dash Button to get to the dah board", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
        }
    }

    private fun initializeViews() {
        signUpBtn = findViewById(R.id.SignUp_Butn)
        loginBtn = findViewById(R.id.Login_Button)
        dashBtn = findViewById(R.id.dash_Button)
    }
}

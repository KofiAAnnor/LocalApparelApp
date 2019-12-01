package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private lateinit var dashBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //firebase needs this
        FirebaseApp.initializeApp(this)

        //method created at the bttom
        initializeViews()

        dashBtn.setOnClickListener {
           // Toast.makeText(applicationContext, "You used Dash Button to get to the dash board", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@MainActivity, Dashboard::class.java))
        }

        registerBtn.setOnClickListener {
           // Toast.makeText(applicationContext, "You Just Decided to Sign Up", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@MainActivity, RegistrationActivity::class.java))
        }

        loginBtn.setOnClickListener {
           // Toast.makeText(applicationContext, "You Just Decided to Sign Up", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }

    private fun initializeViews() {
        dashBtn = findViewById(R.id.dashButton_id)
        registerBtn = findViewById(R.id.register_button_id)
        loginBtn = findViewById(R.id.login_button_id)
    }

    //This makes it so that when the user presses the back button, it will just go home.
//    override fun onBackPressed() {
//        return
        //if I were to uncomment return, it would just make it so that the back button does nothing.
        //The user would have to use the home button
       // moveTaskToBack(true)
//    }

}

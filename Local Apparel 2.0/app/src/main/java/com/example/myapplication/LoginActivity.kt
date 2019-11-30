package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerTxt: TextView
    private lateinit var mPrefs: SharedPreferences
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        initializeUI()
        loginBtn.setOnClickListener { loginUserAccount() }
        registerTxt.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
    }

    private fun loginUserAccount() {

        val email: String = userEmail.text.toString()
        val password: String = userPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_LONG)
                        .show()
                    //instantiate my shared prefernces object
                    mPrefs = getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
                    val editor = mPrefs.edit()
                    editor.putString("EMAIL", email)
                    val dashBoardIntent = Intent(this@LoginActivity, Dashboard::class.java)
                    dashBoardIntent.putExtra("email",userEmail.text.toString())
                    startActivity(Intent(this@LoginActivity, Dashboard::class.java))
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Login failed! Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun initializeUI() {
        userEmail = findViewById(R.id.email_view)
        userPassword = findViewById(R.id.password_view)
        loginBtn = findViewById(R.id.login_fromLoginScreen_id)
        registerTxt = findViewById(R.id.goto_register_screen)
    }

}

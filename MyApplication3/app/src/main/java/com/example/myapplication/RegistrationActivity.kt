package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class RegistrationActivity: AppCompatActivity() {

    private lateinit var fullNameTV: EditText
    private lateinit var emailTv: EditText
    private lateinit var passwordTV: EditText
    private lateinit var confirmPasswordTV: EditText
    private lateinit var zipcodeTV: EditText
    private lateinit var registerButtnView: Button
    private lateinit var fakeregisterButtnView: Button

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)
        initializeUI()

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        registerButtnView.setOnClickListener { registerNewUser() }
        fakeregisterButtnView.setOnClickListener { fakeRegisterNewUser() }
    }

    private fun registerNewUser() {

        val full_name: String = fullNameTV.text.toString()
        val email: String = emailTv.text.toString()
        val password: String = passwordTV.text.toString()
        val confirmPassword: String = confirmPasswordTV.text.toString()
        val zipcode: String = zipcodeTV.text.toString()

        if (TextUtils.isEmpty(full_name)) {
            Toast.makeText(applicationContext, "Please enter your full name...", Toast.LENGTH_LONG).show()
            return
        }else if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(applicationContext, "Confirm Pass doesn't match OG Pass!", Toast.LENGTH_LONG).show()
            return
        }else if (TextUtils.isEmpty(zipcode)) {
            Toast.makeText(applicationContext, "We need a zipcode to get a general Idea of where you are", Toast.LENGTH_LONG).show()
            return
        }


        val x = mAuth!!.createUserWithEmailAndPassword(email, password)

        x.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, getString(R.string.register_success_string), Toast.LENGTH_LONG).show()
                startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
            } else {
                Toast.makeText(applicationContext, getString(R.string.register_failed_string), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fakeRegisterNewUser() {
        Toast.makeText(applicationContext, "You Just Decided to Sign Up", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
    }

    private fun initializeUI() {
        fullNameTV = findViewById(R.id.fullName_view)
        emailTv = findViewById(R.id.email_view)
        passwordTV = findViewById(R.id.password_view)
        confirmPasswordTV = findViewById(R.id.confirmPassword_view)
        zipcodeTV = findViewById(R.id.zipcode_View)
        registerButtnView = findViewById(R.id.registerButtonView)
        fakeregisterButtnView = findViewById(R.id.fakeRegisterButtonView2)

    }
}
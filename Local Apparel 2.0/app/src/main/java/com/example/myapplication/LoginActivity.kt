package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.Objects.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val MYTAG = "myLoginAct"
class LoginActivity : AppCompatActivity() {

    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerTxt: TextView
    private lateinit var mPrefs: SharedPreferences
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(MYTAG,"WELCOME TO LOGIN") //It doesn't go past 1
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        initializeUI()
        loginBtn.setOnClickListener { loginUserAccount() }
        registerTxt.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
    }

    var globalUserID: String = ""
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
                    Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_LONG).show()
                    //instantiate my shared prefernces object
                    mPrefs = getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
                    Log.i(MYTAG,"YOU ARE LOOKING AT A SNAPSHOT 0")


                    val editor = mPrefs.edit()
                    editor.putString("EMAIL", email)
                    editor.apply()

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


    private fun findUserInformationInDatabase(email: String) {
        val ref = FirebaseDatabase.getInstance().reference
        val query = ref.child("users")

        Log.i(MYTAG,"YOU ARE LOOKING AT A SNAPSHOT 0")
        query.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(fbUserList: DataSnapshot) {
                if(fbUserList.exists()){
                    Log.i(MYTAG,"YOU ARE LOOKING AT A SNAPSHOT 0")
                    for(person in fbUserList.children){
                        Log.i(MYTAG,"YOU ARE LOOKING AT A SNAPSHOT 1") //It doesn't go past 1
                        val thisUser = person.getValue(User::class.java)
                        Log.i(MYTAG,"YOU ARE LOOKING AT A SNAPSHOT 2")
                        if(thisUser!!.userEmail == email){
                            Log.i(MYTAG,"WE FOUND THE USER")
                            globalUserID = thisUser.userID!!
                            return
                        }
                    }
                }
            }
        })
    }

    private fun initializeUI() {
        userEmail = findViewById(R.id.email_view)
        userPassword = findViewById(R.id.password_view)
        loginBtn = findViewById(R.id.login_fromLoginScreen_id)
        registerTxt = findViewById(R.id.goto_register_screen)
    }

}

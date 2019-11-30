package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.Objects.Items
import com.example.myapplication.Objects.MyItemsUpForSaleListAdapter
import com.example.myapplication.Objects.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_items_up_for_sale.*


class LoginActivity : AppCompatActivity() {

    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var mPrefs: SharedPreferences
    private var mAuth: FirebaseAuth? = null
    private var globalUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        initializeUI()
        loginBtn.setOnClickListener { loginUserAccount() }
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
                    Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_LONG).show()
                    //instantiate my shared prefernces object
                    mPrefs = getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
                    //findUserInformationInDatabase(email)
                    val editor = mPrefs.edit()
                    editor.putString("EMAIL", email)
                    //editor.putString("USERNAME",globalUser!!.userName)
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
        val ref = FirebaseDatabase.getInstance().getReference("users")

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(h in p0.children){
                        val thisUser = h.getValue(User::class.java)
                        if(thisUser!!.userEmail == email){
                            globalUser = thisUser
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
    }

}

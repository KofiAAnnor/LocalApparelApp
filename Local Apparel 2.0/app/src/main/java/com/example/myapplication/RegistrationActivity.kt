package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.Objects.Items
import com.example.myapplication.Objects.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var fullNameTV: EditText
    private lateinit var emailTv: EditText
    private lateinit var passwordTV: EditText
    private lateinit var confirmPasswordTV: EditText
    private lateinit var zipcodeTV: EditText
    private lateinit var registerButtnView: Button
    private lateinit var fakeregisterButtnView: Button
    private lateinit var mPrefs: SharedPreferences

    private var mAuth: FirebaseAuth? = null
    //val sharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        Log.i("myRegistration Act", "Welcome to Registration")
        initializeUI()

        //instantiate my shared prefernces object
        mPrefs = getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)

        mAuth = FirebaseAuth.getInstance()
        registerButtnView.setOnClickListener { registerNewUser() }
        fakeregisterButtnView.setOnClickListener { fakeRegisterNewUser() }
        //select_photo_button_id.setOnClickListener { selectPhoto() }
    }




    private fun selectPhoto() {
        Log.i("myRegistration Activity", "Try to show photo selector")

        //create the intent for the photo selector
        val photoSelectIntent = Intent(Intent.ACTION_PICK)
        photoSelectIntent.type = "image/*"
        startActivityForResult(photoSelectIntent, 0)
        //This means that we need to call onActivityResult
    }


    var mySelectedPhotoUri: Uri? = null


    //This is photo stuff
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.i("myRegistration Act", "Photo Was Selected")

            mySelectedPhotoUri = data.data
            val theBitMap = MediaStore.Images.Media.getBitmap(contentResolver, mySelectedPhotoUri)
            val theBitMapPicture = BitmapDrawable(theBitMap)
            select_photo_button_id.text = ""
            select_photo_button_id.setBackgroundDrawable(theBitMapPicture)
        }
    }


    private fun registerNewUser() {
        val fullName: String = fullNameTV.text.toString()
        val email: String = emailTv.text.toString()
        val password: String = passwordTV.text.toString()
        val confirmPassword: String = confirmPasswordTV.text.toString()
        val zipcode: String = zipcodeTV.text.toString()

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(applicationContext, "Please enter your full name...", Toast.LENGTH_LONG).show()
            return
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        } else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(applicationContext, "Confirm Pass doesn't match OG Pass!", Toast.LENGTH_LONG).show()
            return
        } else if (TextUtils.isEmpty(zipcode)) {
            Toast.makeText(applicationContext, "We need a Zipcode", Toast.LENGTH_LONG).show()
            return
        } else {
//            val editor = mPrefs.edit()
//            editor.putString("MY_NAME", fullName)
//            editor.putString("EMAIL", email)
//            editor.putString("ZIPCODE", zipcode)
//            editor.apply()
            Log.i("myRegistration", "We added all the prefs")
        }


        val x = mAuth!!.createUserWithEmailAndPassword(email, password)

        x.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, getString(R.string.register_success_string), Toast.LENGTH_LONG).show()
                val name = mPrefs.getString("NAME", "")
                saveUserToFireBaseDataBase()
                Log.i("myReg", "The task was a success")
                startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
            } else {
                Toast.makeText(applicationContext, getString(R.string.register_failed_string), Toast.LENGTH_LONG).show()
            }
        }

    }




    private fun uploadImageToFireBaseStorage() {
        if (mySelectedPhotoUri == null) {
            return
        }
        Log.i("myReg", "Lets Upload that image")
        val filename = UUID.randomUUID().toString()
        val myFireBaseRef = FirebaseStorage.getInstance().getReference("/images/$filename")

        val x = myFireBaseRef.putFile(mySelectedPhotoUri!!)
        x.addOnSuccessListener {
            Log.i("myRegistration Act", "Succesfully uploaded image: ${it.metadata?.path}")
        }
        x.addOnFailureListener{
            Log.i("myRegistration Act", "Failed to upload the image")
        }
        myFireBaseRef.downloadUrl.addOnSuccessListener {
            Log.i("myRegistration Act", "File Location $it")
            saveUserToFireBaseDataBase()
        }.addOnFailureListener{
            Log.i("myRegistration Act", "I FAILED to download file location $it")
        }
    }




    private fun saveUserToFireBaseDataBase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val myRef = FirebaseDatabase.getInstance().getReference("/users/$uid")

        //got this one way
        //val name = mPrefs.getString("NAME", "")
        val eMail = emailTv.text.toString()
        val name = fullNameTV.text.toString()


        //got this another way just cuz
        val myUser = User(uid, name, eMail!!)
        myUser.takePassWord(passwordTV.text.toString())
        myUser.takeZipCode(zipcodeTV.text.toString())
        val myItem = Items("Jacket","Small","20")
        myUser.addItems(myItem)

        val x = myRef.setValue(myUser)
        x.addOnSuccessListener {
            Log.i("myRegistration Act", "We saved the user")
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

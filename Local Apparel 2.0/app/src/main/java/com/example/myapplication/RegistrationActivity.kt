package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.Objects.Items
import com.example.myapplication.Objects.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registration.*
import java.net.URL
import java.util.*

private const val MYTAG = "myRegistrationAct"

class RegistrationActivity : AppCompatActivity() {

    private lateinit var fullNameTV: EditText
    private lateinit var emailTv: EditText
    private lateinit var passwordTV: EditText
    private lateinit var confirmPasswordTV: EditText
    private lateinit var zipcodeTV: EditText
    private lateinit var registerButtnView: Button
    private lateinit var fakeregisterButtnView: TextView

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        Log.i(MYTAG, "Welcome to Registration")
        initializeUI()

        mAuth = FirebaseAuth.getInstance()

        registerButtnView.setOnClickListener { registerNewUser() }
        fakeregisterButtnView.setOnClickListener { fakeRegisterNewUser() }
        select_photo_button_id.setOnClickListener { selectPhoto() }
    }

    private fun selectPhoto() {
        Toast.makeText(this, "Choose a photo", Toast.LENGTH_SHORT).show()
        Log.i(MYTAG, "User Is Choosing a Photo")

        //create the intent for the photo selector, this allows us to go into the
        //gallery and select whatever photo we want
        val photoSelectIntent = Intent(Intent.ACTION_PICK)
        photoSelectIntent.type = "image/*"
        startActivityForResult(photoSelectIntent, 0)
        //This means that we need to call onActivityResult
    }

    //we put this out here cuz we're gonna need it for later.
    var mySelectedPhotoUri: Uri? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.i(MYTAG, "Photo Was Selected")

            //get the info saved.
            mySelectedPhotoUri = data.data
            val theBitMap = MediaStore.Images.Media.getBitmap(contentResolver, mySelectedPhotoUri)
            val theBitMapPicture = BitmapDrawable(theBitMap)

            //Turn the button into the picture the user selected
            select_photo_button_id.text = ""
            select_photo_button_id.setBackgroundDrawable(theBitMapPicture)
        }
    }


    private fun registerNewUser() {
        Log.i(MYTAG, "Now We Register The User...")
        //When the user clicks register
        val fullName: String = fullNameTV.text.toString()
        val email: String = emailTv.text.toString()
        val password: String = passwordTV.text.toString()
        val confirmPassword: String = confirmPasswordTV.text.toString()
        val zipCode: String = zipcodeTV.text.toString()

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(applicationContext, "Please enter your full name...", Toast.LENGTH_LONG).show()
            return
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        } else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        } else if (password != confirmPassword) {
            Toast.makeText(applicationContext, "Confirm Pass doesn't match OG Pass!", Toast.LENGTH_LONG).show()
            return
        } else if (TextUtils.isEmpty(zipCode)) {
            Toast.makeText(applicationContext, "We need a zipcode to get a general Idea of where you are", Toast.LENGTH_LONG).show()
            return
        }else if(mySelectedPhotoUri ==null){
            Toast.makeText(applicationContext,"You need a profile Picture =)", Toast.LENGTH_LONG).show()
            return
        }

        //If all the slots are filled out. We authenticate

        val x = mAuth!!.createUserWithEmailAndPassword(email, password)

        x.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(MYTAG, "Authentication was a success")
                Toast.makeText(applicationContext, getString(R.string.register_success_string), Toast.LENGTH_LONG).show()
                uploadImageToFireBaseStorage() //CALL THE METHOD OT UPLOAD IMAGES!
                val loginIntent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(loginIntent)

            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.register_failed_string),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun uploadImageToFireBaseStorage() {
        //if no photo is selected
        if (mySelectedPhotoUri == null) {
            return
        }
        Log.i(MYTAG, "Lets Upload that image")

        val filename = UUID.randomUUID().toString()

        val myFireBaseRef = FirebaseStorage.getInstance().reference.child("/images/$filename")
                                    //TODO this use to say .getreference().child
        val x = myFireBaseRef.putFile(mySelectedPhotoUri!!)


        val urlTask = x.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            myFireBaseRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.i(MYTAG, "THIS IS THE DOWNLOAD_URI -> "+downloadUri.toString())
                saveUserToFireBaseDataBase(downloadUri.toString())
            } else {
            }
        }.addOnFailureListener{
            Log.i(MYTAG, "THE DOWNLOAD URT FAILED! =( ")
        }

        Log.i(MYTAG, "THIS IS THE SELECTED_PHOTO_URI -> "+mySelectedPhotoUri.toString())

//        x.addOnSuccessListener {
//            Log.i(MYTAG, "Successfully uploaded image. The Path is -> : ${it.metadata?.path}")
//        }
//        x.addOnFailureListener{
//            Log.i(MYTAG, "Failed to upload the image")
//        }
    }

    private fun saveUserToFireBaseDataBase(myProfileImageUrl: String) {
        Log.i(MYTAG, "Let's Save The user to Firebase")
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$uid")


        val name = fullName_view.text.toString()
        val eMail = email_view.text.toString()


        val myUser = User(uid, name, eMail, myProfileImageUrl)
        myUser.setPassword(passwordTV.text.toString())
        myUser.setZipCode(zipcodeTV.text.toString())

        //add an item to the users items just to be sure. it works
        val firstItem = Items("MyShirt","Nike","2 million dollars")
        myUser.addItems(firstItem)

        //add the user to the Databse
        val x = myUserRef.setValue(myUser)

        x.addOnSuccessListener {
            Log.i(MYTAG, "We saved the user")
        }
    }

    private fun fakeRegisterNewUser() {
        startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
    }

    private fun initializeUI() {
        Log.i(MYTAG, "We Initialize UI")
        fullNameTV = findViewById(R.id.fullName_view)
        emailTv = findViewById(R.id.email_view)
        passwordTV = findViewById(R.id.password_view)
        confirmPasswordTV = findViewById(R.id.confirmPassword_view)
        zipcodeTV = findViewById(R.id.zipcode_View)
        registerButtnView = findViewById(R.id.registerButtonView)
        fakeregisterButtnView = findViewById(R.id.fakeRegisterButtonView2)

    }
}

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
import android.widget.Toast
import com.example.myapplication.Objects.Items
import com.example.myapplication.Objects.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

private const val MYTAG = "myRegistrationAct"

class RegistrationActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_registration)
        Log.i(MYTAG, "Welcome to Registration")
        initializeUI()



        mAuth = FirebaseAuth.getInstance()

        registerButtnView.setOnClickListener { registerNewUser() }
        fakeregisterButtnView.setOnClickListener { fakeRegisterNewUser() }
        select_photo_button_id.setOnClickListener { selectPhoto() }
    }

    private fun selectPhoto() {
        Log.i(MYTAG, "Try to show photo selector")

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
            Toast.makeText(this,"We are in On Act Res",Toast.LENGTH_LONG)

            mySelectedPhotoUri = data.data
            val theBitMap = MediaStore.Images.Media.getBitmap(contentResolver, mySelectedPhotoUri)
            val theBitMapPicture = BitmapDrawable(theBitMap)

            //Turn the button into the picture the user selected
            select_photo_button_id.text = ""
            select_photo_button_id.setBackgroundDrawable(theBitMapPicture)
        }
    }


    private fun registerNewUser() {
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
        }

        //If all the slots are filled out.

        val x = mAuth!!.createUserWithEmailAndPassword(email, password)

        x.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, getString(R.string.register_success_string), Toast.LENGTH_LONG).show()
                uploadImageToFireBaseStorage() //CALL THE METHOD OT UPLOAD IMAGES!
                Log.i(MYTAG, "The task was a success")
                startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
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
        val myFireBaseRef = FirebaseStorage.getInstance().getReference("/images/$filename")

        val x = myFireBaseRef.putFile(mySelectedPhotoUri!!)
        x.addOnSuccessListener {
            Log.i(MYTAG, "Successfully uploaded image: ${it.metadata?.path}")
        }
        x.addOnFailureListener{
            Log.i(MYTAG, "Failed to upload the image")
        }
        saveUserToFireBaseDataBase()
        //once the image is uploasded Add the user.

        //todo If you uncomment this Block, Make sure to comment out the previous call to saveUserToFireBaseDataBase()
//        myFireBaseRef.downloadUrl.addOnSuccessListener {
//            Log.i(MYTAG, "File Location $it")
//            //saveUserToFireBaseDataBase(it.toString())
//            saveUserToFireBaseDataBase()
//        }.addOnFailureListener{
//            Log.i(MYTAG, "I FAILED to download file location $it")
//        }
    }


    //I'm saving the user without doing the URI download.
    private fun saveUserToFireBaseDataBase() {
        val email: String = emailTv.text.toString()
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val myRef = FirebaseDatabase.getInstance().getReference("/users/$uid")



        val name = fullName_view.text.toString()
        val eMail = email_view.text.toString()


        val myUser = User(uid, name, eMail)
        myUser.setPassword(passwordTV.text.toString())
        myUser.setZipCode(zipcodeTV.text.toString())
        val firstItem = Items("MyShirt","Nike","2 million dollars")
        myUser.addItems(firstItem)
        val x = myRef.setValue(myUser)
        x.addOnSuccessListener {
            Log.i("myRegistration Act", "We saved the user")
        }
    }


    private fun saveUserToFireBaseDataBase(myProfileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val myUserItemsListRef = FirebaseDatabase.getInstance().getReference("/users/$uid")

        //got this one way
        val name = fullName_view.text.toString()
        val eMail = email_view.text.toString()

        if (uid == "" || name == "" || eMail == "" || myProfileImageUrl == "") {
            return
        }

        //got this another way just cuz
        val myUser = User(uid!!, name!!, eMail!!, myProfileImageUrl)
        myUser.setPassword(passwordTV.text.toString())
        myUser.setZipCode(zipcodeTV.text.toString())
        val firstItem = Items("MyShirt","Nike","2 million dollars")
        myUser.addItems(firstItem)

        val x = myUserRef.setValue(myUser)
        x.addOnSuccessListener {
            Log.i(MYTAG, "We saved the user")
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

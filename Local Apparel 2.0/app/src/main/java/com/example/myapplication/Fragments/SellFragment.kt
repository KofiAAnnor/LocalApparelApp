package com.example.myapplication.Fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.Objects.Items

import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_sell.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SellFragment : Fragment() {

    private val MYTAG = "MySellFrag"
    private lateinit var mPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sell_chooseimage_button_id.setOnClickListener { selectPhoto() }

        sell_uploaditem_button_id.setOnClickListener {
            Toast.makeText(requireContext(),"Adding items to Store...",Toast.LENGTH_LONG)
            uploadImageToFireBaseStorage() }
    }

    private fun selectPhoto() {
        //Toast.makeText(activity, "Choose a photo", Toast.LENGTH_SHORT).show()
        Log.i(MYTAG,"User Is Choosing a Photo")

        //this allows us to go in their images and access their photos.
        val photoSelectIntent = Intent(Intent.ACTION_PICK)
        photoSelectIntent.type = "image/*"
        startActivityForResult(photoSelectIntent, 0)
    }



    //I put this out here cuz it needs to be seen by OnActResult() and UploadImageToFireBaseStorage()
    var mySelectedPhotoUri: Uri? = null




    //After we come back from selecting the photo we want
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.i(MYTAG, "Photo Was Selected")

            mySelectedPhotoUri = data.data

            //get the image
            val theBitMap = MediaStore.Images.Media.getBitmap(this.activity!!.contentResolver, mySelectedPhotoUri)
            val theBitMapPicture = BitmapDrawable(theBitMap)

            //set the image view = to the image we just selected
            item_image_id.setImageDrawable(theBitMapPicture)
        }
        //after picking their picture, the user should click the upload Item button.
        //which activated the onClickListener that calls uploadImageToFireBaseStorage()
    }




    private fun uploadImageToFireBaseStorage() {
        val itemName = editText_name_id.text.toString()
        val itemPrice = editText_price_id.text.toString()
        val itemSize = size_spinner_id.selectedItem.toString()


        if(itemName == "" || itemPrice == "" || itemSize == ""){
            Toast.makeText(context,"Some Parameters Incomplete",Toast.LENGTH_LONG).show()
            Log.i(MYTAG,"User Needs to Fill out Item Parameters")
            return
        }

        if (mySelectedPhotoUri == null) { //if no photo was selected
            Log.i(MYTAG,"User Tried to upload a picture of nothing")
            Toast.makeText(activity,"Upload Item Photo",Toast.LENGTH_LONG).show()
            return
        }

        Log.i(MYTAG, "Lets Upload that image")

        val filename = UUID.randomUUID().toString()//give the current item a unique ID in FB STORAGE

        //KOFI'S NEW CODE
        val myFireBaseRef = FirebaseStorage.getInstance().reference.child("/shopImages/$filename")

        val x = myFireBaseRef.putFile(mySelectedPhotoUri!!) //add the picture to firebase STORAGE

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
                Log.i(MYTAG, "LOOK RIGHT HERE(1)DOWNLOAD_URI"+downloadUri.toString())
                saveItemToFireBaseDataBase(downloadUri.toString())
            } else {
            }
        }
        //KOFI'S NEW CODE


        Log.i(MYTAG, "LOOK RIGHT HERE(2)SELECTED_PHOTO_URI"+mySelectedPhotoUri.toString())

        x.addOnSuccessListener {
            Log.i(MYTAG, "Successfully uploaded image: ${it.metadata?.path}")
            //saveItemToFireBaseDataBase()//After we upload the image we need to upload the item to database
        }
        x.addOnFailureListener{
            Log.i(MYTAG, "Failed to upload the image")
        }

        myFireBaseRef.downloadUrl.addOnSuccessListener {
            Log.i(MYTAG, "File Location $it")
        }.addOnFailureListener{
            Log.i(MYTAG, "I FAILED to download file location $it")
        }
    }




    private fun saveItemToFireBaseDataBase(itemUrl: String) {
        //TODO Also add the item to the users List of items.
        //It should have the same ID in both... Maybe items should have an ID param

        //getting the user email from shared preferences
        val fragPref = this.activity!!.getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
        val thisUsersEmail = fragPref.getString("EMAIL","No email")

        //this is redundant, I could just pass in the values as parameters. =|
        val itemName = editText_name_id.text.toString()
        val itemSize = size_spinner_id.selectedItem.toString()
        val itemPrice = editText_price_id.text.toString()
        val itemBrand = editText_brand_id.text.toString()
        val itemCondition = condition_spinner_id.selectedItem.toString()
        val itemCategory = category_spinner_id.selectedItem.toString()
        val itemDescription = editText_describe_id.text.toString()
        val myItem = Items(itemName,itemSize,itemPrice,itemBrand,itemCondition, itemCategory,itemDescription)
        myItem.setEmail(thisUsersEmail!!)
        myItem.setUrl(itemUrl)


        val myRef = FirebaseDatabase.getInstance().getReference("mainShop") //get FB ref
        val itemID = myRef.push().key   //get unique ID for Item
        myItem.setID(itemID!!)//set the Items ID

        val x = myRef.child(itemID).setValue(myItem) //put the item in the DB
        x.addOnSuccessListener {
            Log.i(MYTAG, "We saved the Item")
            Toast.makeText(activity, "Uploaded", Toast.LENGTH_SHORT).show()
            cleanUp()
        }
    }




    private fun cleanUp(){
        editText_name_id.setText("")
        size_spinner_id.setSelection(0)
        editText_price_id.setText("")
        editText_brand_id.setText("")
        condition_spinner_id.setSelection(0)
        category_spinner_id.setSelection(0)
        editText_describe_id.setText("")
        item_image_id.setImageResource(R.drawable.photo_camera)
    }
}

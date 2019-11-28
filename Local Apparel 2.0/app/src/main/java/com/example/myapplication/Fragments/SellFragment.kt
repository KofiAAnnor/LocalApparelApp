package com.example.myapplication.Fragments


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import com.example.myapplication.Objects.User

import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_sell.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SellFragment : Fragment() {

    private val MYTAG = "MySellFrag"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sell, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseimage_button_id.setOnClickListener { selectPhoto() }
        uploaditem_button_id.setOnClickListener { uploadImageToFireBaseStorage() }
        takephoto_button_id.setOnClickListener {
            Toast.makeText(activity,"Not implemented yet",Toast.LENGTH_LONG)
        }
    }

    private fun selectPhoto() {
        Toast.makeText(activity, "Which Photo?!!", Toast.LENGTH_SHORT).show()
        Log.i(MYTAG,"You just clicked the choose image Icon")

        val photoSelectIntent = Intent(Intent.ACTION_PICK)
        photoSelectIntent.type = "image/*"
        startActivityForResult(photoSelectIntent, 0)
    }


    var mySelectedPhotoUri: Uri? = null

    //After we come back from selecting the photo we want
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.i("myRegistration Act", "Photo Was Selected")

            mySelectedPhotoUri = data.data
            val theBitMap = MediaStore.Images.Media.getBitmap(this.activity!!.contentResolver, mySelectedPhotoUri)
            val theBitMapPicture = BitmapDrawable(theBitMap)
            //set the image view = to the image we just selected
            item_image_id.setImageDrawable(theBitMapPicture)
        }
        //after this the user should click the Upload button
    }

    private fun uploadImageToFireBaseStorage() {
        //inside this method we create the new item and make sure
        // all the information is filled out.
        if (mySelectedPhotoUri == null) {
            return
        }
        Log.i("myReg", "Lets Upload that image")
        val filename = UUID.randomUUID().toString()
        val myFireBaseRef = FirebaseStorage.getInstance().getReference("/shopImages/$filename")

        val x = myFireBaseRef.putFile(mySelectedPhotoUri!!)
        x.addOnSuccessListener {
            Log.i("myRegistration Act", "Succesfully uploaded image: ${it.metadata?.path}")
            saveItemToFireBaseDataBase()
        }
        x.addOnFailureListener{
            Log.i("myRegistration Act", "Failed to upload the image")
        }
        myFireBaseRef.downloadUrl.addOnSuccessListener {
            Log.i("myRegistration Act", "File Location $it")
        }.addOnFailureListener{
            Log.i("myRegistration Act", "I FAILED to download file location $it")
        }
    }

    private fun saveItemToFireBaseDataBase() {
        //TODO Make it so that we get a new ID for each item that's added into the main shop
        //Also add the item to the users List of items.
        //It should have the same ID in both... Maybe items should have an ID.
        val itemID = FirebaseAuth.getInstance().uid ?: ""
        val myRef = FirebaseDatabase.getInstance().getReference("/mainShop/$itemID")


        val itemName = editText_name_id.text.toString()
        val itemSize = size_spinner_id.selectedItem.toString()
        val itemPrice = editText_price_id.text.toString()
        val brand = editText_brand_id.text.toString()
        val itemCondition = condition_spinner_id.selectedItem.toString()
        val itemCategory = category_spinner_id.selectedItem.toString()
        val itemDescription = editText_describe_id.text.toString()


        val myItem = Items(itemName,itemSize,itemPrice,brand,itemCondition,
            itemCategory,itemDescription)

        val x = myRef.setValue(myItem)
        x.addOnSuccessListener {
            Log.i("myRegistration Act", "We saved the Item")
            Toast.makeText(activity, "Item Successfully Uploaded", Toast.LENGTH_SHORT).show()
            cleanUp()
        }

    }

    private fun cleanUp(){
        editText_name_id.setText("")
        size_spinner_id.resetPivot()
        editText_price_id.setText("")
        editText_brand_id.setText("")
        condition_spinner_id.resetPivot()
        category_spinner_id.resetPivot()
        editText_describe_id.setText("")
        item_image_id.setImageResource(R.drawable.photo_camera)
    }
}

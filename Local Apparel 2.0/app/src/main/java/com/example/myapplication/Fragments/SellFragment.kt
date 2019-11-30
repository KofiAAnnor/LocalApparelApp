package com.example.myapplication.Fragments


import android.app.Activity
import android.content.Intent
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

        sell_uploaditem_button_id.setOnClickListener { uploadImageToFireBaseStorage() }

//        sell_takeAPic_button_id.setOnClickListener {
//            Toast.makeText(context,"Not implemented yet",Toast.LENGTH_LONG).show()
//        }
    }

    private fun selectPhoto() {
        Toast.makeText(activity, "Choose a photo", Toast.LENGTH_SHORT).show()
        Log.i(MYTAG,"You just clicked the choose image Icon")

        //this allows s to go in their images and access their photos.
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
            Log.i("MySellFrag", "Photo Was Selected")

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
        val itemBrand = editText_brand_id.text.toString()


        if(itemName == "" || itemPrice == "" || itemBrand == ""){
            Toast.makeText(context,"You need to fill out the Item Parameters",Toast.LENGTH_LONG).show()
            Log.i(MYTAG,"User Needs to Fill out Item Parameters")
            return
        }

        if (mySelectedPhotoUri == null) { //if no photo was selected
            Log.i(MYTAG,"User Tried to upload a picture of nothing")
            Toast.makeText(activity,"We need a Photo Of The Item",Toast.LENGTH_LONG).show()
            return
        }

        Log.i("myReg", "Lets Upload that image")

        val filename = UUID.randomUUID().toString()//give the current item a unique ID in FB STORAGE
        val myFireBaseRef = FirebaseStorage.getInstance().getReference("/shopImages/$filename")

        val x = myFireBaseRef.putFile(mySelectedPhotoUri!!) //add the picture to firebase STORAGE
        x.addOnSuccessListener {
            Log.i(MYTAG, "Successfully uploaded image: ${it.metadata?.path}")
            saveItemToFireBaseDataBase()//After we upload the image we need to upload the item to database
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

    private fun saveItemToFireBaseDataBase() {
        //TODO Also add the item to the users List of items.
        //It should have the same ID in both... Maybe items should have an ID param

        //this is redundant, I could just pass in the values as parameters. =|
        val itemName = editText_name_id.text.toString()
        val itemSize = size_spinner_id.selectedItem.toString()
        val itemPrice = editText_price_id.text.toString()
        val itemBrand = editText_brand_id.text.toString()
        val itemCondition = condition_spinner_id.selectedItem.toString()
        val itemCategory = category_spinner_id.selectedItem.toString()
        val itemDescription = editText_describe_id.text.toString()
        val myItem = Items(itemName,itemSize,itemPrice,itemBrand,itemCondition,
            itemCategory,itemDescription)


        //val itemID = FirebaseAuth.getInstance().uid ?: ""
        val myRef = FirebaseDatabase.getInstance().getReference("mainShop")
        val itemID = myRef.push().key
        myItem.setID(itemID!!)

        val x = myRef.child(itemID!!).setValue(myItem)
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

package com.example.myapplication.Objects

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.myapplication.R
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

private const val MYTAG = "myAdapterTag"
class MyItemsUpForSaleListAdapter (val mCtx: Context, val layoutResID: Int, val myItemsList: List<Items>):
    ArrayAdapter<Items>(mCtx,layoutResID,myItemsList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val myView: View = layoutInflater.inflate(layoutResID,null)
        val itemImage = myView.findViewById<ImageView>(R.id.storeItemsLayout_itemImage_id)
        val itemNameTV = myView.findViewById<TextView>(R.id.myItemsUpForSaleLayout_TextView_id)
        val itemBrandTV = myView.findViewById<TextView>(R.id.myItemsUpForSaleLayout_BrandTV_id)
        val updateButton = myView.findViewById<Button>(R.id.iufs_layout_update_button_id)
        val deleteButton = myView.findViewById<Button>(R.id.iufs_layout_delete_button_id)

        val theItem = myItemsList[position]


        itemNameTV.text = theItem.itemName
        itemBrandTV.text = "Brand: "+theItem.itemBrand
        Picasso.get().load(theItem.itemUrl).into(itemImage)

        updateButton.setOnClickListener {
            Toast.makeText(context,"Updating "+theItem.itemName,Toast.LENGTH_LONG).show()
            showUpdateDialog(theItem)
        }
        deleteButton.setOnClickListener {
            Toast.makeText(context,"You are Deleting "+theItem.itemName,Toast.LENGTH_LONG).show()
            showDeleteDialog(theItem)
        }
        return myView
    }

    private fun showDeleteDialog(theItem: Items) {

        val builder = AlertDialog.Builder(mCtx)
        //builder.setTitle("Delete Item")

        val inflater = LayoutInflater.from(mCtx)
        val myDeleteView = inflater.inflate(R.layout.delete_item_dialogue_layout,null)


        builder.setView(myDeleteView)
        val alert = builder.create()

        val yesButton = myDeleteView.findViewById<Button>(R.id.delete_yes_button_id)
        val noButton = myDeleteView.findViewById<Button>(R.id.delete_no_button_id)
        Log.i(MYTAG,"You clicked the delete button")
        yesButton.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().getReference("mainShop").child(theItem.itemID!!)

            //removing item
            dbRef.removeValue()

            Toast.makeText(mCtx, "Item Deleted", Toast.LENGTH_LONG).show()
            Log.i(MYTAG,"You Deleted the item")

            //dismiss the dialogue
            alert.dismiss()
        }

        noButton.setOnClickListener {
            alert.dismiss()
        }

        alert.show()
    }

    private fun showUpdateDialog(theItem: Items) {
        val builder = AlertDialog.Builder(mCtx)
//        builder.setTitle("Update Item")

        val inflater = LayoutInflater.from(mCtx)
        val myUpdateView = inflater.inflate(R.layout.update_item_dialogue_layout,null)

        val changeNameETV = myUpdateView.findViewById<TextView>(R.id.changeName_id)
        val changeBrandETV = myUpdateView.findViewById<TextView>(R.id.changeBrand_id)
        val changePriceETV = myUpdateView.findViewById<TextView>(R.id.changePrice_id)
        val changeDescriptionETV = myUpdateView.findViewById<TextView>(R.id.changeDescription_id)

        //now the views in the dialogue box contain the items current values
        changeNameETV.text = theItem.itemName
        changeBrandETV.text=theItem.itemBrand
        changePriceETV.text=theItem.itemPrice
        changeDescriptionETV.text=theItem.itemDescription


        //set view to our builder. Whatever that means.
        builder.setView(myUpdateView)


        builder.setPositiveButton("UPDATE ITEM"
        ) { dialog, which ->
            //get FB reference
            val dbItem = FirebaseDatabase.getInstance().getReference("mainShop")

            //get the new values that the user typed in
            val newItemName = changeNameETV.text.toString().trim()
            val newItemBrand = changeBrandETV.text.toString().trim()
            val newItemPrice = changePriceETV.text.toString().trim()
            val newItemDescription = changeDescriptionETV.text.toString().trim()

            //if they didnt give a new name or brand
            if(newItemName.isEmpty()|| newItemBrand.isEmpty() || newItemPrice.isEmpty() || newItemDescription.isEmpty()){
                changeNameETV.error = "Please Enter a Name"
                changeNameETV.requestFocus()
                //return@setPositiveButton //this is the equivalent of a return inside a lambda or inner class
            }else {
                //change the information
                theItem.setName(newItemName)
                theItem.setBrand(newItemBrand)
                theItem.setPrice(newItemPrice)
                theItem.setDescription(newItemDescription)
                dbItem.child(theItem.itemID!!).setValue(theItem).addOnSuccessListener {
                    Toast.makeText(mCtx,"Item Updated",Toast.LENGTH_LONG).show()
                }
            }
        }

        builder.setNegativeButton("CANCEL"
        ) { dialog, which ->
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        val alert = builder.create()
        alert.show()


    }
}
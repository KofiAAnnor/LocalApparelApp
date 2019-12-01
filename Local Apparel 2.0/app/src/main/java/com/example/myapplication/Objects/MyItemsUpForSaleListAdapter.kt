package com.example.myapplication.Objects

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.google.firebase.database.FirebaseDatabase

private const val MYTAG = "myAdapterTag"
class MyItemsUpForSaleListAdapter (val mCtx: Context, val layoutResID: Int, val myItemsList: List<Items>):
    ArrayAdapter<Items>(mCtx,layoutResID,myItemsList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val myView: View = layoutInflater.inflate(layoutResID,null)

        val itemNameTV = myView.findViewById<TextView>(R.id.myItemsUpForSaleLayout_TextView_id)
        val itemBrandTV = myView.findViewById<TextView>(R.id.myItemsUpForSaleLayout_BrandTV_id)
        val updateButton = myView.findViewById<Button>(R.id.iufs_layout_update_button_id)
        val deleteButton = myView.findViewById<Button>(R.id.iufs_layout_delete_button_id)

        val theItem = myItemsList[position]

        itemNameTV.text = "Item Name: "+theItem.itemName
        itemBrandTV.text = "Item Brand: "+theItem.itemBrand

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
        builder.setTitle("Delete Item")

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
        builder.setTitle("Update Item")
        val inflater = LayoutInflater.from(mCtx)

        val myUpdateView = inflater.inflate(R.layout.update_item_dialogue_layout,null)

        val changeNameETV = myUpdateView.findViewById<TextView>(R.id.changeName_id)
        val changeBrandETV = myUpdateView.findViewById<TextView>(R.id.changeBrand_id)

        //now the views in the dialogue box contain the items current values
        changeNameETV.text = theItem.itemName
        changeBrandETV.text=theItem.itemBrand

        //set view to our builder. Whatever that means.
        builder.setView(myUpdateView)


        builder.setPositiveButton("Update"
        ) { dialog, which ->
            //get FB reference
            val dbItem = FirebaseDatabase.getInstance().getReference("mainShop")

            //get the new values that the user typed in
            val newItemName = changeNameETV.text.toString().trim()
            val newItemBrand = changeBrandETV.text.toString().trim()

            //if they didnt give a new name or brand
            if(newItemName.isEmpty()|| newItemBrand.isEmpty()){
                changeNameETV.error = "Please Enter a Name"
                changeNameETV.requestFocus()
                //return@setPositiveButton //this is the equivalent of a return inside a lambda or inner class
            }else {
                //change the information
                theItem.setName(newItemName)
                theItem.setBrand(newItemBrand)
                dbItem.child(theItem.itemID!!).setValue(theItem).addOnSuccessListener {
                    Toast.makeText(mCtx,"Item Updated",Toast.LENGTH_LONG).show()
                }
            }
        }

        builder.setNegativeButton("No"
        ) { dialog, which ->
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        val alert = builder.create()
        alert.show()


    }
}
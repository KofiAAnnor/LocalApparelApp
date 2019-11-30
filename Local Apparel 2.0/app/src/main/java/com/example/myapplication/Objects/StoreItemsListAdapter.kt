package com.example.myapplication.Objects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.myapplication.R

private const val MYTAG = "myStoreAdapterTag"
class StoreItemsListAdapter(val mCtx: Context, val layoutResID: Int, val myItemsList: List<Items>):
    ArrayAdapter<Items>(mCtx,layoutResID,myItemsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val myView: View = layoutInflater.inflate(layoutResID,null)

        val itemNameTV = myView.findViewById<TextView>(R.id.storeItemsLayout_itemName_id)
        val itemBrandTV = myView.findViewById<TextView>(R.id.storeItemsLayout_itemBrand_id)
        val wishListButton = myView.findViewById<Button>(R.id.storeItemsLayout_WishListButton_id)
        val messageButton = myView.findViewById<Button>(R.id.storeItemsLayout_msgButton_id)
        val itemImage = myView.findViewById<ImageView>(R.id.storeItemsLayout_itemImage_id)
        //Todo I wanna put an onclick listener on the item so it can show more info about the item.

        val theItem = myItemsList[position]

        itemNameTV.text = "Item Name: "+theItem.itemName
        itemBrandTV.text = "Item Brand: "+theItem.itemBrand


        messageButton.setOnClickListener {
            Toast.makeText(context,"Message", Toast.LENGTH_LONG).show()
        }
        wishListButton.setOnClickListener {
            Toast.makeText(context,"WishList", Toast.LENGTH_LONG).show()
        }
        itemImage.setOnClickListener {
            Toast.makeText(context,"Image", Toast.LENGTH_LONG).show()
            //showItemPageDialog(theItem)
        }

        return myView
    }

}
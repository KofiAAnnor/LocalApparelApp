package com.example.myapplication.Objects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.content.Intent
import android.R
import android.net.Uri
import com.example.myapplication.R.*
import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_layout.view.*


private const val MYTAG = "myStoreAdapterTag"

class StoreItemsListAdapter(
    val mCtx: Context, val layoutResID: Int, val myItemsList: List<Items>,
    sOIS: Bundle?
) :
    ArrayAdapter<Items>(mCtx, layoutResID, myItemsList) {
    val thisOIS = sOIS
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val myView: View = layoutInflater.inflate(layoutResID, null)

        val itemImageView = myView.findViewById<ImageView>(id.storeItemsLayout_itemImage_id)
        val itemBrandTV = myView.findViewById<TextView>(id.storeItemsLayout_itemBrand_id)
        val itemNameTV = myView.findViewById<TextView>(id.storeItemsLayout_itemName_id)
        val itemPriceTV = myView.findViewById<TextView>(id.storeItemsLayout_itemPrice_id)
        val itemSizeTV = myView.findViewById<TextView>(id.storeItemsLayout_itemSize_id)
        val itemEmailTV = myView.findViewById<TextView>(id.storeItemsLayout_itemEmail_id)
        val wishListButton = myView.findViewById<Button>(id.storeItemsLayout_WishListButton_id)
        val messageButton = myView.findViewById<Button>(id.storeItemsLayout_msgButton_id)
        //Todo I wanna put an onclick listener on the item so it can show more info about the item.
        val theItem = myItemsList[position]

        itemBrandTV.text = theItem.itemBrand
        itemNameTV.text = theItem.itemName
        itemPriceTV.text = "$" + theItem.itemPrice
        itemSizeTV.text = "Size: " + theItem.itemSize
        itemEmailTV.text = "Seller Email: " + theItem.itemEmail
        Picasso.get().load(theItem.itemUrl).into(itemImageView)




        messageButton.setOnClickListener {
            Toast.makeText(context, "Message", Toast.LENGTH_LONG).show()
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:" + theItem.itemEmail)
            try {
                startActivity(context, emailIntent, thisOIS)
            } catch (e: ActivityNotFoundException) {
                return@setOnClickListener
            }
        }
        wishListButton.setOnClickListener {
            Toast.makeText(context, "WishList", Toast.LENGTH_LONG).show()
        }
        itemImageView.setOnClickListener {
            Toast.makeText(context, "Image", Toast.LENGTH_LONG).show()
            //showItemPageDialog(theItem)
        }

        return myView
    }

}
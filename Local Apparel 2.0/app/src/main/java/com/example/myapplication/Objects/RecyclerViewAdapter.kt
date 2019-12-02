package com.example.myapplication.Objects

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_layout.view.*



class RecyclerViewAdapter (val myItemsList: List<Items>, val mCtx: Context, savedInstanceState: Bundle?
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    val thisOIS = savedInstanceState
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val theItemNameTV = itemView.findViewById<TextView>(R.id.recycler_layout_itemName_id)
        val theItemPriceTV = itemView.findViewById<TextView>(R.id.recycler_layout_itemPrice_id)
        val theItemSizeTV = itemView.findViewById<TextView>(R.id.recycler_layout_itemSize_id)
        val theItemEmailTV = itemView.findViewById<TextView>(R.id.recycler_layout_itemEmail_id)
        val theDetailsButton = itemView.findViewById<Button>(R.id.recycler_layout_DetailsButton_id)
        val theMessageButton = itemView.findViewById<Button>(R.id.recycler_layout_MessageButton_id)
        val thePicture = itemView.findViewById<ImageView>(R.id.recycler_layout_imageView_id)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_layout,parent,false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return myItemsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Items = myItemsList[position]

        holder.theItemNameTV.text = "Item Name: " +item.itemName
        holder.theItemPriceTV.text = "$" + item.itemPrice
        holder.theItemSizeTV.text = "Size: " +item.itemSize
        holder.theItemEmailTV.text = "Email: " +item.itemEmail
        Picasso.get().load(item.itemUrl).into(holder.thePicture.recycler_layout_imageView_id)


        holder.theDetailsButton.setOnClickListener {
            Toast.makeText(mCtx,"You Want Details?",Toast.LENGTH_LONG).show()
            showDetailedItemPageDialog(item)
        }

        holder.theMessageButton.setOnClickListener {
            val emailSubject = "Hello, I would like to buy Your "+item.itemName

            val uriText = "mailto:"+item.itemEmail +
                    "?subject=" + Uri.encode(emailSubject)
            val uri = Uri.parse(uriText)

            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = uri
            try {
                startActivity(mCtx, emailIntent, thisOIS)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(mCtx,e.message,Toast.LENGTH_LONG)
            }
        }


    }



    private fun showDetailedItemPageDialog(theItem: Items) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("More Details...")

        val inflater = LayoutInflater.from(mCtx)
        val detailsView = inflater.inflate(R.layout.detailed_items_dialog_layout,null)

        builder.setView(detailsView)
        val alert = builder.create()

        val detailed_image = detailsView.findViewById<ImageView>(R.id.detailed_item_image_id)
        val detailed_name = detailsView.findViewById<TextView>(R.id.detailed_item_name_id)
        val detailed_price = detailsView.findViewById<TextView>(R.id.detailed_item_price_id)
        val detailed_brand = detailsView.findViewById<TextView>(R.id.detailed_item_brand_id)
        val detailed_cond = detailsView.findViewById<TextView>(R.id.detailed_item_condition_id)
        val detailed_size = detailsView.findViewById<TextView>(R.id.detailed_item_size_id)
        val detailed_descr = detailsView.findViewById<TextView>(R.id.detailed_item_description_id)
        val detailed_category = detailsView.findViewById<TextView>(R.id.detailed_item_category_id)
        val dismissButton = detailsView.findViewById<TextView>(R.id.detailed_dismiss_button_id)


        Picasso.get().load(theItem.itemUrl).into(detailed_image)
        detailed_name.text = "Item Name: "+theItem.itemName
        detailed_price.text = "$" + theItem.itemPrice
        detailed_brand.text = "Brand: "+theItem.itemBrand
        detailed_cond.text = "Condition: "+theItem.itemCondition
        detailed_size.text = "Size: "+theItem.itemSize
        detailed_descr.text = "Description: "+theItem.itemDescription
        detailed_category.text = "Category: "+theItem.itemCategory
        dismissButton.setOnClickListener {
            alert.dismiss()
        }
        alert.show()
    }
}
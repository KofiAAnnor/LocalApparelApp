package com.example.myapplication.Objects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class RecyclerView_Adapter (val myItemsList: ArrayList<Items>) : RecyclerView.Adapter<RecyclerView_Adapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val theItemNameTV = itemView.findViewById<TextView>(R.id.recycler_layout_itemName_id)
        val theItemPriceTV = itemView.findViewById<TextView>(R.id.recycler_layout_itemPrice_id)
        val theDetailsButton = itemView.findViewById<Button>(R.id.recycler_layout_DetailsButton_id)
        val theMessageButton = itemView.findViewById<Button>(R.id.recycler_layout_MessageButton_id)
        val theWishListButton = itemView.findViewById<Button>(R.id.recycler_layout_WIshListButton_id)
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

        holder.theItemNameTV.text = item.itemName
        holder.theItemPriceTV.text = item.itemPrice

    }

}
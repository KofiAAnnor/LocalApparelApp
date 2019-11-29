package com.example.myapplication.Objects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.R

class MyItemsUpForSaleListAdapter (val mCtx: Context, val layoutResID: Int, val myItemsList: List<Items>):
    ArrayAdapter<Items>(mCtx,layoutResID,myItemsList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val myView: View = layoutInflater.inflate(layoutResID,null)

        val itemnameTV = myView.findViewById<TextView>(R.id.myItemsUpForSaleLayout_TextView_id)
        val itemBrandTV = myView.findViewById<TextView>(R.id.myItemsUpForSaleLayout_BrandTV_id)
        val item = myItemsList[position]
        itemnameTV.text = "Item Name: "+item.itemName
        itemBrandTV.text = "Item Brand: "+item.itemBrand

        return myView
    }
}
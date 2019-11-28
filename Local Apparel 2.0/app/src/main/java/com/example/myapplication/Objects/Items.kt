package com.example.myapplication.Objects

import java.util.concurrent.locks.Condition

class Items {

    var itemName: String? = null
    var itemSize: String? = null
    var itemPrice: String? = null
    var brand: String? = null
    var itemCondition: String? = null
    var itemCategory: String? = null
    var itemDescription: String? = null

    constructor(name:String, size:String, price:String, brand:String,itemCondition: String,
                itemCategory: String, itemDescription: String){
        this.itemName = name
        this.itemSize = size
        this.itemPrice = price
        this.brand = brand
        this.itemCondition = itemCondition
        this.itemCategory = itemCategory
        this.itemDescription = itemDescription
    }

    constructor(name:String, size:String, price:String){
        this.itemName = name
        this.itemSize = size
        this.itemPrice = price
    }
}
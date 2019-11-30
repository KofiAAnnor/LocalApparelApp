package com.example.myapplication.Objects

class User {
    var userID: String? = null
    var userName: String? = null
    var userEmail: String? = null
    var userPassWord: String? = null
    var userZipCode: String? = null
    var userImageUrl: String? = null
    var itemsUpForSale: ArrayList<Items>? = null

    constructor(userID:String, userName:String, userEmail:String){
        this.userID = userID
        this.userName = userName
        this.userEmail = userEmail
        this.itemsUpForSale = ArrayList<Items>()
    }

    constructor(userID:String, userName:String, userEmail:String, userImageUrl:String){
        this.userID = userID
        this.userName = userName
        this.userEmail = userEmail
        this.userImageUrl = userImageUrl
        this.itemsUpForSale = ArrayList<Items>()
    }

    fun addItems(itemToAdd:Items){
        itemsUpForSale?.add(itemToAdd)
    }

    fun remoVeItem(itemToRemove:Items){
        itemsUpForSale?.remove(itemToRemove)
    }

    fun setPassword(pass:String){
        userPassWord = pass
    }

    fun setZipCode(zipCode: String){
        userZipCode = zipCode
    }
}
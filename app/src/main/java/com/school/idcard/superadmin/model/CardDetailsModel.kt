package com.school.idcard.superadmin.model

class CardDetailsModel {
    var cardType: String? = null
    var value: String? = null

    constructor(cardType:String, value:String){
        this.cardType = cardType
        this.value = value
    }

}
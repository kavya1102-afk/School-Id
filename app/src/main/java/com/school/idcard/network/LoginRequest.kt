package com.school.idcard.network

class LoginRequest (
    val email:String,
    val password:String
)


class AddSchoolRequest(
    val schoolName: String,
    val contactNo: String?,
    val address1: String,
    val address2: String,
    val city: String?,
    val pinCode: String,
    val state: String,
    val country: String,
    val schoolEmail: String,
    val principalName: String?,
    val agentId: String?,
    val status: String,
    val addField: List<String>,
    val customField: List<String>?
)

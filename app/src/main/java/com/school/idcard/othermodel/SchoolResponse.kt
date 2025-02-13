package com.school.idcard.othermodel

data class SchoolResponse(
    val schools: List<School>,
    val message: String,
    val status: String
)

data class School(
    val id: Int,
    val schoolId: String,
    val schoolName: String,
    val contactNo: String,
    val address1: String,
    val address2: String?,
    val city: String,
    val pinCode: String,
    val state: String,
    val country: String,
    val schoolEmail: String,
    val principalName: String,
    val agentId: String,
    val status: String,
    val createDate: String?,
    val idCardDesign: String?
)


data class SchoolsResponse2(
    val schools: List<SchoolSeperateList>?,
    val message: String?,
    val status: String?
)

data class SchoolSeperateList(
    val schoolId: String?,
    val schoolName: String?
)


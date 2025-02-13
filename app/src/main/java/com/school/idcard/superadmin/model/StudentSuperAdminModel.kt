package com.school.idcard.superadmin.model

import com.google.gson.annotations.SerializedName


class StudentSuperAdminModel (
    val students: List<StudentSuperAdmin>?,
    val message: String?,
    val status: String?
)

data class StudentSuperAdmin(
    val id: Int?,
    val schoolid: String?,
    val studentid: String?,
    val studentname: String?,
    val classno: String?,
    val mothername: String?,
    val fathername: String?,
    val contactno: String?,
    val section: String?,
    val gender: String?,
    val dateofbirth: String?,
    val address: String?,
    val admissiondate: String?,
    val details: String?,
    val photo: String?,
    val session: String?,
    val status: String?,
    val approvedTeacher: Boolean?,
    val approvedAdmin: Boolean?,
    val cardDesign: String?,
    val extrafieldsList: String?
)


data class StaffResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("staffs") val staffs: List<Staff>?,
    @SerializedName("status") val status: String?
)

data class Staff(
    @SerializedName("id") val id: Int?,
    @SerializedName("schoolId") val schoolId: String?,
    @SerializedName("staffId") val staffId: String?,
    @SerializedName("staffName") val staffName: String?,
    @SerializedName("contactNo") val contactNo: String?,
    @SerializedName("address1") val address1: String?,
    @SerializedName("address2") val address2: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("pinCode") val pinCode: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("roleType") val roleType: String?,
    @SerializedName("photo") val photo: String?,  // Assuming this is a URL or base64 string
    @SerializedName("status") val status: String?,
    @SerializedName("classNo") val classNo: String?,
    @SerializedName("section") val section: String?
)

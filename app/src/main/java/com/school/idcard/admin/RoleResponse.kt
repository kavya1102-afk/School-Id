package com.school.idcard.admin

data class RoleResponse(
    val roles: List<Role>,
    val message: String,
    val status: String
)

data class Role(
    val roleId: Int,
    val schoolId: String,
    val roleName: String
)

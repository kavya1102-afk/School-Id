package com.school.idcard.othermodel

data class LoginResponse(
    val message: String,
    val roleType: String,
    val email: String,
    val token: String,
    val status: String
) {
    companion object {
        fun fromJson(json: Map<String, Any?>): LoginResponse {
            return LoginResponse(
                message = json["message"] as? String ?: "",
                roleType = json["roleType"] as? String ?: "",
                email = json["email"] as? String ?: "",
                token = json["token"] as? String ?: "",
                status = json["status"] as? String ?: ""
            )
        }

        fun LoginResponse.toJson(): Map<String, Any> {
            return mapOf(
                "message" to message,
                "roleType" to roleType,
                "email" to email,
                "token" to token,
                "status" to status
            )
        }
    }
}

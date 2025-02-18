package com.school.idcard.othermodel

class ProfileResponse (
    val profile: AgentProfile?,
    val message: String?,
    val status: String?
)

data class AgentProfile(
    val id: Int?,
    val agentId: String?,
    val firstName: String?,
    val lastName: String?,
    val contactNo: String?,
    val address1: String?,
    val address2: String?,
    val agentEmail: String?,
    val status: Boolean?,
    val city: String?,
    val pinCode: String?,
    val state: String?,
    val country: String?,
    val agentType: String?,
    val startSub: String?, // Consider using LocalDate if needed
    val endSubs: String?,  // Consider using LocalDate if needed
    val subsStatus: String?,
    val noOfSchool: Int?,
    val noOfStudent: Int?,
    val noOfPendingCard: Int?,
    val noOfSubmittedCard: Int?,
    val noOfApprovedCard: Int?,
    val noOfReceivePrintCard: Int?,
    val noOfPrintProgressCard: Int?,
    val noOfPrintedCard: Int?,
    val noOfDeliveredCard: Int?
)

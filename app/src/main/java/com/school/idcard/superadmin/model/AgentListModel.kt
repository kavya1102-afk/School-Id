package com.school.idcard.superadmin.model

class AgentListModel (
    val message: String,
    val status: String,
    val agents: List<Agent>
)

data class Agent(
    val id: Int,
    val agentId: String,
    val firstName: String,
    val lastName: String,
    val contactNo: String,
    val address1: String,
    val address2: String?,
    val agentEmail: String,
    val status: Boolean,
    val city: String,
    val pinCode: String,
    val state: String,
    val country: String,
    val agentType: String,
    val startSub: String?,
    val endSubs: String?,
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
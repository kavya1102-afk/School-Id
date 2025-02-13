package com.school.idcard.othermodel

data class AgentResponse(
    val agent: Agent
)

data class Agent(
    val id: Int,
    val agentId: String,
    val firstName: String,
    val lastName: String,
    val contactNo: String,
    val agentEmail: String,
    val address1: String,
    val city: String,
    val pinCode: String,
    val state: String,
    val country: String,
    val agentType: String,
    val startSub: String?,
    val endSub: String?,
    val status: Boolean
)


data class AgentResponse2(
    val message: String,
    val agents: List<Agent2>,
    val status: String
)

data class Agent2(
    val agentId: String,
    val agentLastName: String,
    val agentEmail: String,
    val agentFirstName: String
)


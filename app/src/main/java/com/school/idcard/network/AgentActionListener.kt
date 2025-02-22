package com.school.idcard.network

interface AgentActionListener {
    fun onDeleteAgent(agentId: String)
    fun onDeleteStaff(agentId: String)

}


interface SchoolActionListener {
    fun onDeleteSchool(schoolId: String)

}

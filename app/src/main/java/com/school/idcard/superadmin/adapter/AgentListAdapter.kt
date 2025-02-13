package com.school.idcard.superadmin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.school.idcard.R
import com.school.idcard.network.AgentActionListener
import com.school.idcard.superadmin.activity.AddAgentActivity
import com.school.idcard.superadmin.activity.AgentDetailsActivity
import com.school.idcard.superadmin.model.Agent

class AgentListAdapter(
    private val list: ArrayList<Agent>,
    private val type: String,
    var context: Context,
    private val listener: AgentActionListener
) : RecyclerView.Adapter<AgentListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.agent_details_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.agentId.text = list[position].agentId
        holder.agentName.text = "${list[position].firstName} ${list[position].lastName}"
        holder.agentAddress.text = list[position].address1
        holder.agentContact.text = list[position].contactNo
        holder.agentEmail.text = list[position].agentEmail
        holder.schoolNumber.text = list[position].noOfSchool.toString()
        holder.subscription.text = "${list[position].startSub} to ${list[position].endSubs}"
        if(list[position].status){
            holder.status.text = "Active"
        }else{
            holder.status.text = "In-Active"
        }

        if (list[position].agentType == "PREMIUM") {
            holder.agentSubscriptionLayout.visibility = View.VISIBLE
        } else {
            holder.agentSubscriptionLayout.visibility = View.GONE
        }

        if (list[position].status==false) {
            holder.status.setTextColor(Color.RED)
        }else{
            holder.status.setTextColor(Color.GREEN)
        }

        holder.editAgent.setOnClickListener {
            context.startActivity(
                Intent(context, AddAgentActivity::class.java)
                    .putExtra("type", 1)
                    .putExtra("id", list[position].id.toString())
            )
        }

        holder.deleteAgent.setOnClickListener {
            showDeleteDialog(context) {
                listener.onDeleteAgent(list[position].agentId.toString()) // Call the interface function
            }
        }

        holder.viewInfoBtn.setOnClickListener {
            context.startActivity(Intent(context, AgentDetailsActivity::class.java))
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val agentId: TextView = itemView.findViewById(R.id.agentId)
        val agentName: TextView = itemView.findViewById(R.id.agentName)
        val agentAddress: TextView = itemView.findViewById(R.id.agentaddress)
        val agentContact: TextView = itemView.findViewById(R.id.agentContact)
        val agentEmail: TextView = itemView.findViewById(R.id.agentEmail)
        val schoolNumber: TextView = itemView.findViewById(R.id.agentAddSchool)
        val subscription: TextView = itemView.findViewById(R.id.agentSubscription)
        val status: TextView = itemView.findViewById(R.id.agentStatus)
        val agentSubscriptionLayout: LinearLayout =
            itemView.findViewById(R.id.agentSubscriptionLayout)
        val editAgent: ImageView = itemView.findViewById(R.id.editAgent)
        val deleteAgent: ImageView = itemView.findViewById(R.id.deleteAgent)
        val viewInfoBtn: ImageView = itemView.findViewById(R.id.viewInfoBtn)
    }

    private fun showDeleteDialog(context: Context, onConfirmDelete: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Delete") { _, _ ->
                onConfirmDelete()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
package com.school.idcard.superadmin.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.school.idcard.R
import com.school.idcard.network.SchoolActionListener
import com.school.idcard.othermodel.School
import com.school.idcard.superadmin.activity.AddSchoolActivity

class SchoolListAdapter(
    private val list: List<School>,
    private val type: String,
    var context: Context,
    private val listener: SchoolActionListener
) : RecyclerView.Adapter<SchoolListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.school_item_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.schoolId.text = list[position].schoolId
        holder.schoolName.text = list[position].schoolName
        holder.schoolAddress.text = list[position].address1
        holder.schoolContact.text = list[position].contactNo
        holder.schoolEmail.text = list[position].schoolEmail
        holder.schoolNumber.text = list[position].principalName
        holder.agentName.text = list[position].agentName
        holder.date.text = list[position].createDate
        holder.status.text = list[position].status

        if (list[position].status.equals("Active", ignoreCase = true)) {
            holder.status.setTextColor(Color.GREEN)
        } else {
            holder.status.setTextColor(Color.RED)
        }

        holder.editSchool.setOnClickListener {
            context.startActivity(
                Intent(context, AddSchoolActivity::class.java)
                    .putExtra("type", 1)
                    .putExtra("schoolId",list[position].id)
            )
        }

        holder.deleteSchool.setOnClickListener {
            showDeleteDialog(context) {
                listener.onDeleteSchool(list[position].id.toString()) // Call the interface function
            }
        }

        holder.viewInfoBtn.visibility=View.INVISIBLE


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val schoolId: TextView = itemView.findViewById(R.id.schoolId)
        val schoolName: TextView = itemView.findViewById(R.id.schoolName)
        val schoolAddress: TextView = itemView.findViewById(R.id.schooladdress)
        val schoolContact: TextView = itemView.findViewById(R.id.schoolContact)
        val schoolEmail: TextView = itemView.findViewById(R.id.schoolEmail)
        val schoolNumber: TextView = itemView.findViewById(R.id.schoolPrincipal)
        val agentName: TextView = itemView.findViewById(R.id.schoolAgentName)
        val date: TextView = itemView.findViewById(R.id.schoolDate)
        val status: TextView = itemView.findViewById(R.id.schoolStatus)
        val editSchool: ImageView = itemView.findViewById(R.id.editschool)
        val deleteSchool: ImageView = itemView.findViewById(R.id.deleteschool)
        val viewInfoBtn: ImageView = itemView.findViewById(R.id.viewInfoBtn)
    }

    private fun showDeleteDialog(context: Context, onConfirmDelete: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete this School?")
            .setPositiveButton("Delete") { _, _ ->
                onConfirmDelete()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
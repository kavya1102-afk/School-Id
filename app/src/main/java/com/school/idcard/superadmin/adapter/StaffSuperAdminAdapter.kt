package com.school.idcard.superadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.school.idcard.R
import com.school.idcard.network.AgentActionListener
import com.school.idcard.superadmin.model.Staff

class StaffSuperAdminAdapter(private val context: Context, private val list:ArrayList<Staff>, private val listener: AgentActionListener
):RecyclerView.Adapter<StaffSuperAdminAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.staff_details_item,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data=list[position]

        holder.staffId.text=data.staffId
        holder.staffName.text=data.staffName?.replaceFirstChar { it.uppercase() } ?: ""
        holder.classStudent.text = data.roleType?.replaceFirstChar { it.uppercase() } ?: ""
        holder.address.text=data.address1?.replaceFirstChar { it.uppercase() } ?: ""
        holder.contact.text=data.contactNo
        holder.status.text=data.status?.replaceFirstChar { it.uppercase() } ?: ""

        holder.deleteStaff.setOnClickListener {
            showDeleteDialog(context) {
                listener.onDeleteStaff(list[position].id.toString()) // Call the interface function
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val staffId: TextView = itemView.findViewById(R.id.staffId)
        val staffName: TextView = itemView.findViewById(R.id.staffName)
        val classStudent: TextView = itemView.findViewById(R.id.staffRole)
        val address: TextView = itemView.findViewById(R.id.staffAddress)
        val contact: TextView = itemView.findViewById(R.id.staffContact)
        val status: TextView = itemView.findViewById(R.id.staffStatus)
        val deleteStaff: ImageView = itemView.findViewById(R.id.deleteStaff)


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
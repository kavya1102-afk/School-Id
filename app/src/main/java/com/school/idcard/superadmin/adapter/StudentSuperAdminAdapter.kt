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
import com.school.idcard.superadmin.model.StudentSuperAdmin

class StudentSuperAdminAdapter(private val context: Context, private val list:ArrayList<StudentSuperAdmin>, private val listener: AgentActionListener
):RecyclerView.Adapter<StudentSuperAdminAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.student_details_item,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data=list[position]

        holder.studentId.text=data.studentid
        holder.studentName.text=data.studentname
        holder.classStudent.text=data.classno
        holder.section.text=data.section
        holder.address.text=data.address
        holder.contact.text=data.contactno
        holder.fatherName.text=data.fathername
        holder.status.text=data.status
        holder.deleteStudent.setOnClickListener {
            showDeleteDialog(context) {
                listener.onDeleteAgent(list[position].id.toString()) // Call the interface function
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val studentId: TextView = itemView.findViewById(R.id.studentId)
        val studentName: TextView = itemView.findViewById(R.id.studentName)
        val classStudent: TextView = itemView.findViewById(R.id.studentClass)
        val section: TextView = itemView.findViewById(R.id.schoolSection)
        val address: TextView = itemView.findViewById(R.id.studentAddress)
        val contact: TextView = itemView.findViewById(R.id.studentContact)
        val fatherName: TextView = itemView.findViewById(R.id.studentFatherName)
        val status: TextView = itemView.findViewById(R.id.studentStatus)
        val deleteStudent: ImageView = itemView.findViewById(R.id.deleteStudent)


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
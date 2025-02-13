package com.school.idcard.superadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.school.idcard.R
import com.school.idcard.superadmin.model.CardDetailsModel

class super_details_card_adapter(private val examList: List<CardDetailsModel>) :
    RecyclerView.Adapter<super_details_card_adapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_card_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cardType.text=examList[position].cardType
        holder.cardValue.text=examList[position].value

    }

    override fun getItemCount(): Int {
        return examList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardType: TextView = itemView.findViewById(R.id.cardType)
        val cardValue: TextView = itemView.findViewById(R.id.cardValue)
//        val cardContainer: LinearLayout = itemView.findViewById(R.id.cardContainer)
    }


}
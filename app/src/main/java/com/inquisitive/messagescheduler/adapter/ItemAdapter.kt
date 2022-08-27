package com.inquisitive.messagescheduler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inquisitive.messagescheduler.ContactModel
import com.inquisitive.messagescheduler.R
import com.inquisitive.messagescheduler.editText
import com.inquisitive.messagescheduler.recyclerView

lateinit var numdrop:String
 class ItemAdapter(private var contactList: List<ContactModel>) :
    RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {
     class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         var name: TextView = view.findViewById(R.id.name)
         var number: TextView = view.findViewById(R.id.number)
     }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val contact = contactList[position]
        holder.name.text = contact.getName()
        holder.number.text = contact.getNum()
        holder.itemView.setOnClickListener{
            editText.setText(holder.name.text.toString())
            recyclerView.visibility =View.GONE
            numdrop=holder.number.text.toString()
        }
    }
     override fun getItemCount(): Int {
        return contactList.size
    }
     fun updateList(list: List<ContactModel> ){
    contactList=list
         notifyDataSetChanged()
     }

    }



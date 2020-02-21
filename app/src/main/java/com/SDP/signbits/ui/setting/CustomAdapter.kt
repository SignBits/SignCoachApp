package com.SDP.signbits.ui.setting

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.SDP.signbits.R

class CustomAdapter(val userList: ArrayList<User>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){


    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val textViewName = itemView.findViewById(R.id.textViewUsername) as TextView
            val textViewAddress  = itemView.findViewById(R.id.textViewAddress) as TextView
//            textViewName.text = user.name
//            textViewAddress.text = user.address

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.list_layout,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val user:User = userList[p1]
        p0?.textViewName.text = user.name
        p0?.textViewAddress.text = user.address
    }

}
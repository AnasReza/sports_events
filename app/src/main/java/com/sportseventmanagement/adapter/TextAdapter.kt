package com.sportseventmanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.sportseventmanagement.R

class TextAdapter(

    val list: Array<String>

) : RecyclerView.Adapter<TextAdapter.TextHolder>() {
    private var notifyHolder: TextHolder? = null


    class TextHolder(view: View) : RecyclerView.ViewHolder(view) {

        var text: TextView = view.findViewById(R.id.mainText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_layout, parent, false)
        notifyHolder = TextHolder(itemView)
        return notifyHolder as TextHolder
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: TextHolder, position: Int) {
        holder.text.text = list[position]
    }


}
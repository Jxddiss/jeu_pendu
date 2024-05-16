package com.example.myapplication.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Mot


class RecyclerAdapter(var motList:ArrayList<Mot>):
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>()
{
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var lettresText : TextView
        var langueText : TextView
        var difficulteText : TextView

        init {

            lettresText = itemView.findViewById(R.id.lettres)
            langueText = itemView.findViewById(R.id.langue)
            difficulteText = itemView.findViewById(R.id.difficulte)
        }

    }

    var onItemClick: ((Mot) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView : View = LayoutInflater.from(parent.context).inflate(R.layout.mot,parent,false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return motList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var let : String = motList[position].lettres
        var lang : String = motList[position].langue
        var dif : String = motList[position].difficulte

        holder.lettresText.text = let
        holder.langueText.text = lang
        holder.difficulteText.text = dif


        holder.itemView.setOnClickListener {
            onItemClick?.invoke(motList[position])
        }
    }

}
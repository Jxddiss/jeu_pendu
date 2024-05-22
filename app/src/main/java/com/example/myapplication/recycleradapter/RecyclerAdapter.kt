package com.example.myapplication.recycleradapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databasehelper.DatabaseHelper
import com.example.myapplication.databasehelper.MotDAO
import com.example.myapplication.model.Mot

class RecyclerAdapter(
    private val context: Context,
    private var motList: ArrayList<Mot>,
    private var francais: Boolean,
    private val ressources: Resources
) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private val databaseHelper = DatabaseHelper(context)
    private val motDAO = MotDAO(databaseHelper)

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var motText: TextView = itemView.findViewById(R.id.mot)
        var difficulteText: TextView = itemView.findViewById(R.id.difficulte)
        var btnRetirer: Button = itemView.findViewById(R.id.boutonRetirer)
    }

    var onItemClick: ((Mot) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.mot, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return motList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mot = motList[position]

        if (francais) {
            holder.motText.text = mot.motFrancais
        } else {
            holder.motText.text = mot.motAnglais
        }

        holder.difficulteText.text = ContextCompat.getString(
            context,
            ressources.getIdentifier(mot.difficulte, "string", context.packageName)
        )

        holder.btnRetirer.setOnClickListener {
            motDAO.deleteMot(mot.id.toString())
            motList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, motList.size)
        }
    }
}
package com.example.myapplication.recycleradapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.PartieJouee
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RecyclerAdapterPartieJouer(private val context: Context,
                                 private var partieList: ArrayList<PartieJouee>
) : RecyclerView.Adapter<RecyclerAdapterPartieJouer.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var motText: TextView = itemView.findViewById(R.id.motPartie)
        var difficulteText: TextView = itemView.findViewById(R.id.difficultePartie)
        var tempText: TextView = itemView.findViewById(R.id.tempPartie)
        var imgReussite: ImageView = itemView.findViewById(R.id.reussitePartieImageView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.partie_jouee, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return partieList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val partie = partieList[position]

        holder.motText.text = partie.mot
        holder.tempText.text = partie.temps
            .toDuration(DurationUnit.MILLISECONDS)
            .toString(DurationUnit.SECONDS,2)
        holder.difficulteText.text = partie.difficulte
        if (partie.reussite){
            holder.imgReussite.setBackgroundResource(R.drawable.success)
            holder.motText.setTextColor(Color.parseColor("#02b311"))
        }else{
            holder.imgReussite.setBackgroundResource(R.drawable.failed)
            holder.motText.setTextColor(Color.RED)
        }
    }
}
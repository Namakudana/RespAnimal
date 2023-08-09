package com.mardana.respanimal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mardana.respanimal.R
import com.mardana.respanimal.model.MaterialModel

class MaterialAdapter(
    private val materialList: List<MaterialModel>,
    private val onClick: (MaterialModel) -> Unit
) :
    RecyclerView.Adapter<MaterialAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPicture: ImageView = view.findViewById(R.id.tvpicture)
        val ivTitle: TextView = view.findViewById(R.id.tvtitle)
        val tvSummary: TextView = view.findViewById(R.id.tvsummary)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_material, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.itemView.context)
            .load(materialList[position].picture)
            .into(viewHolder.tvPicture)
        viewHolder.ivTitle.text = materialList[position].title
        viewHolder.tvSummary.text = materialList[position].summary
        viewHolder.itemView.setOnClickListener {
            onClick(materialList[position])
        }
    }

    override fun getItemCount() = materialList.size
}

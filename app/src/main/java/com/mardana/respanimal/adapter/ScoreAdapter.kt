package com.mardana.respanimal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mardana.respanimal.R
import com.mardana.respanimal.model.ScoreModel

class ScoreAdapter(
    private val scoreList: List<ScoreModel>,
    private val onClick: (ScoreModel) -> Unit
) :
    RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tv_username)
        val tvUserRoom: TextView = view.findViewById(R.id.tv_user_room)
        val tvScore: TextView = view.findViewById(R.id.tv_score)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_score, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvUsername.text = "Nama: ${scoreList[position].name.toString()}"
        viewHolder.tvUserRoom.text = "Kelas: ${scoreList[position].room.toString()}"
        viewHolder.tvScore.text = "Nilai: ${scoreList[position].score.toString()}"
    }

    override fun getItemCount() = scoreList.size
}
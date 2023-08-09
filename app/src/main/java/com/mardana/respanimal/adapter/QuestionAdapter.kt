package com.mardana.respanimal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mardana.respanimal.R
import com.mardana.respanimal.model.QuestionModel

class QuestionAdapter(
    private val questionList: List<QuestionModel>,
    private val onClick: (QuestionModel) -> Unit
) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivQuestion: TextView = view.findViewById(R.id.tvquestion)
        val tvAnswer: TextView = view.findViewById(R.id.tvanswer)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_question, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.ivQuestion.text = questionList[position].question
        viewHolder.tvAnswer.text = questionList[position].option?.get(questionList[position].answer)
            ?: ""
        viewHolder.itemView.setOnClickListener {
            onClick(questionList[position])
        }
    }

    override fun getItemCount() = questionList.size
}
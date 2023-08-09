package com.mardana.respanimal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.mardana.respanimal.adapter.QuestionAdapter
import com.mardana.respanimal.databinding.ActivityViewQuestionBinding
import com.mardana.respanimal.model.QuestionModel

class ViewQuestion : AppCompatActivity() {
    private lateinit var binding: ActivityViewQuestionBinding
    private lateinit var recycler: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private val questionList = mutableListOf<QuestionModel>()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, EditorQuestion::class.java))
        }

        setupData()
    }

    private fun setupData() {
        db.collection("question").addSnapshotListener { value, error ->
            questionList.clear()
            if (value != null && !value.isEmpty) {
                value.documents.forEach { snapshot ->
                    val getQuestion = snapshot.toObject(QuestionModel::class.java)
                    getQuestion?.let { question ->
                        questionList.add(question)
                    }
                }
            } else {
                questionList.clear()
                Log.e("ViewQuestion", "data kosong atau null")
            }
            setupAdapter()
        }
    }

    private fun setupAdapter() {
        Log.e("ViewQuestion", questionList.size.toString())
        recycler = binding.recyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        questionAdapter =
            QuestionAdapter(questionList.sortedByDescending { it.lastUpdatedDate }) { question ->
                val intent = Intent(this, EditorQuestion::class.java)
                intent.putExtra(EditorQuestion.IntentId.questionExtra, question)
                startActivity(intent)
            }
        recycler.adapter = questionAdapter
    }
}
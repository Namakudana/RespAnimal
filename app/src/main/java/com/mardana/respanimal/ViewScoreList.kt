package com.mardana.respanimal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.mardana.respanimal.adapter.ScoreAdapter
import com.mardana.respanimal.databinding.ActivityViewScoreListBinding
import com.mardana.respanimal.model.ScoreModel

class ViewScoreList : AppCompatActivity() {
    private lateinit var binding: ActivityViewScoreListBinding
    private lateinit var recycler: RecyclerView
    private lateinit var scoreAdapter: ScoreAdapter
    private val scoreList = mutableListOf<ScoreModel>()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewScoreListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
    }

    private fun setupData() {
        db.collection("score").addSnapshotListener { value, error ->
            scoreList.clear()
            if (value != null && !value.isEmpty) {
                value.documents.forEach { snapshot ->
                    val getMaterial = snapshot.toObject(ScoreModel::class.java)
                    getMaterial?.let { score ->
                        scoreList.add(score)
                    }
                }
            } else {
                scoreList.clear()
            }
            setupAdapter()
        }
    }

    private fun setupAdapter() {
        recycler = binding.recyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        scoreAdapter =
            ScoreAdapter(scoreList) { _ ->
                // Nothing
            }
        recycler.adapter = scoreAdapter
    }
}
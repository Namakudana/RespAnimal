package com.mardana.respanimal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mardana.respanimal.databinding.ActivityViewQuizBinding

class ViewQuiz : AppCompatActivity() {
    private lateinit var binding: ActivityViewQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, EditorQuiz::class.java))
        }
    }
}
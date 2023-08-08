package com.mardana.respanimal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mardana.respanimal.databinding.ActivityEditorMaterialBinding
import com.mardana.respanimal.databinding.ActivityEditorQuizBinding
import com.mardana.respanimal.databinding.ActivityMainBinding

class EditorQuiz : AppCompatActivity() {
    private lateinit var binding: ActivityEditorQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
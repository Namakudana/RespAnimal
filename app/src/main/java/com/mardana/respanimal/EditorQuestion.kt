package com.mardana.respanimal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mardana.respanimal.databinding.ActivityEditorQuestionBinding

class EditorQuestion : AppCompatActivity() {
    private  val items = arrayOf("Pilihan A","Pilihan B","Pilihan C","Pilihan D")
    private lateinit var binding: ActivityEditorQuestionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
package com.mardana.respanimal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mardana.respanimal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExit.setOnClickListener {
            (this as Activity).finish()
        }

        binding.btnMateri.setOnClickListener {
            startActivity(Intent(this, ViewMaterial::class.java))
        }

        binding.btnQuiz.setOnClickListener {
            startActivity(Intent(this, ViewQuestion::class.java))
        }

        binding.btnNilai.setOnClickListener {
            startActivity(Intent(this, ViewScoreList::class.java))
        }

    }
}
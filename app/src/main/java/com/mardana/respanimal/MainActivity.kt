package com.mardana.respanimal

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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

        binding.btnNilai.setOnClickListener {
            startActivity(Intent(this, ViewScoreList::class.java))
        }
    }
}
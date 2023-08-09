package com.mardana.respanimal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.mardana.respanimal.adapter.MaterialAdapter
import com.mardana.respanimal.databinding.ActivityViewMaterialBinding
import com.mardana.respanimal.model.MaterialModel

class ViewMaterial : AppCompatActivity() {
    private lateinit var binding: ActivityViewMaterialBinding
    private lateinit var recycler: RecyclerView
    private lateinit var materialAdapter: MaterialAdapter
    private val materialList = mutableListOf<MaterialModel>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, EditorMaterial::class.java))
        }
    }

    private fun setupData() {
        db.collection("material").addSnapshotListener { value, error ->
            materialList.clear()
            if (value != null && !value.isEmpty) {
                value.documents.forEach { snapshot ->
                    val getMaterial = snapshot.toObject(MaterialModel::class.java)
                    getMaterial?.let { material ->
                        materialList.add(material)
                    }
                }
                setupAdapter()
            } else {
                materialList.clear()
                setupAdapter()
            }
        }
    }

    private fun setupAdapter() {
        recycler = binding.recyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        materialAdapter =
            MaterialAdapter(materialList.sortedByDescending { it.lastUpdatedDate }) { material ->
                val intent = Intent(this, EditorMaterial::class.java)
                intent.putExtra(EditorMaterial.IntentId.materialExtra, material)
                startActivity(intent)
            }
        recycler.adapter = materialAdapter
    }
}
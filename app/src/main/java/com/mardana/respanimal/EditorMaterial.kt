package com.mardana.respanimal

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mardana.respanimal.databinding.ActivityEditorMaterialBinding
import com.mardana.respanimal.model.MaterialModel
import com.mardana.respanimal.utils.CAMERA_PERMISSION
import com.mardana.respanimal.utils.Pattern
import com.mardana.respanimal.utils.bitmapToFile
import com.mardana.respanimal.utils.generateRandomId
import com.mardana.respanimal.utils.timestampToDate
import com.mardana.respanimal.utils.uriToFile
import java.io.File

class EditorMaterial : AppCompatActivity() {
    private lateinit var binding: ActivityEditorMaterialBinding
    private var getFile: File? = null
    private val storageRef = Firebase.storage.reference
    private val db = FirebaseFirestore.getInstance()

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent!!, 10)
            } else {
                Toast.makeText(
                    this, "Silahkan izinkan aplikasi mengakses kamera", Toast.LENGTH_SHORT
                ).show()
            }
        }

    object IntentId {
        const val materialExtra = "MATERIAL_EXTRA"
    }

    private fun deleteMaterial(id: String) {
        binding.btnDelete.isEnabled = false
        db.collection("material").document(id).delete().addOnSuccessListener {
            Toast.makeText(this, "Berhasil menghapus data", Toast.LENGTH_SHORT).show()
            onBackPressed()
            binding.btnDelete.isEnabled = true
        }.addOnFailureListener {
            binding.btnDelete.isEnabled = true
            Log.e("EditorMaterial", it.message.toString())
            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val getMaterial = intent.getParcelableExtra<MaterialModel>(IntentId.materialExtra)
        if (intent.extras != null) {
            setupData(getMaterial)
        }

        binding.picture.setOnClickListener {
            showDialog()
        }
        binding.btnSave.setOnClickListener {
            when {
                (getFile == null) -> {
                    Toast.makeText(this, "Silahkan tambahkan gambar", Toast.LENGTH_SHORT).show()
                }

                (binding.title.text.toString() == "") -> {
                    binding.title.error = "Silahkan isi kolom judul"
                }

                (binding.summary.text.toString() == "") -> {
                    binding.summary.error = "Silahkan isi kolom ringkasan"
                }

                (binding.details.text.toString() == "") -> {
                    binding.details.error = "Silahkan isi kolom detail"
                }

                else -> {
                    uploadImage { data, success ->
                        if (success) {
                            if (intent.extras != null) {
                                val newData = Pair(
                                    getMaterial?.id?:"0", MaterialModel(
                                        id = getMaterial?.id,
                                        title = binding.title.text.toString(),
                                        picture = data.toString(),
                                        summary = binding.summary.text.toString(),
                                        detail = binding.details.text.toString(),
                                        createdDate = getMaterial?.createdDate,
                                        lastUpdatedDate = System.currentTimeMillis(),
                                    )
                                )
                                saveData(newData)
                            } else {
                                val generateId = generateRandomId()
                                val newData = Pair(
                                    generateId, MaterialModel(
                                        id = generateId,
                                        title = binding.title.text.toString(),
                                        picture = data.toString(),
                                        summary = binding.summary.text.toString(),
                                        detail = binding.details.text.toString(),
                                        createdDate = System.currentTimeMillis(),
                                        lastUpdatedDate = System.currentTimeMillis(),
                                    )
                                )
                                saveData(newData)
                            }
                        } else {
                            Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()
                            binding.btnSave.isEnabled = true
                        }
                    }
                }
            }
        }
        binding.btnDelete.setOnClickListener {
            deleteMaterial(getMaterial?.id ?: "0")
        }
    }

    private fun setupData(getMaterial: MaterialModel?) {
        val context = this
        Glide.with(this)
            .asBitmap()
            .load(getMaterial?.picture)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    getFile = bitmapToFile(context, resource)
                    binding.picture.setImageBitmap(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // Nothing
                }
            })
        binding.title.setText(getMaterial?.title)
        binding.summary.setText(getMaterial?.summary)
        binding.details.setText(getMaterial?.detail)
        binding.timeMade.text = getString(R.string.created_date, getMaterial?.createdDate?.timestampToDate(Pattern.dateTimePattern))
        binding.editedTime.text = getString(R.string.edit_date, getMaterial?.lastUpdatedDate?.timestampToDate(Pattern.dateTimePattern))
        binding.timeMade.visibility = View.VISIBLE
        binding.editedTime.visibility = View.VISIBLE
        binding.btnDelete.visibility = View.VISIBLE
    }

    private fun uploadImage(callback: (data: Any, success: Boolean) -> Unit) {
        binding.btnSave.isEnabled = false
        val file = Uri.fromFile(getFile)
        val riversRef =
            storageRef.child("images").child("material").child(file.lastPathSegment.toString())
        riversRef.putFile(file).addOnSuccessListener {
            it.metadata?.reference?.downloadUrl?.let { task ->
                task.addOnCompleteListener {
                    callback(task.result,true)
                }
            }
        }.addOnFailureListener {
            callback(it.message.toString(),false)
        }
    }

    private fun saveData(material: Pair<String, MaterialModel>) {
        db.collection("material").document(material.first).set(material.second)
            .addOnSuccessListener {
                binding.btnSave.isEnabled = true
                Toast.makeText(this, "Berhasil Menyimpan Data", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }.addOnFailureListener {
                Log.e("Editor Material", it.message.toString())
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDialog() {
        val context = this
        val dialogItem = arrayOf(
            "Kamera", "Galeri"
        )
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Pilih tindakan")
            var intent: Intent
            setItems(dialogItem) { dialog, which ->
                when (which) {
                    0 -> {
                        // Kamera
                        if (ContextCompat.checkSelfPermission(
                                context, CAMERA_PERMISSION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestCameraPermissionLauncher.launch(CAMERA_PERMISSION)
                        } else {
                            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, 10)
                        }
                    }

                    1 -> {
                        intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 20)
                    }

                    else -> {
                        // Nothing
                    }
                }
            }
        }
        builder.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                10 -> {
                    if (data != null) {
                        val imageBitmap = data.extras?.get("data")
                        imageBitmap?.let {
                            getFile = bitmapToFile(this, it as Bitmap)
                            binding.picture.setImageBitmap(it)

                        }
                    }
                }

                20 -> {
                    if (data != null) {
                        data.data?.let {
                            getFile = uriToFile(it, this)
                            binding.picture.setImageURI(it)
                        }
                    }
                }
            }
        }
    }
}
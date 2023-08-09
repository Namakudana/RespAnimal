package com.mardana.respanimal

import android.R
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mardana.respanimal.databinding.ActivityEditorQuestionBinding
import com.mardana.respanimal.model.QuestionModel
import com.mardana.respanimal.utils.Pattern
import com.mardana.respanimal.utils.generateRandomId
import com.mardana.respanimal.utils.timestampToDate

class EditorQuestion : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityEditorQuestionBinding
    private val db = FirebaseFirestore.getInstance()
    private var selectedOption = "A"
    private val answer = arrayOf("A", "B", "C", "D")

    object IntentId {
        const val questionExtra = "QUESTION_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val getQuestion = intent.getParcelableExtra<QuestionModel>(IntentId.questionExtra)
        if (intent.extras != null) {
            setupData(getQuestion)
            binding.btnDelete.setOnClickListener {
                deleteQuestion(getQuestion?.id ?: "0")
            }
        } else {
            setupAnswerSpinner(null)
        }

        binding.btnSave.setOnClickListener {
            when {
                (binding.option1.text.toString().isEmpty()) -> {
                    binding.option1.error = "Silahkan lengkapi jawaban A"
                }

                (binding.option2.text.toString().isEmpty()) -> {
                    binding.option2.error = "Silahkan lengkapi jawaban B"
                }

                (binding.option3.text.toString().isEmpty()) -> {
                    binding.option3.error = "Silahkan lengkapi jawaban C"
                }

                (binding.option4.text.toString().isEmpty()) -> {
                    binding.option4.error = "Silahkan lengkapi jawaban D"
                }

                else -> {
                    binding.btnSave.isEnabled = false
                    val currentTime = System.currentTimeMillis()
                    val newData: QuestionModel?
                    val option = mapOf(
                        "a" to binding.option1.text.toString(),
                        "b" to binding.option2.text.toString(),
                        "c" to binding.option3.text.toString(),
                        "d" to binding.option4.text.toString(),
                    )
                    if (intent.extras != null) {
                        newData = QuestionModel(
                            id = getQuestion?.id,
                            question = binding.question.text.toString(),
                            option = option,
                            answer = selectedOption.lowercase(),
                            createdDate = getQuestion?.createdDate,
                            lastUpdatedDate = currentTime,
                        )
                    } else {
                        val newId = generateRandomId()
                        newData = QuestionModel(
                            id = newId,
                            question = binding.question.text.toString(),
                            option = option,
                            answer = selectedOption.lowercase(),
                            createdDate = currentTime,
                            lastUpdatedDate = currentTime,
                        )
                    }
                    saveQuestion(newData)
                }
            }
        }
    }

    private fun setupData(questionModel: QuestionModel?) {
        with(binding) {
            val rightOption = questionModel?.answer ?: "A"
            question.setText(questionModel?.question ?: "")
            option1.setText(questionModel?.option?.get("a") ?: "")
            option2.setText(questionModel?.option?.get("b") ?: "")
            option3.setText(questionModel?.option?.get("c") ?: "")
            option4.setText(questionModel?.option?.get("d") ?: "")
            setupAnswerSpinner(rightOption)
            selectedOption = rightOption
            binding.timeMade.text = getString(
                com.mardana.respanimal.R.string.created_date,
                questionModel?.createdDate?.timestampToDate(
                    Pattern.dateTimePattern
                )
            )
            binding.editedTime.text = getString(
                com.mardana.respanimal.R.string.edit_date,
                questionModel?.lastUpdatedDate?.timestampToDate(
                    Pattern.dateTimePattern
                )
            )

            binding.timeMade.visibility = View.VISIBLE
            binding.editedTime.visibility = View.VISIBLE
            btnDelete.visibility = View.VISIBLE
            btnSave.text = "Ubah"
        }
    }

    private fun setupAnswerSpinner(currentAnswer: String?) {
        val aa = ArrayAdapter(this, R.layout.simple_spinner_item, answer)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        with(binding.mySpinner) {
            adapter = aa
            onItemSelectedListener = this@EditorQuestion
            prompt = "Silahkan pilih jawaban"
            gravity = Gravity.CENTER
            if (currentAnswer != null) {
                setSelection(answer.indexOf(currentAnswer.uppercase()))
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedOption = answer[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Nothing
    }

    private fun saveQuestion(question: QuestionModel) {
        db.collection("question").document(question.id ?: "0").set(question)
            .addOnSuccessListener {
                binding.btnSave.isEnabled = true
                Toast.makeText(this, "Berhasil Menyimpan Data", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }.addOnFailureListener {
                binding.btnSave.isEnabled = true
                Log.e("Editor Question", it.message.toString())
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteQuestion(id: String) {
        binding.btnDelete.isEnabled = false
        db.collection("question").document(id).delete().addOnSuccessListener {
            Toast.makeText(this, "Berhasil menghapus data", Toast.LENGTH_SHORT).show()
            onBackPressed()
            binding.btnDelete.isEnabled = true
        }.addOnFailureListener {
            binding.btnDelete.isEnabled = true
            Log.e("EditorQuestion", it.message.toString())
            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
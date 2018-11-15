package com.mxcrtr.notepadtest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.File

class MenuActivity : AppCompatActivity() {

    private var viewWork: Button? = null
    private var editWork: Button? = null
    private var addWork: Button? = null

    private var homeworkTextField: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val path = filesDir
        val directory = File(path, "HWFolder")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "HWData.txt")

        viewWork = findViewById(R.id.viewSavedHomework)
        editWork = findViewById(R.id.editSavedHomework)
        addWork = findViewById(R.id.addHomework)

        homeworkTextField = findViewById(R.id.typeHomework)

        viewWork?.setOnClickListener {
            val intent = Intent(this, ScrollActivity::class.java)
            startActivity(intent)
        }

        editWork?.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        addWork?.setOnClickListener {
            if (!homeworkTextField?.text?.equals("")!!) {
                val homework = homeworkTextField?.text.toString()
                Toast.makeText(this, homework, Toast.LENGTH_SHORT).show()
                file.appendText("\n" + homework)
            }

            homeworkTextField?.setText("")
            addWork?.isEnabled = false
        }

        homeworkTextField?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                canWorkBeAdded()
            }

            override fun afterTextChanged(p0: Editable?) {
                canWorkBeAdded()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                canWorkBeAdded()
            }
        })

    }

    private fun canWorkBeAdded() {
        homeworkTextField?.text?.isNullOrEmpty().apply {
            addWork?.isEnabled = false
        }

        homeworkTextField?.text?.isNullOrEmpty().apply {
            addWork?.isEnabled = true
        }
    }

    /**
    val SHIFT = 52

    fun getEncryptedText(text: String) : String {
        println("YEETING")
        var encryptedText = ""
        for (letter in text) {
            var ascii: Int = letter.toInt()
            ascii += SHIFT
            encryptedText += ascii.toChar()
        }

        return encryptedText
    }*/

}


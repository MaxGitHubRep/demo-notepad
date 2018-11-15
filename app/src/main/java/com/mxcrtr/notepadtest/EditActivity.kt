package com.mxcrtr.notepadtest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.lang.Exception

class EditActivity : AppCompatActivity() {

    var display: EditText? = null
    var finished: Button? = null
    var abort: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        display = findViewById(R.id.display)
        finished = findViewById(R.id.saveChanges)
        abort = findViewById(R.id.abort)

        val path = filesDir
        val directory = File(path, "HWFolder")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "HWData.txt")

        var inputAsString = ""

        try {
            inputAsString  = FileInputStream(file).bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            Toast.makeText(this, "OH FUCK: " + ex, Toast.LENGTH_LONG).show()
        }

        display?.setText(inputAsString)

        finished?.setOnClickListener {
            file.writeText("", Charsets.UTF_8)
            file.appendText(display?.text.toString())

            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()

            var intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        abort?.setOnClickListener {
            var intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }


    }



}

package com.mxcrtr.notepadtest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_scroll.*
import java.io.File
import java.io.FileInputStream
import java.lang.Exception

class ScrollActivity : AppCompatActivity() {

    var display: TextView? = null
    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll)

        display = findViewById(R.id.display)
        button = findViewById(R.id.goBack)

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

        display?.text = inputAsString

        goBack.setOnClickListener {
            var intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

    }

}

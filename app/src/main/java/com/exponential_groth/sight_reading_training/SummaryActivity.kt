package com.exponential_groth.sight_reading_training

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class SummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)


        val display : TextView = findViewById(R.id.display)
        val durationDisplay : TextView = findViewById(R.id.durationDisplay)
        val btn : Button = findViewById(R.id.btn)

        var type = "0"


        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 0).toString()
        }

        if (type == "1") {
            if (intent.hasExtra("percentage")) {
                display.text = getString(R.string.percentage_display, "${intent.getDoubleExtra("percentage", 100.0)}%")
            }
        } else {
            if (intent.hasExtra("streak")) {
                display.text = getString(R.string.streak_display, intent.getIntExtra("streak", 0).toString())
            }
        }

        if (intent.hasExtra("duration")) {
            durationDisplay.text = getString(R.string.duration_display, intent.getDoubleExtra("duration", 0.0).toString())
        }


        btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
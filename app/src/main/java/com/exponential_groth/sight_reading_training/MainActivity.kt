package com.exponential_groth.sight_reading_training

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.postDelayed
import androidx.core.view.marginBottom
import androidx.preference.PreferenceManager
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random

import androidx.appcompat.content.res.AppCompatResources.getDrawable as gD



class MainActivity : AppCompatActivity() {

    var inSettings = false

    var lowestTone : String = "f"
    var highestTone : String = "c3"
    var sleepingDuration = 1.0
    var endOfSession = 0
    var numOfRounds = 0



    var note = ""
    var noteIndex = 0
    var time = 0L
    var correctNotes = 0
    var durations = arrayListOf<Double>()


    private lateinit var notes : ArrayList<String>
    lateinit var whiteKeys : List<Button>
    private lateinit var images : ArrayList<Int>
    lateinit var imageSwitcher : ImageView
    lateinit var display : TextView
    lateinit var durationDisplay : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        lowestTone = sharedPreferences.getString(getString(R.string.lowest_tone_key), "f").toString()
        highestTone = sharedPreferences.getString(getString(R.string.highest_tone_key), "c3").toString()
        sleepingDuration = sharedPreferences.getString(getString(R.string.pause_key), "1.0")?.toDouble() ?: 1.0
        endOfSession = sharedPreferences.getString(getString(R.string.session_ending_key), "0")?.toInt() ?: 0
        numOfRounds = sharedPreferences.getString(getString(R.string.num_of_rounds_key), "10")?.toInt() ?: 10

        notes = arrayListOf("f", "g", "a", "h")
        for (i in "12") {
            for (char in "cdefgah") {
                notes.add("$char$i")
            }
        }
        notes.add("c3")

        images = arrayListOf(R.drawable.f0)
        images.add(R.drawable.g0)
        images.add(R.drawable.a0)
        images.add(R.drawable.h0)
        images.add(R.drawable.c1)
        images.add(R.drawable.d1)
        images.add(R.drawable.e1)
        images.add(R.drawable.f1)
        images.add(R.drawable.g1)
        images.add(R.drawable.a1)
        images.add(R.drawable.h1)
        images.add(R.drawable.c2)
        images.add(R.drawable.d2)
        images.add(R.drawable.e2)
        images.add(R.drawable.f2)
        images.add(R.drawable.g2)
        images.add(R.drawable.a2)
        images.add(R.drawable.h2)
        images.add(R.drawable.c3)

        val inflater = LayoutInflater.from(this)
        val inflatedView = inflater.inflate(R.layout.activity_main, null)
        setContentView(inflatedView)


        val fis : Button = findViewById(R.id.fis)
        val gis : Button = findViewById(R.id.gis)
        val ais : Button = findViewById(R.id.ais)
        val cis1 : Button = findViewById(R.id.cis1)
        val dis1 : Button = findViewById(R.id.dis1)
        val fis1 : Button = findViewById(R.id.fis1)
        val gis1 : Button = findViewById(R.id.gis1)
        val ais1 : Button = findViewById(R.id.ais1)
        val cis2 : Button = findViewById(R.id.cis2)
        val dis2 : Button = findViewById(R.id.dis2)
        val fis2 : Button = findViewById(R.id.fis2)
        val gis2 : Button = findViewById(R.id.gis2)
        val ais2 : Button = findViewById(R.id.ais2)

        val f : Button = findViewById(R.id.f)
        val g : Button = findViewById(R.id.g)
        val a : Button = findViewById(R.id.a)
        val h : Button = findViewById(R.id.h)
        val c1 : Button = findViewById(R.id.c1)
        val d1 : Button = findViewById(R.id.d1)
        val e1 : Button = findViewById(R.id.e1)
        val f1 : Button = findViewById(R.id.f1)
        val g1 : Button = findViewById(R.id.g1)
        val a1 : Button = findViewById(R.id.a1)
        val h1 : Button = findViewById(R.id.h1)
        val c2 : Button = findViewById(R.id.c2)
        val d2 : Button = findViewById(R.id.d2)
        val e2 : Button = findViewById(R.id.e2)
        val f2 : Button = findViewById(R.id.f2)
        val g2 : Button = findViewById(R.id.g2)
        val a2 : Button = findViewById(R.id.a2)
        val h2 : Button = findViewById(R.id.h2)
        val c3 : Button = findViewById(R.id.c3)

        val settingsButton : ImageButton = findViewById(R.id.settingsBtn)
        val startButton : Button = findViewById(R.id.startBtn)
        imageSwitcher = findViewById(R.id.switcher)


        display = findViewById(R.id.display)
        display.text = when (endOfSession) {
            0 -> getString(R.string.streak_display, correctNotes.toString())
            else -> getString(R.string.percentage_display, "0")
        }

        durationDisplay = findViewById(R.id.durationDisplay)
        durationDisplay.text = getString(R.string.duration_display, "0")

        val piano : ConstraintLayout = findViewById(R.id.piano)

        val blackKeys = listOf(fis, gis, ais, cis1, dis1, fis1, gis1, ais1, cis2, dis2, fis2, gis2, ais2)
        whiteKeys = listOf(f, g, a, h, c1, d1, e1, f1, g1, a1, h1, c2, d2, e2, f2, g2, a2, h2, c3)


        f.measure(0, 0)
        val width = convertPixelsToDp((f.measuredWidth * 1.352).toInt(), this)
        Log.println(Log.ERROR, "width", width.toString())
        for (key in blackKeys) {
            setMargins(key, width/2, 0, width / 2, key.marginBottom)
            key.visibility = View.VISIBLE
            key.requestLayout()
        }



/*        imageSwitcher.setFactory(ViewSwitcher.ViewFactory {

            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER

            return@ViewFactory imageView
        })*/


        startButton.setOnClickListener {
            val startNum = notes.indexOf(lowestTone)
            val endNum = notes.indexOf(highestTone)
            noteIndex = Random.nextInt(startNum,endNum+1)
            note = notes[noteIndex]

            startButton.visibility = View.INVISIBLE
            startButton.isClickable = false

            imageSwitcher.setImageResource(images[noteIndex])

            time = System.nanoTime()
        }


        settingsButton.setOnClickListener {
            inSettings = true
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


    }


    override fun onResume() {
        super.onResume()

        if (inSettings) {
            inSettings = false
            super.recreate()
        }
    }


    @SuppressLint("StringFormatMatches")
    fun onWhiteKeyPress(view: View) {
        val dt = (System.nanoTime() - time) / 10.0.pow(9)
        durations.add(dt)
        durationDisplay.text = getString(R.string.duration_display, ((durations.average()*10).roundToInt().toDouble()/10.0).toString())

        if (view.id == whiteKeys[noteIndex].id) {
            view.setBackgroundResource(R.drawable.green_key)
            correctNotes++

            Handler(Looper.getMainLooper()).postDelayed({
                view.background = gD(this, R.drawable.white_key)

                if (endOfSession == 0 || numOfRounds > durations.size) {
                    val startNum = notes.indexOf(lowestTone)
                    val endNum = notes.indexOf(highestTone)
                    noteIndex = Random.nextInt(startNum,endNum+1)
                    note = notes[noteIndex]

                    imageSwitcher.setImageResource(images[noteIndex])
                    time = System.nanoTime()
                } else {
                    val intent = Intent(this, SummaryActivity::class.java)
                    intent.putExtra("type", endOfSession) // always 1
                    intent.putExtra("percentage", (1000.0 * correctNotes.toDouble() / durations.size.toDouble()).roundToInt().toDouble() / 10.0)
                    intent.putExtra("duration", (durations.average()*10).roundToInt().toDouble()/10.0)
                    startActivity(intent)
                }


            }, (sleepingDuration * 1000).roundToLong())


        } else {
            view.setBackgroundResource(R.drawable.red_key)


            Handler(Looper.getMainLooper()).postDelayed({
                view.background = gD(this, R.drawable.white_key)

                if (endOfSession == 1 && numOfRounds > durations.size) {
                    val startNum = notes.indexOf(lowestTone)
                    val endNum = notes.indexOf(highestTone)
                    noteIndex = Random.nextInt(startNum,endNum+1)
                    note = notes[noteIndex]

                    imageSwitcher.setImageResource(images[noteIndex])
                    time = System.nanoTime()
                } else {
                    val intent = Intent(this, SummaryActivity::class.java)
                    intent.putExtra("type", endOfSession)
                    intent.putExtra("streak", correctNotes)
                    intent.putExtra("duration", (durations.average()*10).roundToInt().toDouble()/10.0)
                    startActivity(intent)
                }


            }, (sleepingDuration * 1000).roundToLong())

        }

        display.text = when (endOfSession) {
            0 -> getString(R.string.streak_display, correctNotes.toString())
            else -> getString(R.string.percentage_display, "${(100.0 * correctNotes.toDouble() / durations.size.toDouble()).roundToInt()}")
        }

    }






    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }
    private fun convertPixelsToDp(px: Int, context: Context): Int {
        return px / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

}
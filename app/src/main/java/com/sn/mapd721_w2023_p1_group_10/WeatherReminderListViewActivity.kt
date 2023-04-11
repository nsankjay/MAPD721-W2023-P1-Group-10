package com.sn.mapd721_w2023_p1_group_10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class WeatherReminderListViewActivity : AppCompatActivity() {

    private lateinit var ReminderGoBackToWeatherViewBtn: Button

    var REMINDERCITY: String = "Vancouver,CA"
    val REMINDERAPI: String = "3b57b3d787ba93b37afb428cb50d2cdd"

    private lateinit var setReminderCityTxt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_reminder_list_view)


        //Assigning current city for display and global variable
        var FRCSTCITY = intent.getStringExtra("EXTRA_FORECASTCITY")
        REMINDERCITY = FRCSTCITY.toString()

        //Setting the Edit text with city value
        setReminderCityTxt = findViewById<EditText>(R.id.enterReminderCityTxt)
        setReminderCityTxt.setText(REMINDERCITY)

        ReminderGoBackToWeatherViewBtn = findViewById(R.id.reminderGoBackToWeatherViewBtn)

        ReminderGoBackToWeatherViewBtn.setOnClickListener {
            Intent(this, MainWeatherViewActivity::class.java).also {

                startActivity(it)
            }
        }

    }

    private fun setNewReminderCity()
    {
        val cityText=findViewById<EditText>(R.id.enterReminderCityTxt)
        val enteredCity = cityText.text.toString()
        REMINDERCITY = enteredCity

    }
}
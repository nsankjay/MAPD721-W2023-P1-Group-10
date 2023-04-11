package com.sn.mapd721_w2023_p1_group_10

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class WeatherForecastViewActivity : AppCompatActivity() {

    var length: Int = 0
    var arrayNum: Int = 0
    var temperature: String = ""
    var forecastDescription: String = ""
    var forecastDate: String = ""
    var forecastImage: Int = 0

    var FORECASTCITY: String = "Vancouver,CA"
    val FORECASTAPI: String = "3b57b3d787ba93b37afb428cb50d2cdd"
    var FORECASTCOUNT: Int = 40

    private lateinit var getForcastBtn: Button
    private lateinit var goToMainWeatherViewBtn: Button

    private lateinit var setFrcstCityTxt: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast_view)

        //Assigning current city for display and global variable
        var FRCSTCITY = intent.getStringExtra("EXTRA_FORECASTCITY")
        FORECASTCITY = FRCSTCITY.toString()

        //Setting the Edit text with city value
        setFrcstCityTxt = findViewById<EditText>(R.id.enterCityTxt)
        setFrcstCityTxt.setText(FORECASTCITY)

        forecastWeatherTask().execute()


        goToMainWeatherViewBtn = findViewById(R.id.goToMainWeatherViewBtn)

        goToMainWeatherViewBtn.setOnClickListener {
            Intent(this, MainWeatherViewActivity::class.java).also {

                startActivity(it)
            }
        }

        getForcastBtn = findViewById(R.id.getForecastBtn)

        getForcastBtn.setOnClickListener {

            setNewCity()
            forecastWeatherTask().execute()
            arrayNum = 0

        }
    }

    inner class forecastWeatherTask() : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.forecastLoader).visibility = View.VISIBLE
            findViewById<TextView>(R.id.forecastErrorText).visibility = View.GONE

        }

        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/forecast?q=$FORECASTCITY&cnt=$FORECASTCOUNT&units=metric&appid=$FORECASTAPI")
                        .readText(Charsets.UTF_8)
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {

                val jsonObj = JSONObject(result)
                val list = jsonObj.getJSONArray("list").getJSONObject(0)

                val jsonArray = jsonObj.getJSONArray("list")
                length = jsonArray.length()
                //length = arrayLength


                //List related stuff
                var listView = findViewById<ListView>(R.id.forecastListView)
                //Creating a Seperate list view variable
                var listForecast = mutableListOf<forecastModel>()

                for (i in 0 until length) {

                    /* Extracting data from API result and Populating extracted data into list */
                    val list2 = jsonObj.getJSONArray("list").getJSONObject(arrayNum)
                    val main = list2.getJSONObject("main")
                    val temp = main.getInt("temp")
                    temperature = temp.toString() + "°C"
                    val weather = list2.getJSONArray("weather").getJSONObject(0)
                    val weatherDescription = weather.getString("description")
                    forecastDescription = weatherDescription
                    val weatherDate: Long = list2.getLong("dt")
                    val forecastDateTxt = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(weatherDate * 1000))
                    forecastDate = forecastDateTxt

                    if (forecastDescription == "light rain") {
                        forecastImage = R.drawable.ic_rainy_weater
                    } else if (forecastDescription == "moderate rain") {
                        forecastImage = R.drawable.ic_moderate_rain_weather
                    } else {
                        forecastImage = R.drawable.ic_sunny_weather
                    }

                    listForecast.add(forecastModel(temperature, forecastDescription, forecastDate, forecastImage))

                    arrayNum += 1

                }

                listView.adapter = forecastModelAdapter(this@WeatherForecastViewActivity, R.layout.forecast_row, listForecast)

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.forecastLoader).visibility = View.GONE

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.forecastLoader).visibility = View.GONE
                findViewById<TextView>(R.id.forecastErrorText).visibility = View.VISIBLE
            }
        }
    }

    private fun setNewCity()
    {
        val cityText=findViewById<EditText>(R.id.enterCityTxt)
        val enteredCity = cityText.text.toString()
        FORECASTCITY = enteredCity

    }
}
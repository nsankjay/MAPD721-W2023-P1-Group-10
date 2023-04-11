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

    var FORECASTCITY: String = "Vancouver,CA"
    val FORECASTAPI: String = "3b57b3d787ba93b37afb428cb50d2cdd"

    private lateinit var getForcastBtn: Button
    private lateinit var goToMainWeatherViewBtn: Button

    private lateinit var frcstLocationTv: TextView
    private lateinit var frcstDateTv: TextView
    private lateinit var frcstTempTv: TextView
    private lateinit var frcstDescriptionTv: TextView
    private lateinit var frcstAddressTv: TextView
    private lateinit var setFrcstCityTxt: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast_view)

        //Assigning current city for display and global variable
        var FRCSTCITY = intent.getStringExtra("EXTRA_FORECASTCITY")
        frcstLocationTv = findViewById(R.id.forecastLocationTv) //This connects the declared text view to the xml text view
        val forecastLocationDisplay = frcstLocationTv
        //forecastLocationDisplay.text = FRCSTCITY
        FORECASTCITY = FRCSTCITY.toString()
        forecastLocationDisplay.text = FORECASTCITY

        //Setting the Edit text with city value
        setFrcstCityTxt = findViewById<EditText>(R.id.enterCityTxt)
        setFrcstCityTxt.setText(FORECASTCITY)


        goToMainWeatherViewBtn = findViewById(R.id.goToMainWeatherViewBtn)

        goToMainWeatherViewBtn.setOnClickListener {
            Intent(this, MainWeatherViewActivity::class.java).also {

                startActivity(it)
            }
        }

        getForcastBtn = findViewById(R.id.getForecastBtn)

        getForcastBtn.setOnClickListener {
            forecastWeatherTask().execute()
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
                    URL("https://api.openweathermap.org/data/2.5/forecast?q=$FORECASTCITY&cnt=5&appid=$FORECASTAPI")
                        .readText(Charsets.UTF_8)
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
//                val jsonObj = JSONObject(result)
//                val main = jsonObj.getJSONObject("main")
//                val sys = jsonObj.getJSONObject("sys")
//                val wind = jsonObj.getJSONObject("wind")
//                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
//                val updatedAt: Long = jsonObj.getLong("dt")
//                val updatedAtText =
//                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
//                        Date(updatedAt * 1000)
//                    )
//                val temp = main.getInt("temp")
//                //val temp = main.getInt("temp") + "°C"
//                val tempMin = "Min Temp: " + main.getInt("temp_min")
//                val tempMax = "Max Temp: " + main.getInt("temp_max")
//                //val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
//                val pressure = main.getString("pressure")
//                val humidity = main.getString("humidity")
//                val sunrise: Long = sys.getLong("sunrise")
//                val sunset: Long = sys.getLong("sunset")
//                val windSpeed = wind.getString("speed")
//                val weatherDescription = weather.getString("description")
//                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                val jsonObj = JSONObject(result)
                val list = jsonObj.getJSONArray("list").getJSONObject(0)
                val visibility = list.getString("visibility")

                val jsonArray = jsonObj.getJSONArray("list")
                val arrayLength = jsonArray.length()


                findViewById<TextView>(R.id.forecastDateTv).text = arrayLength.toString()

                findViewById<TextView>(R.id.forecastAddressTv).text = visibility



                /* Populating extracted data into our views */
//                findViewById<TextView>(R.id.address).text = address
//                findViewById<TextView>(R.id.updated_at).text = updatedAtText
//                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
//                findViewById<TextView>(R.id.temp).text =
//                    temp.toString() + "°C"
//                findViewById<TextView>(R.id.temp_min).text =
//                    tempMin.toString() + "°C"
//                findViewById<TextView>(R.id.temp_max).text =
//                    tempMax.toString() + "°C"
//                findViewById<TextView>(R.id.sunrise).text =
//                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
//                findViewById<TextView>(R.id.sunset).text =
//                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
//                findViewById<TextView>(R.id.wind).text = windSpeed
//                findViewById<TextView>(R.id.pressure).text = pressure
//                findViewById<TextView>(R.id.humidity).text = humidity

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.forecastLoader).visibility = View.GONE
            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.forecastLoader).visibility = View.GONE
                findViewById<TextView>(R.id.forecastErrorText).visibility = View.VISIBLE
            }
        }
    }
}
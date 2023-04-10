package com.sn.mapd721_w2023_p1_group_10

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationRequest
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.google.android.gms.location.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainWeatherViewActivity : AppCompatActivity() {

    var CITY: String = "Vancouver,CA"
    val API: String = "3b57b3d787ba93b37afb428cb50d2cdd"

    //Connecting UI objects for Current location display
//    var lattitudeText = findViewById<TextView>(R.id.lattitudeTxt)
//    var longitudeText = findViewById<TextView>(R.id.longitudeTxt)
//    var countryText = findViewById<TextView>(R.id.countryNameTxt)
//    var localityText = findViewById<TextView>(R.id.localityTxt)
//    var addressText = findViewById<TextView>(R.id.addressTxt)
//    var getLocationBtn = findViewById<ImageButton>(R.id.getPosGetCurrentLocationBtn)

    private lateinit var lattitudeText: TextView
    private lateinit var longitudeText: TextView
    private lateinit var countryText: TextView
    private lateinit var localityText: TextView
    private lateinit var addressText: TextView
    private lateinit var getLocationBtn: ImageButton

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    //private val permissionId = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_weather_view)

        lattitudeText = findViewById(R.id.lattitudeTxt)
        longitudeText = findViewById(R.id.longitudeTxt)
        countryText = findViewById(R.id.countryNameTxt)
        localityText = findViewById(R.id.localityTxt)
        addressText = findViewById(R.id.addressTxt)
        getLocationBtn = findViewById(R.id.getPosGetCurrentLocationBtn)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        weatherTask().execute()

        getLocationBtn.setOnClickListener {
            checkPermissions()
            weatherTask().execute()
        }


    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainWeatherInfoContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE

        }

        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
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
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000)
                    )
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                /* Populating extracted data into our views */
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text =
                    temp.substring(0, Math.min(temp.length, 2)) + "°C"
                findViewById<TextView>(R.id.temp_min).text =
                    tempMin.substring(0, Math.min(tempMin.length, 12)) + "°C"
                findViewById<TextView>(R.id.temp_max).text =
                    tempMax.substring(0, Math.min(tempMax.length, 12)) + "°C"
                findViewById<TextView>(R.id.sunrise).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                findViewById<TextView>(R.id.sunset).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainWeatherInfoContainer).visibility =
                    View.VISIBLE
            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }

    private fun checkPermissions()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            getLocations()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocations() {
        mFusedLocationClient.lastLocation?.addOnSuccessListener {
            if(it == null) {
                Toast.makeText(this, "Sorry Can't get Location", Toast.LENGTH_SHORT).show()
            } else it.apply{
                val lattitude = it.latitude
                val longitude = it.longitude
                var manuallattitude = 43.7184038
                var manualongitude = -79.5181405
                var cityName:String = ""
                var countryName = ""
                var geocoder = Geocoder(this@MainWeatherViewActivity, Locale.getDefault())
                var cityAddress = geocoder.getFromLocation(lattitude,longitude,3)

                if (cityAddress != null) {
                    cityName = cityAddress.get(0).locality
                    CITY = cityName
                }
                if (cityAddress != null) {
                    countryName = cityAddress.get(0).countryName
                }

                addressText.text = "Latitude: $manuallattitude, Longitude: $manualongitude, City: $cityName, Country: $countryName"
            }
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    getLocations()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}
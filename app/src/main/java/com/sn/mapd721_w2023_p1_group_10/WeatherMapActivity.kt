package com.sn.mapd721_w2023_p1_group_10

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import java.net.HttpURLConnection
import java.net.URL

class WeatherMapActivity : AppCompatActivity() {

    var MAPCITY: String = "Vancouver,CA"
    val MAPAPI: String = "3b57b3d787ba93b37afb428cb50d2cdd"
    var MAPLAYER: String = "temp_new"

    private lateinit var getNewLayerBtn: Button
    private lateinit var MapGoBackToMainWeatherViewBtn: Button

    private lateinit var setMapLayerTxt: EditText
    private lateinit var mapImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_map)


        getNewLayerBtn = findViewById(R.id.mapChangeLayerBtn)

        getNewLayerBtn.setOnClickListener {

            setNewLayer()
            Toast.makeText(this, "Set Layer: $MAPLAYER", Toast.LENGTH_SHORT).show()
            setMap()

        }

        MapGoBackToMainWeatherViewBtn = findViewById(R.id.mapViewGoBackToWeatherViewBtn)

        MapGoBackToMainWeatherViewBtn.setOnClickListener {
            Intent(this, MainWeatherViewActivity::class.java).also {

                startActivity(it)
            }
        }
    }

    private fun setNewLayer()
    {
        val layerText=findViewById<EditText>(R.id.enterMapLayerTxt)
        val enteredLayer = layerText.text.toString()
        MAPLAYER = enteredLayer

    }

    private fun setMap()
    {
        mapImageView = findViewById<ImageView?>(R.id.displayMapimageView)
//        val url = "https://tile.openweathermap.org/map/$MAPLAYER/0/0/0.png?appid=$MAPAPI"
//        val connection = URL(url).openConnection() as HttpURLConnection
//        connection.doInput = true
//        connection.connect()
//        val input = connection.inputStream
//        val bitmap = BitmapFactory.decodeStream(input)
//        mapImageView.setImageBitmap(bitmap)
        Picasso.get()
            .load("https://tile.openweathermap.org/map/$MAPLAYER/0/0/0.png?appid=$MAPAPI")
            .into(mapImageView)
        mapImageView.visibility = View.VISIBLE
    }
}
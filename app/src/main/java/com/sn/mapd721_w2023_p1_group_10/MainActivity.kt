package com.sn.mapd721_w2023_p1_group_10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val editBtn=findViewById<Button>(R.id.goToWeatherViewBtn)

        //Click Edit Button action - go to edit user account details page
        editBtn.setOnClickListener {
            Intent(this, MainWeatherViewActivity::class.java).also {

                startActivity(it)
            }

        }

    }

}
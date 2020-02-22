package com.SDP.signbits

import android.content.Context
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import java.io.*


class MainActivity : AppCompatActivity() {

    companion object{
        val alphabet = 'A'..'Z'
        var learningProgress : HashMap<String, Int> = hashMapOf(
            "learning progress" to 0,
            "CharToFin quiz" to 0,
            "FinToChar quiz" to 0
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_learn, R.id.navigation_quiz, R.id.navigation_settings
            )
        )

        val file = File("learningProgress.txt")
        if (file.exists()){
            val input : InputStream = openFileInput(file.readText())
            val bufferedReader = input.bufferedReader()
            val stringBuilder = StringBuilder()
            try{
                var line = bufferedReader.readLine()
                while (line != null){
                    
                }

            } catch (e: IOException){
                Log.d("IOError", e.message)
            }
            if (input!=null){

            }
            val reader = JsonReader(InputStreamReader(input))
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }


}

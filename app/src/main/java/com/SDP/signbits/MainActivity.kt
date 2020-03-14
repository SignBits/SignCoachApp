package com.SDP.signbits

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.JsonReader
import android.util.Log
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.SDP.signbits.ui.learn.LearnFragment
import java.io.*


class MainActivity : AppCompatActivity() {

    companion object{
        val alphabet = "ABCDEFGJK"
    }
    private var fragmentName : String? = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_learn, R.id.navigation_home, R.id.navigation_quiz, R.id.navigation_settings
            )
        )
        if (savedInstanceState != null){
            fragmentName = savedInstanceState.getString("Fragment")
            navController.restoreState(savedInstanceState.getBundle("Nav"))
            Log.d("SaveState","Restored state")
        }
        val pref : SharedPreferences = applicationContext.getSharedPreferences("LearningProgress",0)
        val editor = pref.edit()

        if (pref.getInt("Learning", -1) == -1)
            editor.putInt("Learning", 0)
        if (pref.getInt("F2CNumber", -1) == -1)
            editor.putInt("F2CNumber", 0)
        if (pref.getInt("F2Correct", -1) == -1)
            editor.putInt("F2CCorrect", 0)
        if (pref.getInt("C2FNumber", -1) == -1)
            editor.putInt("C2FNumber", 0)
        if (pref.getInt("C2FCorrect", -1) == -1)
            editor.putInt("C2FCorrect", 0)
        if (pref.getString("LearnSequence", "").equals(""))
            editor.putString("LearnSequence","A")
        editor.apply()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }


}

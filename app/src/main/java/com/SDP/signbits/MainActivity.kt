package com.SDP.signbits

import android.os.Bundle
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

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

        val dictionaryScrollView: LinearLayout = findViewById(R.id.dictionaryScrollView)

        populateDictionary(dictionaryScrollView)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun populateDictionary(linearLayout: LinearLayout){
        val alphabet = 'A'..'Z'

        alphabet.forEach {
            val btnTag = Button(this)
            btnTag.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            btnTag.textSize = 32.0f
            btnTag.textAlignment = TEXT_ALIGNMENT_CENTER
            btnTag.text = it.toString()
            btnTag.id = it.toInt()
            btnTag.setOnClickListener { _ -> sendFingerspellRequest(it)}
            linearLayout.addView(btnTag)
        }
    }

    private fun sendFingerspellRequest(char: Char){
        val fingerspellEndpoint = "http://pikachu:5000/api/fingerspell/"

        val params: HashMap<String, String> = hashMapOf(
            "characterSequence" to char.toString()
        )

        val jsonParams = JSONObject(params.toMap())

        val request = object: JsonObjectRequest(
            POST,
            fingerspellEndpoint,
            jsonParams,
            Response.Listener { response ->
                // Process the json
                try {
                    println("Response: $response")
                }catch (e:Exception){
                    println("Exception: $e")
                }

            }, Response.ErrorListener{
                // Error in request
                println("Volley error: $it")
            }){

            override fun getHeaders(): HashMap<String, String>{
                return hashMapOf("Content-Type" to "application/json")
            }
        }

        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }


}

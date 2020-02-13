package com.SDP.signbits.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.SDP.signbits.R
import com.SDP.signbits.VolleySingleton
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        val dictionaryScrollView: LinearLayout = root.findViewById(R.id.dictionaryScrollView)

        populateDictionary(dictionaryScrollView)
        return root
    }


    private fun populateDictionary(linearLayout: LinearLayout){
        val alphabet = 'A'..'Z'

        alphabet.forEach {
            val btnTag = Button(activity)
            btnTag.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            btnTag.textSize = 32.0f
            btnTag.textAlignment = View.TEXT_ALIGNMENT_CENTER
            btnTag.text = it.toString()
            btnTag.id = it.toInt()
            btnTag.setOnClickListener { _ -> sendFingerspellRequest(it)}
            linearLayout.addView(btnTag)
        }
    }

    private fun sendFingerspellRequest(char: Char){
        val fingerspellEndpoint = "http://192.168.105.150:5000/api/fingerspell/"

        val params: HashMap<String, String> = hashMapOf(
            "characterSequence" to char.toString()
        )

        val jsonParams = JSONObject(params.toMap())

        val request = object: JsonObjectRequest(
            Method.POST,
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

        VolleySingleton.getInstance(this.requireContext()).addToRequestQueue(request)
    }
}
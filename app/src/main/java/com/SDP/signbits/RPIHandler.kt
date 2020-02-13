package com.SDP.signbits

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

/**
 * This is the singleton class to complete the task of communication with RPi.
 *
 * @param context The only constructor param is context of the handler.
 *
 */

class RPIHandler constructor(private val context: Context) {

    /**
     * This is the variable of the endpoint of the device. in the format
     * of "http://{ip-address}:{port}". By default it is the testing ip of our pikachu!
     */
    private var endPoint: String = "http://192.168.105.150:5000"
    /**
     * This is the getInstance method of the singleton class.
     */
    companion object {
        @Volatile
        private var INSTANCE: RPIHandler? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RPIHandler(context).also { INSTANCE = it }
            }
    }


    /**
     * This methods send finger spell request to the RPI.
     *
     * @param charSequence This param is the character sequence to send to Pi. Might be character
     * or string.
     * @param context This is the context the class is in. In Fragment, it is the "activity"
     * while in an activity it is "this".
     */
    fun sendFingerSpellRequest(charSequence: CharSequence){
        val fingerspellEndpoint = endPoint + "/api/fingerspell/"

        val params: HashMap<String, String> = hashMapOf(
            "characterSequence" to charSequence.toString()
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

        VolleySingleton.getInstance(this.context).addToRequestQueue(request)
    }

    /**
     * This method should search all the devices available over Wifi.
     */
    fun searchDeviceOverWifi(){
        throw NotImplementedError("Have not implemented this method!")
    }

    /**
     * This method connect to the device the user choose and change the endpoint of the class.
     */
    fun connectToDevice(deviceHostName : String){
        throw NotImplementedError("Have not implemented this method!")
    }
}
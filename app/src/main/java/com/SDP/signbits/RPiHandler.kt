package com.SDP.signbits

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

/**
 * This is the singleton class to complete the task of communication with RPi.
 *
 * This class should be created only when an RPi ip address is known.
 * This class is used to interface with a RPi, once a connection has already been established.
 *
 * It owns all possible ways of communicating with the pi, once a connection has been established.
 *
 * @param context The only constructor param is context of the handler.
 *
 */

class RPiHandler constructor(private val context: Context) {

    /**
     * This is the variable of the endpoint of the device. in the format
     * of "http://{ip-address}:{port}". By default it is the testing ip of our pikachu!
     */
    var endPoint: String = "http://192.168.105.150:5000"
    /**
     * This is the getInstance method of the singleton class.
     */
    companion object {
        @Volatile
        private var INSTANCE: RPiHandler? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RPiHandler(context).also { INSTANCE = it }
            }
    }

    /**
     * This methods send finger spell request to the RPI.
     *
     * @param charSequence This param is the character sequence to send to Pi. Might be character
     * or string.
     */
    fun postFingerSpellRequest(charSequence: CharSequence){
        val fingerspellEndpoint = endPoint + "/api/fingerspell/"

        val params: HashMap<String, String> = hashMapOf(
            "characterSequence" to charSequence.toString()
        )
        sendPostRequest(fingerspellEndpoint, params)
    }

    /**
     * This method gets basic device info.
     * Need to implement with a get request here.
     */
    fun getDeviceInfo(){
        throw NotImplementedError("Have not implemented this method!")
    }

    /**
     * This method updates the basic information of the device, if allowed to.
     * Need to implement with a put request here.
     */
    fun updateDeviceInfo(){
        throw NotImplementedError("Have not implemented this method!")
    }

    /**
     * We might also want to use usb to connect to RPi initially to tell RPi the WiFi info and
     * also its MAC address.
     */
    fun connectToRPiViaUsb(){
        throw NotImplementedError("Have not implemented this method!")
    }

    private fun <T> sendPostRequest(endpoint: String, params: HashMap<T, T>){

        val jsonParams = JSONObject(params.toMap())

        val request = object: JsonObjectRequest(
            Method.POST,
            endpoint,
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

}
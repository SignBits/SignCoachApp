package com.SDP.signbits

import androidx.core.content.ContextCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.AsyncTask
import android.os.Handler
import android.text.PrecomputedText
import android.util.Log
import androidx.core.app.ActivityCompat
import java.net.*
import java.nio.Buffer

/**
 * This is the singleton class to complete the task of communication with RPi.
 *
 * This class should be created only when an RPi ip address is known.
 * This class is used to interface with a RPi, once a connection has already been established.
 *
 * It owns all possible ways of communicating with the pi, once a connection has been established.
 *
 * @param activity The only constructor param is context of the handler.
 *
 */

class RPiHandler constructor(private val activity: Activity) {

    /**
     * This is the variable of the endpoint of the device. in the format
     * of "http://{ip-address}:{port}". By default it is the testing ip of our pikachu!"http://192.168.105.150:5000"
     */
    var endPoint: String? = null
    var MY_INTERNET_PERMISSION : Int = 0
    var MY_WIFI_PERMISSION : Int = 0

    /**
     * This is the getInstance method of the singleton class.
     */
    companion object {
        @Volatile
        private var INSTANCE: RPiHandler? = null
        var name : UDPPacket? = null
        fun getInstance(context: Activity) =
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
    fun postFingerSpellRequest(charSequence: CharSequence) : Boolean{
        if (endPoint == null) {
            createDialog("Please connect to your robot first!")
            return false
        }
        val fingerspellEndpoint = endPoint + "/api/fingerspell/"

        val params: HashMap<String, String> = hashMapOf(
            "characterSequence" to charSequence.toString()
        )
        sendPostRequest(fingerspellEndpoint, params)
        return true
    }

    fun searchLAN() {
        val thread = Thread(ReceiveUDP())
        thread.start()
        thread.join(4000)
    }


    private fun <T> sendPostRequest(endpoint: String, params: HashMap<T, T>) {
        val internet = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED
        val wifi = ContextCompat.checkSelfPermission(activity, Manifest.permission
            .CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
        if (internet) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.INTERNET),
                MY_INTERNET_PERMISSION)
        } else if (wifi){
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission
                .CHANGE_WIFI_STATE), MY_WIFI_PERMISSION)
        } else {

            val jsonParams = JSONObject(params.toMap())

            val request = object : JsonObjectRequest(
                Method.POST,
                endpoint,
                jsonParams,
                Response.Listener { response ->
                    // Process the json
                    try {
                        println("Response: $response")
                    } catch (e: Exception) {
                        println("Exception: $e")
                    }

                }, Response.ErrorListener {
                    // Error in request
                    println("Volley error: $it")
                }) {

                override fun getHeaders(): HashMap<String, String> {
                    return hashMapOf("Content-Type" to "application/json")
                }
            }

            // Volley request policy, only one time request to avoid duplicate transaction
            request.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, // 0 means no retry
                0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            VolleySingleton.getInstance(this.activity).addToRequestQueue(request)
        }
    }

    private fun createDialog(msgP : CharSequence){
        AlertDialog.Builder(activity)
            .setTitle(msgP)
            .setPositiveButton("OK", null)
            .create()
            .show()
    }

}

data class UDPPacket(val ip: String, val port : String, val data: String)

class ReceiveUDP : Runnable{
    override fun run() {
        RPiHandler.name = null
        val mSocket = DatagramSocket(null)
        mSocket.soTimeout = 20000
        mSocket.broadcast = true
        val TAG = "SCAN"
        Log.d(TAG, "Socket Started")
        try {
            mSocket.bind(InetSocketAddress(10000))
            val buffer = ByteArray(32)
            val mPacket = DatagramPacket(buffer, buffer.size)
            mSocket.receive(mPacket)
            val ip = mPacket.address?.toString()
            val port = mPacket.port
            val data = String(mPacket.data, 0, mPacket.length)
            Log.d(TAG, "ip: $ip")
            Log.d(TAG, "port: $port")
            Log.d(TAG, "data: $data")
            RPiHandler.name = ip?.let { UDPPacket(it, port.toString(), data) }
            mSocket.close()
            Log.d(TAG, "FLAGTRUE")
        } catch (e: SocketTimeoutException){
            val a = "TIMEOUT"
            RPiHandler.name = UDPPacket(ip = a, port = a, data = a)
            mSocket.close()
            Log.d(TAG, "FLAGTRUE")
        } catch (e: BindException){
            RPiHandler.name = null
            mSocket.close()
            Log.d(TAG, "FLAGTRUE")
        }

    }

}
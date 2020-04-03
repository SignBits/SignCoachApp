package com.SDP.signbits

import androidx.core.content.ContextCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.trycatch.mysnackbar.Prompt
import com.trycatch.mysnackbar.TSnackbar

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
     * of "http://{ip-address}:{port}". By default it is the testing ip of our pikachu!
     */
    var endPoint: String = "http://192.168.105.150:5000"
    var MY_INTERNET_PERMISSION : Int = 0
    var MY_WIFI_PERMISSION : Int = 0

    /**
     * This is the getInstance method of the singleton class.
     */
    companion object {
        @Volatile
        private var INSTANCE: RPiHandler? = null
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
    fun postFingerSpellRequest(charSequence: CharSequence){
        val fingerspellEndpoint = endPoint + "/api/fingerspell/"

        val params: HashMap<String, String> = hashMapOf(
            "characterSequence" to charSequence.toString()
        )
        sendPostRequest(fingerspellEndpoint, params)
    }

    /**
     * This method
     */
    fun searchLAN(){
        val wifiManager = activity.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    scanSuccess()
                } else {
                    scanFailure()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        activity.registerReceiver(wifiScanReceiver, intentFilter)

        val success = wifiManager.startScan()
        if (!success) {
            // scan failure handling
            scanFailure()
        }
    }

    private fun scanFailure(){
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        val wifiManager = activity.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val results = wifiManager.scanResults
        AlertDialog.Builder(activity)
            .setTitle("Scan Failed!")
            .setPositiveButton("OK", null)
            .create()
            .show()

    }

    private fun scanSuccess(){
        val wifiManager = activity.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val results = wifiManager.scanResults
        createDialogueAndShow(results)
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

    private fun createDialogueAndShow(devices: List<ScanResult>){
        val deviceNames : Array<CharSequence> = devices.map { result -> result.operatorFriendlyName }.toTypedArray()
        AlertDialog.Builder(activity)
            .setTitle("Choose Device")
            .setSingleChoiceItems(deviceNames, 0,
                DialogInterface.OnClickListener { dialog, which ->
                    // The 'which' argument contains the index position
                    // of the selected item
                    val deviceIP = devices[which].BSSID
                    Log.d("deviceIP", deviceIP)
                    endPoint = "http://${deviceIP}:5000"
            })
            .create()
            .show()
    }

}

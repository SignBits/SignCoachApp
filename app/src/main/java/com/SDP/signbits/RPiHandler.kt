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
import android.util.Log
import androidx.core.app.ActivityCompat

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
        // Instantiate a new DiscoveryListener
        val nsdManager = activity.getSystemService(Context.NSD_SERVICE) as NsdManager
        val discoveryListener = object : NsdManager.DiscoveryListener {

            val TAG = "NSD"
            var services : ArrayList<NsdServiceInfo> = ArrayList()

            // Called as soon as service discovery begins.
            override fun onDiscoveryStarted(regType: String) {
                Log.d(TAG, "Service discovery started")
                createDialog("Start Discovery!")
            }

            override fun onServiceFound(service: NsdServiceInfo) {
                // A service was found! Do something with it.
                Log.d(TAG, "Service discovery success$service")
                services.add(service)
            }

            override fun onServiceLost(service: NsdServiceInfo) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(TAG, "service lost: $service")
                createDialog("Network Service Discovery is Lost!")
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.i(TAG, "Discovery stopped: $serviceType")
                scanSuccess(services)
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e(TAG, "Discovery failed: Error code:$errorCode")
                nsdManager.stopServiceDiscovery(this)
                scanFailure()
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e(TAG, "Discovery failed: Error code:$errorCode")
                nsdManager.stopServiceDiscovery(this)
                scanFailure()
            }

        }
        val serviceType = "_http._tcp"
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    private fun scanFailure(){
        createDialog("Scan Fail!")
    }

    private fun createDialog(msg : CharSequence){
        AlertDialog.Builder(activity)
            .setTitle(msg)
            .setPositiveButton("OK", null)
            .create()
            .show()
    }

    private fun scanSuccess(results : List<NsdServiceInfo>){
        if (results.isEmpty())
            AlertDialog.Builder(activity)
                .setTitle("No device found!")
                .setPositiveButton("OK", null)
                .create()
                .show()
        else
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

    private fun createDialogueAndShow(devices: List<NsdServiceInfo>){
        val deviceNames : Array<CharSequence> = devices.map { it.serviceName }.toTypedArray()
        AlertDialog.Builder(activity)
            .setTitle("Choose Device")
            .setSingleChoiceItems(deviceNames, 0,
                DialogInterface.OnClickListener { dialog, which ->
                    // The 'which' argument contains the index position
                    // of the selected item
                    val deviceIP = devices[which].host
                    val devicePort = devices[which].port
                    Log.d("Nsd/deviceIP", deviceIP.toString())
                    Log.d("Nsd/devicePort", deviceIP.toString())
                    endPoint = "http://${deviceIP}:${devicePort}"
            })
            .create()
            .show()
    }

}

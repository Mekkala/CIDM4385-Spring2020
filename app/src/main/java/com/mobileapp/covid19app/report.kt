package com.mobileapp.covid19app

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import kotlinx.android.synthetic.main.fragment_report.*
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [report.newInstance] factory method to
 * create an instance of this fragment.
 */
class report : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    //Google API
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var mapView : MapView
    private lateinit var googleMap : GoogleMap
    private lateinit var uiSettings: UiSettings

    private var mLocationPermissionGranted : Boolean = true
    private var lat : Double = 32.77
    private var lon : Double = -96.78
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION : Int = 1
    private val MAP_VIEW_BUNDLE_KEY : String = "MapViewBundleKey"



    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        mapView = view.findViewById(R.id.mapReport)

        val homeReport: Button? = view?.findViewById(R.id.homeReport)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity?.applicationContext!!)
        geocoder = Geocoder(activity?.applicationContext!!)

        var mapViewBundle : Bundle? = null
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView.onCreate(mapViewBundle)
        //mapView.getMapAsync(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->

                Log.i("DUDE", "LOCATION SERVICE")

                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    lat = location.latitude
                }
                if (location != null) {
                    lon = location.longitude
                }

                //move map
                if(googleMap != null){
                    val loc = LatLng(lat, lon)
                    val cameraUpdate : CameraUpdate = CameraUpdateFactory.newLatLng(loc)
                    googleMap.addMarker(MarkerOptions().position(loc).title("Your Location"))
                    googleMap.animateCamera(cameraUpdate)
                    googleMap.moveCamera(cameraUpdate)
                    Log.i("DUDE", "LAT: ${googleMap.cameraPosition.target.latitude.toString()} " +
                            "LON: ${googleMap.cameraPosition.target.longitude.toString()}")
                } else {
                    Log.i("DUDE", "Google Map not Ready to use")
                }
                

                val list = geocoder.getFromLocation(lat, lon, 1)

                val city = list[0].locality
                val state = list[0].adminArea

                val place = "$city, $state"

                // Instantiate the RequestQueue.
                val queue = Volley.newRequestQueue(activity?.applicationContext)
                val url = "https://api.covid19api.com/live/country/united-states/status/confirmed/date/2020-03-21T13:13:30Z"

                /* Request a string response from the provided URL. */
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener<JSONObject> { response ->

                        val cases = response.getJSONObject("currently").get("Confirmed").toString()
                        textViewCases.text = "Cases: $cases"
                        textViewLocation.text = "Location: $place"

            },

            Response.ErrorListener { error ->
            // TODO: Handle error
            Log.e("DUDE", "ERRORED OUT" + error.message!!)
        }
        )

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
        homeReport?.setOnClickListener { v: View -> onButtonHomeReportClick(v) }
        return view
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            googleMap = map
        }
        googleMap.setMinZoomPreference(8f)
        googleMap.setMaxZoomPreference(14f)
        googleMap.isMyLocationEnabled = true

        //get the UiSettings Property from the Map
        uiSettings = googleMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isScrollGesturesEnabled = true
        uiSettings.isZoomGesturesEnabled = true

    }
    //location Permission
    private fun getLocationPermission() {
        var same : Boolean = android.Manifest.permission.ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED.toString()

        if(same) {
            mLocationPermissionGranted = true
        } else {
            val perms : Array<String> = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            activity?.requestPermissions(perms, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }
    //back to home button
    private fun onButtonHomeReportClick(view: View) {
        view.findNavController().navigate(R.id.reportToMain)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment report.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            report().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}

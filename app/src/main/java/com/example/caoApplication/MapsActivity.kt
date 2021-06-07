package com.example.caoApplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    var size:Int=0
    var timestamp:Long ?=null
    var intent2:Intent ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val my = LatLng(37.55743, 126.92620)
        val my2= LatLng(37.5465753,126.9124594)

        val MyLocation=mMap.addMarker(MarkerOptions().position(my).title("내 위치"))
        MyLocation.showInfoWindow()
        var cafeDTO:ArrayList<CafeDTO> = arrayListOf()


        FirebaseFirestore.getInstance().collection("cafes").orderBy("timestamp").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                cafeDTO.clear()
            if (querySnapshot == null) return@addSnapshotListener
            for (snapshot in querySnapshot!!.documents) {
                var item = snapshot.toObject(CafeDTO::class.java)
                if(item?.Location1!! >=my.latitude-0.01&& item?.Location1!!<=my.latitude+0.01){
                    if(item?.Location2!! >=my.longitude-0.01 && item?.Location2!! <=my.longitude+0.01){
                cafeDTO.add(item!!)}}
            }
            size=cafeDTO.size
            intent2=Intent(this,CafeDetailActivity::class.java)

            for(cafeDTO in cafeDTO.iterator()){
                var marker:Marker?=null
                var location = LatLng(
                    cafeDTO.Location1!!.toDouble(),
                    cafeDTO.Location2!!.toDouble()
                )
                 marker=mMap.addMarker(
                     MarkerOptions().position(location).title(cafeDTO.CafeName!!.toString())
                 )
                timestamp=cafeDTO.timestamp
                marker?.showInfoWindow()
                mMap.setOnInfoWindowClickListener(this)

            }//for
        }//firestore



        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my, 16f))
        mMap.uiSettings.isZoomControlsEnabled=true


       }


    override fun onInfoWindowClick(p0: Marker) {
        var intent:Intent=Intent(this, CafeDetailActivity::class.java)
        intent.putExtra("timestamp",timestamp)
        startActivity(intent)


    }



    override fun onMarkerClick(p0: Marker): Boolean {

        return false
    }




}














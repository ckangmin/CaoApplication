package com.example.caoApplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caoApplication.navigation.MainAdapter

import kotlinx.android.synthetic.main.activity_main.*


//class MainActivity : AppCompatActivity() , OnMapReadyCallback {
class MainActivity : AppCompatActivity(){
//    private lateinit var naverMap: NaverMap
//    private lateinit var locationSource: FusedLocationSource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var recyclerView = findViewById<View>(R.id.main_recyclerview) as RecyclerView
        val layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL,
                false
        )

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter=MainAdapter()




        goaddbtn.setOnClickListener {
            var intent:Intent
            intent=Intent(this, CafeaddActivity::class.java)
            startActivity(intent)
        }


//        locationSource =
//            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
//        val fm = supportFragmentManager
//        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
//            ?: MapFragment.newInstance().also {
//                fm.beginTransaction().add(R.id.map, it).commit()
//            }
//        mapFragment.getMapAsync(this)


    }

//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>,
//                                            grantResults: IntArray) {
//        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
//                grantResults)) {
//            if (!locationSource.isActivated) { // 권한 거부됨
//                naverMap.locationTrackingMode = LocationTrackingMode.None
//            }
//            return
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }


//    override fun onMapReady(naverMap: NaverMap) {
//        this.naverMap=naverMap
//        naverMap.locationSource= locationSource
//        val locationOverlay = naverMap.locationOverlay
//        locationOverlay.isVisible = true
//        val uiSettings = naverMap.uiSettings
//        uiSettings.isLocationButtonEnabled = true
//
//
//    }
//
//    companion object {
//        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
//    }

}

package com.example.caoApplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.caoApplication.navigation.MainAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_cafeadd.*

import kotlinx.android.synthetic.main.activity_main.*


//class MainActivity : AppCompatActivity() , OnMapReadyCallback {
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
//    private lateinit var naverMap: NaverMap
//    private lateinit var locationSource: FusedLocationSource
        var auth:FirebaseAuth?=null

override fun onNavigationItemSelected(p0: MenuItem): Boolean {
    when(p0.itemId){
        R.id.action_home ->{
            var mainAdapter = MainAdapter()
            supportFragmentManager.beginTransaction().replace(R.id.maincontent,mainAdapter).commit()
            return true
        }

    }
    return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        auth= FirebaseAuth.getInstance()
       /* if(auth?.currentUser?.uid=="masteruid"){
            goaddbtn?.visibility= VISIBLE

        }*/

        goaddbtn?.setOnClickListener {
            var intent:Intent
            intent=Intent(this, CafeaddActivity::class.java)
            startActivity(intent)
        }
        bottom_navigation.selectedItemId = R.id.action_home


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

package com.example.caoApplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cafe_detail.*

class CafeDetailActivity : AppCompatActivity() {
    var timestamp:Long ?=null
    var firestore:FirebaseFirestore ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_detail)
        firestore= FirebaseFirestore.getInstance()
        var intent: Intent=getIntent()

        timestamp=intent.getLongExtra("timestamp",0)



            firestore?.collection("cafes")?.whereEqualTo("timestamp",timestamp)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(CafeDTO::class.java)
                   var cafeDTO:CafeDTO ?=item
                    cafeDatail_name.text=cafeDTO?.CafeName
                    cafeDatail_num.text=cafeDTO?.CafePnum
                    detaildesk.text=cafeDTO?.CafeDesk
                    detailplug.text=cafeDTO?.Cafepower
                    detailroom.text=cafeDTO?.Cafespace
                    Glide.with(this).load(cafeDTO?.Cafeimg).into(cafeDatail_img)
                }
            }// timestamp Intent 로 받아서 데이터 가져와 데이터 입력












    }
}







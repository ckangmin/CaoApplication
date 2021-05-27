package com.example.caoApplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cafe_detail.*
import kotlinx.android.synthetic.main.activity_reviewadd.*

class ReviewaddActivity : AppCompatActivity() {
    var timestamp:Long ?=null
    var cafeimg:String?=null
    var firestore: FirebaseFirestore?=null
    var auth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviewadd)
        auth = FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        var intent: Intent =getIntent()

        timestamp=intent.getLongExtra("timestamp",0)
        cafeimg=intent.getStringExtra("img")
        firestore?.collection("cafes")?.whereEqualTo("timestamp",timestamp)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (querySnapshot == null) return@addSnapshotListener
            for (snapshot in querySnapshot!!.documents) {
                var item = snapshot.toObject(CafeDTO::class.java)
                var cafeDTO: CafeDTO? = item
                review_CafeName.text=cafeDTO?.CafeName
                Glide.with(this).load(cafeDTO?.Cafeimg).into(review_img)
                review_Addbtn.setOnClickListener {
                    var reviewDTO= ReviewDTO()
                    reviewDTO?.recafeName = cafeDTO?.CafeName
                    reviewDTO?.timestamp = System.currentTimeMillis()
                    reviewDTO?.reviewText = review_text.text.toString()
                    reviewDTO?.uid = auth?.currentUser?.uid
                    reviewDTO?.Email=FirebaseAuth.getInstance().currentUser?.email
                    reviewDTO?.cafeImg=cafeimg
                    firestore?.collection("reviews")?.document()?.set(reviewDTO)
                    Toast.makeText(this,"성공적으로 리뷰등록이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                    finish()

                    }// firestore 에 업로

                }//cafe 정보를 가져옴


            }// addSnapshotListener
        }//onCreate
    }//Activity

package com.example.caoApplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cafe_detail.*
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {
    var CafeName: String? = null
    var chatRoomUid: String ?=null
    var cafeImg: String? = null
    var timestamp:Long?= null
    var firestore: FirebaseFirestore? = null
    var destinationuid:String ?=null
    var uid:String =" "
   // var chatTime:Long ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        var intent: Intent = getIntent()
        firestore = FirebaseFirestore.getInstance()
        timestamp=intent.getLongExtra("timestamp", 0)
        CafeName= intent.getStringExtra("cafeName")
        cafeImg = intent.getStringExtra("img")
        destinationuid=intent.getStringExtra("destinationUid")
        uid = FirebaseAuth.getInstance().currentUser.uid
        //chatTime=System.currentTimeMillis()
        Glide.with(this).load(cafeImg).apply(RequestOptions().circleCrop()).into(chat_img)
        chat_cafename.text=CafeName
        // 카페이미지와 카페이름 인텐트로 받아와서 출력


        add_text.setOnClickListener {
            var chatDTO = ChatDTO()
            chatDTO?.users?.put(uid, true)
            chatDTO?.users?.put(destinationuid, true)

            if(chatRoomUid==null){

            FirebaseFirestore.getInstance().collection("chatrooms").document()?.set(chatDTO)}
            else{
                Toast.makeText(this,chatRoomUid,Toast.LENGTH_SHORT).show()
            }


        }

       checkchatroom()


    }
    fun checkchatroom(){
        FirebaseFirestore.getInstance().collection("chatrooms").whereEqualTo(uid,true).
        addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (querySnapshot == null) return@addSnapshotListener
            for (snapshot in querySnapshot!!.documents) {
                var check:ChatDTO?=snapshot.toObject(ChatDTO::class.java)

                if(check?.users!!.containsKey(destinationuid)){
                    chatRoomUid= "hi"
                }
            }
        }

    }

}
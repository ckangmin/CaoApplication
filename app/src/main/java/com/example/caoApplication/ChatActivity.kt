package com.example.caoApplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cafe_detail.*
import kotlinx.android.synthetic.main.activity_cafeadd.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.view.*
import kotlinx.android.synthetic.main.chat_item.view.*


class ChatActivity : AppCompatActivity() {
    var CafeName: String? = null
    var chatRoomUid: String? = null
    var cafeImg: String? = null
    var timestamp: Long? = null
    var firestore: FirebaseFirestore? = null
    var destinationuid: String? = null
    var uid: String = " "

    // var chatTime:Long ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        var intent: Intent = getIntent()
        firestore = FirebaseFirestore.getInstance()
        timestamp = intent.getLongExtra("timestamp", 0)
        CafeName = intent.getStringExtra("cafeName")
        cafeImg = intent.getStringExtra("img")
        destinationuid = intent.getStringExtra("destinationUid")
        uid = FirebaseAuth.getInstance().currentUser.uid
        //chatTime=System.currentTimeMillis()
        Glide.with(this).load(cafeImg).apply(RequestOptions().circleCrop()).into(chat_img)
        chat_cafename.text = CafeName
        // 카페이미지와 카페이름 인텐트로 받아와서 출력


        add_text.setOnClickListener {
            var chatDTO = ChatDTO()
            chatDTO?.users?.put(uid, true)
            chatDTO?.users?.put(destinationuid, true)
            var roomDTO= RoomDTO()
            roomDTO?.cafeImg=cafeImg
            roomDTO?.cafename=CafeName
            roomDTO?.destinationUid=destinationuid
            roomDTO?.uid=uid


            if (chatRoomUid == null) {

               FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatDTO).addOnSuccessListener {
                   checkchatroom()
                   }

                FirebaseFirestore.getInstance().collection("chatrooms").document()?.set(roomDTO)
            } else {
                var message=ChatDTO.Comment()
                message.message=chat_text.text.toString()
                message.uid=uid

                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid!!).child(
                        "comments"
                ).push()
                    .setValue(message)

            }
            chat_text.setText("")

        }


        checkchatroom()

    }
    fun checkchatroom(){
        uid = FirebaseAuth.getInstance().currentUser.uid
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/" + uid)
                .equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot.children) {
                        var chatDTO: ChatDTO? = snapshot.getValue(ChatDTO::class.java)
                        if (chatDTO?.users!!.containsKey(destinationuid)) {
                            chatRoomUid = snapshot.key
                            chat_recyclerview.layoutManager = LinearLayoutManager(applicationContext)
                            chat_recyclerview.adapter = ChatAdapter()

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

    }//checkchatrooms
    inner class ChatAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var commentsList:ArrayList<ChatDTO.Comment> = arrayListOf()
        init{
            FirebaseDatabase.getInstance().reference.child("chatrooms").child(chatRoomUid!!)
                .child("comments").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            commentsList.clear()
                            for (item in dataSnapshot.children) {
                                commentsList.add(item.getValue(ChatDTO.Comment::class.java)!!)
                            }
                            notifyDataSetChanged()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
        }//init
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            var view= LayoutInflater.from(p0.context).inflate(R.layout.chat_item, p0, false)
            return ChatViewHolder(view)
        }//onCreateViewHolder
        inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            var viewHolder=(p0 as ChatViewHolder).itemView
            if(commentsList.get(p1).uid.equals(uid)){
                viewHolder.chat_message.text=commentsList.get(p1).message
               // viewHolder.chat_message.setBackgroundResource(R.drawable.bubble)

                viewHolder.itemlinear.setGravity(Gravity.RIGHT)

                viewHolder.chat_message.setBackgroundColor(Color.parseColor("#FFE2D5FA"))
            }
            else{
                viewHolder.chat_message.text=commentsList.get(p1).message
                viewHolder.chat_message.setBackgroundColor(Color.parseColor("#FBD665"))

                //viewHolder.chat_message.setBackgroundResource(R.drawable.bubble)
            }

        }//onBindViewHolder

        override fun getItemCount(): Int {
           return commentsList.size
        }//getItemCount

    }


}
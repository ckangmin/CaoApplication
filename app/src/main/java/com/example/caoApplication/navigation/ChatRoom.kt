package com.example.caoApplication.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caoApplication.ChatActivity
import com.example.caoApplication.R
import com.example.caoApplication.ReviewDTO
import com.example.caoApplication.RoomDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.chatroom.view.*
import kotlinx.android.synthetic.main.chatroom_item.view.*
import kotlinx.android.synthetic.main.mypage_item.view.*

class ChatRoom:Fragment() {

    var uid: String = " "
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
    : View? {
        uid=FirebaseAuth.getInstance().currentUser.uid

        var view=LayoutInflater.from(activity).inflate(R.layout.chatroom,container,false)
        view.chatroom_recyclerview.adapter=RoomAdapter()
        view.chatroom_recyclerview.layoutManager=LinearLayoutManager(activity)

        return view
    }
    inner class RoomAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var roomDTO:ArrayList<RoomDTO> = arrayListOf()
        init{
            uid=FirebaseAuth.getInstance().currentUser.uid
            FirebaseFirestore.getInstance().collection("chatrooms")
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        roomDTO.clear()
                        if (querySnapshot == null) return@addSnapshotListener
                        for (snapshot in querySnapshot!!.documents) {
                            var item = snapshot.toObject(RoomDTO::class.java)
                            if(item?.uid==uid||item?.destinationUid==uid){
                            roomDTO.add(item!!)}
                        }
                        notifyDataSetChanged()
                    }

        }//init

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
           var view=LayoutInflater.from(p0.context).inflate(R.layout.chatroom_item,p0,false)
            return ChatRoomViewholder(view)
        }
        inner class ChatRoomViewholder(view :View):RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            var viewHolder=(p0 as ChatRoomViewholder).itemView
            viewHolder.room_cafeID.text=roomDTO[p1].cafename
            Glide.with(p0.itemView.context).load(roomDTO!![p1].cafeImg).into(viewHolder.room_img)
            viewHolder.setOnClickListener {
                var intent: Intent =Intent(context,ChatActivity::class.java)
                intent.putExtra("destinationUid",roomDTO[p1].destinationUid)
                intent.putExtra("cafeName",roomDTO[p1].cafename)
                intent.putExtra("img",roomDTO[p1].cafeImg)
                startActivity(intent)

            }
        }

        override fun getItemCount(): Int {
            return roomDTO.size
        }
    }

    }






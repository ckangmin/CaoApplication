package com.example.caoApplication.navigation

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.example.caoApplication.CafeDTO
import com.example.caoApplication.R
import com.example.caoApplication.ReviewDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.mainrecycler.view.*
import kotlinx.android.synthetic.main.mypage_item.view.*
import kotlinx.android.synthetic.main.navi_mypage.*
import kotlinx.android.synthetic.main.navi_mypage.view.*
import kotlinx.android.synthetic.main.reviewitem.view.*

class Mypage: Fragment() {
    var firestore:FirebaseFirestore? = null
    var uid: FirebaseAuth?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        uid= FirebaseAuth.getInstance()

        var view=LayoutInflater.from(activity).inflate(R.layout.navi_mypage, container, false)
        view.myID.text=uid?.currentUser?.email
        firestore= FirebaseFirestore.getInstance()
        view.mypage_recyclerview.adapter=MypageAdapter()
        view.mypage_recyclerview.layoutManager=LinearLayoutManager(activity)
        view.mypage_recyclerview.addItemDecoration(DividerItemDecoration(view.context, 1))

        return view

    }
    inner class MypageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var reviewDTO: ArrayList<ReviewDTO> = arrayListOf()
        init{
            var user=uid?.currentUser?.uid
            firestore?.collection("reviews")?.whereEqualTo("uid",user)?.
            addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                reviewDTO.clear()
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(ReviewDTO::class.java)
                    reviewDTO.add(item!!)
                }
                notifyDataSetChanged()
            }

        }
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(p0.context).inflate(R.layout.mypage_item, p0, false)
            return MyViewHolder(view)
        }
        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            var viewholder = (p0 as MyViewHolder).itemView
            viewholder.mypage_cafeID.text=reviewDTO[p1].recafeName
            Glide.with(p0.itemView.context).load(reviewDTO!![p1].cafeImg).into(viewholder.mypage_imgZone)

           }



        override fun getItemCount(): Int {
           return reviewDTO.size
        }


    }
}
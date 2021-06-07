package com.example.caoApplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caoApplication.navigation.ChatRoom
import com.example.caoApplication.navigation.MainAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cafe_detail.*
import kotlinx.android.synthetic.main.reviewitem.view.*

class CafeDetailActivity : AppCompatActivity() {
    var timestamp:Long ?=null
    var firestore:FirebaseFirestore ?=null
    var cafename:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_detail)
        firestore= FirebaseFirestore.getInstance()
        var intent: Intent=getIntent()
        timestamp=intent.getLongExtra("timestamp",0)
        cafename=intent.getStringExtra("cafename")
        cafeDetail_recycler.adapter=ReviewAdapter()
        cafeDetail_recycler.layoutManager=LinearLayoutManager(this)
        cafeDetail_recycler.addItemDecoration(DividerItemDecoration(this, 1))


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
                    Go_Review.setOnClickListener {
                        val intent:Intent=Intent(this,ReviewaddActivity::class.java)
                        intent.putExtra("timestamp",cafeDTO?.timestamp)
                        intent.putExtra("img",cafeDTO?.Cafeimg)
                        startActivity(intent)
                    }
                    Go_Chat.setOnClickListener {
                        var intent:Intent=Intent(this,ChatActivity::class.java)
                        intent.putExtra("timestamp",cafeDTO?.timestamp)
                        intent.putExtra("cafeName",cafeDTO?.CafeName)
                        intent.putExtra("img",cafeDTO?.Cafeimg)
                        intent.putExtra("destinationUid",cafeDTO?.uid)
                        startActivity(intent)

                    }
                }
            }// timestamp Intent 로 받아서 데이터 가져와 데이터 입력





    }
    inner class ReviewAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var reviewDTO: ArrayList<ReviewDTO> = arrayListOf()
        init {
            firestore?.collection("reviews")?.whereEqualTo("recafeName",cafename)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                reviewDTO.clear()
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(ReviewDTO::class.java)
                    reviewDTO.add(item!!)
                }
                notifyDataSetChanged()


            }
        }//init


        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
           var view=LayoutInflater.from(p0.context).inflate(R.layout.reviewitem,p0,false)
            return CustomViewHolder(view)
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            var viewHolder=(p0 as CustomViewHolder).itemView
            viewHolder.review_content.text=reviewDTO!![p1].reviewText
            viewHolder.review_email.text=reviewDTO!![p1].Email

        }

        override fun getItemCount(): Int {
            return reviewDTO.size
        }

    }


}











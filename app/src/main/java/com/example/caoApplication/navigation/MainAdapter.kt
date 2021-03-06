package com.example.caoApplication.navigation


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caoApplication.CafeDTO
import com.example.caoApplication.CafeDetailActivity
import com.example.caoApplication.MapsActivity
import com.example.caoApplication.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cafeadd.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.mainitem.view.*
import kotlinx.android.synthetic.main.mainrecycler.*
import kotlinx.android.synthetic.main.mainrecycler.view.*



class MainAdapter:Fragment() {
    var firestore : FirebaseFirestore? = null
    var uid : String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.mainrecycler, container, false)
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        view.main_recyclerview.adapter = CafeViewRecyclerViewAdapter()
        view.main_recyclerview.layoutManager = LinearLayoutManager(activity)
        view.main_recyclerview.addItemDecoration(DividerItemDecoration(view.context, 1))

        view.goMap_btn.setOnClickListener {
            var intent:Intent=Intent(context,MapsActivity::class.java)
            startActivity(intent)
        }
        return view
    }//onCreateView

    override fun onResume() {
        super.onResume()
        view?.main_recyclerview?.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,true)
        view?.main_recyclerview?.adapter= CafeViewRecyclerViewAdapter()
    }

   inner class CafeViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
       var cafeDTO: ArrayList<CafeDTO> = arrayListOf()
       init {
           firestore?.collection("cafes")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
               cafeDTO.clear()
               if (querySnapshot == null) return@addSnapshotListener
               for (snapshot in querySnapshot!!.documents) {
                   var item = snapshot.toObject(CafeDTO::class.java)
                   cafeDTO.add(item!!)
               }
               notifyDataSetChanged()


           }
       }//init
       override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
           var view = LayoutInflater.from(p0.context).inflate(R.layout.mainitem, p0, false)
           return CustomViewHolder(view)

       }// onCreateViewHolder
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)
       override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
           var viewholder = (p0 as CustomViewHolder).itemView

           //UserId
           viewholder.nameZone.text = cafeDTO!![p1].CafeName

           //Image
           Glide.with(p0.itemView.context).load(cafeDTO!![p1].Cafeimg).into(viewholder.imgZone)

           //Explain of content
           viewholder.pnumZone.text=cafeDTO!![p1].CafePnum


           viewholder.setOnClickListener {
               var intent: Intent =Intent(context,CafeDetailActivity::class.java)
               intent.putExtra("timestamp",cafeDTO!![p1].timestamp)
               intent.putExtra("cafename",cafeDTO!![p1].CafeName)
               startActivity(intent)
           }//imgZone OnClickListener

       }//onBindViewHolder

       override fun getItemCount(): Int {
           return cafeDTO.size
       }
   }//CafeViewRecyclerViewAdapter



}//MainAdapter





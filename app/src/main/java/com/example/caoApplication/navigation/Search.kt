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
import com.example.caoApplication.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.mainitem.view.*
import kotlinx.android.synthetic.main.navi_mypage.view.*
import kotlinx.android.synthetic.main.navi_search.view.*
import kotlinx.android.synthetic.main.searchitem.view.*

class Search: Fragment() {
    var firestore: FirebaseFirestore ?=null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        var view= LayoutInflater.from(activity).inflate(R.layout.navi_search,container,false)
        firestore= FirebaseFirestore.getInstance()

        view.search_recyclerview.layoutManager=LinearLayoutManager(activity)
        view.search_recyclerview.addItemDecoration(DividerItemDecoration(view.context, 1))
        view.search_button.setOnClickListener {
            view.search_recyclerview.adapter=SearchAdapter()
            (view.search_recyclerview.adapter as SearchAdapter).search(view.search_cafe.text.toString())

        }

        return view
    }
    inner class SearchAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var cafeDTO:ArrayList<CafeDTO> = arrayListOf()
        init{
            firestore?.collection("cafes")?.addSnapshotListener {
                querySnapshot, firebaseFirestoreException ->
                cafeDTO.clear()
                for(snapshot in querySnapshot!!.documents){
                    var item= snapshot.toObject(CafeDTO::class.java)
                    cafeDTO.add(item!!)
                }
                notifyDataSetChanged()
            }
        }
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            var view=LayoutInflater.from(p0.context).inflate(R.layout.searchitem,p0,false)
            return SearchViewHolder(view)
        }
        inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            var viewholder = (p0 as SearchViewHolder).itemView
            viewholder.search_name.text=cafeDTO[p1].CafeName
            Glide.with(p0.itemView.context).load(cafeDTO[p1].Cafeimg).into(viewholder.search_img)
            viewholder.setOnClickListener {
                var intent: Intent = Intent(context, CafeDetailActivity::class.java)
                intent.putExtra("timestamp",cafeDTO!![p1].timestamp)
                intent.putExtra("cafename",cafeDTO!![p1].CafeName)
                startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
           return cafeDTO.size
        }
        fun search(word:String){
            firestore?.collection("cafes")?.addSnapshotListener {
                querySnapshot, firebaseFirestoreException ->
                cafeDTO.clear()
                for(snapshot in querySnapshot!!.documents){
                    if(snapshot.getString("cafeName")!!.contains(word)) {
                        var item = snapshot.toObject(CafeDTO::class.java)
                        cafeDTO.add(item!!)
                    }
                }
                notifyDataSetChanged()
            }
        }

    }

}
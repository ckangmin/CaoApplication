package com.example.caoApplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_cafeadd.*
import kotlinx.android.synthetic.main.activity_reviewadd.*
import java.text.SimpleDateFormat
import java.util.*

class CafeaddActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null
    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafeadd)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

        addbtn.setOnClickListener {
            contentUpload()
        }


        gophoto.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }//gophoto

    }//onCreate

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                //This is path to the selected image
                photoUri = data?.data
                photo1.setImageURI(photoUri)

            } else {
                //Exit the addPhotoActivity if you leave the album without selecting it
                finish()

            }
        }
    }//onActivityResult

    fun contentUpload() {
        //Make filename

        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //Promise method
        storageRef?.putFile(photoUri!!)?.continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            var cafeDTO = CafeDTO()
            cafeDTO.CafeName = cafename.text.toString()
            cafeDTO.CafePnum=   cafepnum.text.toString()
            cafeDTO.Cafeimg = uri.toString()
            cafeDTO.Cafepower = cplug.text.toString()
            cafeDTO.CafeDesk = cdesk.text.toString()
            cafeDTO.Cafespace = croom.text.toString()
            cafeDTO.uid = auth?.currentUser?.uid

            cafeDTO.timestamp = System.currentTimeMillis()

            firestore?.collection("cafes")?.document()?.set(cafeDTO)

            setResult(Activity.RESULT_OK)
            Toast.makeText(this,"성공적으로 등록되었습니다.",Toast.LENGTH_SHORT).show()
            var intent:Intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }//ContentUpload
}
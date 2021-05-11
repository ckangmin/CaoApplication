package com.example.caoApplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.caoApplication.navigation.MainAdapter
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var auth: FirebaseAuth?=null
    var googleSignInClient: GoogleSignInClient? =null
    var GOOGLE_LOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth= Firebase.auth
        var gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient= GoogleSignIn.getClient(this,gso)

        loginEmalibtn.setOnClickListener {
            signinAndSignup()
        }
        signgooglebtn.setOnClickListener {
            googleLogin()
        }

    }
    fun googleLogin() {
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }  // google Login
    fun signinAndSignup() {
        auth?.createUserWithEmailAndPassword(textId.text.toString(), textPass.text.toString())?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Creating  a user account
                moveMainpage(task.result?.user)

            } else {
                //Login if you have Account
                signinEmail()
            }
        }
        }// signinAndSignup
    fun signinEmail() {
        auth?.signInWithEmailAndPassword(textId.text.toString(), textPass.text.toString())?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                moveMainpage(task.result?.user)
            //Login
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
            }
        }

    } //signinEmail

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_LOGIN_CODE){
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result.isSuccess){
                var account= result.signInAccount
               firebaseAuthWithGoogle(account)
            }
        }
    }//on ActivityResult
    fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)
                ?.addOnCompleteListener {
                    task ->
                    if(task.isSuccessful){
                        //Login
                        moveMainpage(task.result?.user)
                    }else{
                        //Show the error message
                        Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
                    }
                }
    }//firebaseAuthWithGoogle


    fun moveMainpage(user:FirebaseUser?){
        if(user!=null){
            startActivity(Intent(this, MainActivity::class.java))

        }

    }//moveMainpage


}
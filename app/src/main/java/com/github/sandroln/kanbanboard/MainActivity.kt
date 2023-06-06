package com.github.sandroln.kanbanboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val REQ_ONE_TAP = 2
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, option)

        auth = Firebase.auth

        findViewById<View>(R.id.loginButton).setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, REQ_ONE_TAP)
        }
        if (auth.currentUser != null){
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, REQ_ONE_TAP)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_ONE_TAP) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

                val idToken = account.idToken

                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        //  Log.d(TAG, "Got ID token.")
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnSuccessListener {
                                val user = auth.currentUser
                                updateUI(user)

                                val uid = user!!.uid
                                val email = user.email
                                //todo send to firebase database this info
                                if (it.additionalUserInfo?.isNewUser == true) {
                                    //new user
                                } else {
                                    //old one
                                }
                                startActivity(Intent(this, SecondActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                updateUI(null) // e.g. network error
                            }
                    }

                    else -> {
                        println("error")
                        // Shouldn't happen.
                        // Log.d(TAG, "No ID token!")
                    }
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateUI(user: Any?) {

    }
}
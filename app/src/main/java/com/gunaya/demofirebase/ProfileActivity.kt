package com.gunaya.demofirebase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*

private const val TAG = "TAG_ProfileActivity"
const val EXTRA_CURRENT_USER = "current_user_extra"

class ProfileActivity : AppCompatActivity() {

    private var user: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        user = intent?.extras?.get(EXTRA_CURRENT_USER) as FirebaseUser
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        initViews()
    }

    override fun onStop() {
        super.onStop()
        auth.signOut()
    }

    private fun initViews() {
        initProfileInfo()
        initLogoutButton()
    }

    private fun initProfileInfo() {
        //TODO: Update user info
        val docRef = db.collection("drivers").document(user!!.email!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "USER EMAIL: ${user?.email}")
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    private fun initLogoutButton() {
        profileLogoutButton.setOnClickListener {
            auth.signOut()
            val intent = LoginActivity.getStartIntent(this)
            startActivity(intent)
            finish()
        }
    }
}

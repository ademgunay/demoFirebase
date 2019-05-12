package com.gunaya.demofirebase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class LoginActivity : AppCompatActivity(), KoinComponent {

    private lateinit var auth: FirebaseAuth
    private val networkStateRepository: NetworkStateRepository by inject()

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
    }

    private fun initViews() {
        auth = FirebaseAuth.getInstance()
        loginButton.setOnClickListener {
            if (emailEditText.text.toString().isNotEmpty()
                && passwordEditText.text.toString().isNotEmpty()
            ) {
                if (networkStateRepository.isNetworkAvailable()) {
                    val email: String = emailEditText.text.toString()
                    val password: String = passwordEditText.text.toString()
                    login(email, password)
                } else {
                    showSnackbar(R.string.noInternet)
                }
            } else {
                showSnackbar(R.string.loginFailed)
            }
        }
    }

    private fun login(email: String, password: String) {
        mainProgressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showSnackbar(R.string.loginSuccess)
                    val user = auth.currentUser
                    val intent = ProfileActivity.getStartIntent(this)
                    intent.putExtra(EXTRA_CURRENT_USER, user)
                    startActivity(intent)
                    finish()
                } else {
                    showSnackbar(R.string.loginFailed)
                }
                mainProgressBar.visibility = View.GONE
            }
    }

    private fun showSnackbar(@StringRes stringId: Int) {
        return Snackbar.make(
            findViewById(R.id.loginCoordinatorLayout),
            stringId,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}

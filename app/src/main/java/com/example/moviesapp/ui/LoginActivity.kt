package com.example.moviesapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity: AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.registerTextView.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }
        if(auth.currentUser!=null){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
            intent.putExtra("email_id", auth.currentUser!!.email)
            startActivity(intent)
            finish()
        }
        else{

            binding.loginBtn.setOnClickListener{
                when{
                    TextUtils.isEmpty(binding.loginEmailEditText.text.toString().trim{it <= ' '}) ->{
                        Toast.makeText(
                            this,
                            "Please enter email.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(binding.loginPasswordEditText.text.toString().trim{it <= ' '}) ->{
                        Toast.makeText(
                            this,
                            "Please enter password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else ->{
                        val email: String = binding.loginEmailEditText.text.toString().trim{it <= ' '}
                        val password: String = binding.loginPasswordEditText .text.toString().trim{it <= ' '}

                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    Toast.makeText(
                                        this,
                                        "You are logged in successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", auth.currentUser!!.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Toast.makeText(
                                        this,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                    }

                }
            }
        }

    }
}
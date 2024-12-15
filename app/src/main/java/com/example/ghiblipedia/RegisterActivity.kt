package com.example.ghiblipedia

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ghiblipedia.databinding.ActivityRegisterBinding
import android.widget.Toast
import com.example.ghiblipedia.Model.User.User
import com.example.ghiblipedia.Network.ApiClient
import okhttp3.Callback
import okhttp3.OkHttpClient

class RegisterActivity : AppCompatActivity(){
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        with(binding){
            loginText.setOnClickListener{
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                }
            regButton.setOnClickListener({
                val name = binding.RegisterUsername.text.toString()
                val email = binding.Email.text.toString()
                val password = binding.RegPassword.text.toString()
                val confirmPassword = binding.ConfirmPassword.text.toString()

                if (validateForm(name, email, password, confirmPassword)){

                }

            })

        }
    }

    private fun validateForm(name: String, email: String, password: String, confirmPassword: String): Boolean {
        return if(name.isNotEmpty()&& email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
            true
        } else {
            false
        }
    }

    private fun createUser(user: User){
        val apiService = ApiClient.getInstance()
        val call = apiService.createUser(user)

        call.enqueue(object : Callback<User>{
            
        })

    }

}
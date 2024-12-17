package com.example.ghiblipedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ghiblipedia.Auth.PrefManager.PrefManager
import com.example.ghiblipedia.Model.User.User
import com.example.ghiblipedia.Network.ApiClient
import com.example.ghiblipedia.User.HomeActivity
import com.example.ghiblipedia.databinding.ActivityLoginBinding
import com.example.ghiblipedia.Admin.AdminActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        prefManager = PrefManager.getInstance(this)
        isLogin()
        val client = ApiClient.getInstance()

        setContentView(binding.root)
        enableEdgeToEdge()

        with(binding){
            registerText.setOnClickListener{
                startActivity( Intent(this@LoginActivity, RegisterActivity::class.java))
               }

            loginButton.setOnClickListener {
                loginButton.isEnabled = false // Disable button to prevent duplicate requests

                val response = client.getAllUsers() // Make API call

                response.enqueue(object : retrofit2.Callback<List<User>> {
                    override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                        loginButton.isEnabled = true // Re-enable button

                        if (response.isSuccessful && response.body() != null) {
                            val usernameInput = binding.Username.text.toString()
                            val passwordInput = binding.Password.text.toString()
                            var loginSucess = false

                            response.body()?.forEach{it ->
                                if (it.username == usernameInput && it.password == passwordInput){
                                    loginSucess = true
                                    prefManager.setLoggedIn(true)
                                    prefManager.saveUsername(binding.Username.text.toString())
                                    prefManager.savePassword(binding.Password.text.toString())
                                    prefManager.saveEmail(it.email)
                                    prefManager.saveRole(it.role)
                                    isLogin()
//                                    finish()
                                }

                            }
                            if (!loginSucess) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Invalid username or password. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Log.e("API Error", "Response not successful or body is null")
                            Toast.makeText(
                                this@LoginActivity,
                                "Failed to fetch users: ${response.message()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        loginButton.isEnabled = true // Re-enable button

                        Toast.makeText(
                            this@LoginActivity,
                            "Error occurred: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }


        }
    }
    fun isLogin(){
        if(prefManager.isLoggedIn()){
            if(prefManager.getRole()=="admin"){
                val intentToAdminActivity =Intent(this@LoginActivity, AdminActivity::class.java)
                startActivity(intentToAdminActivity)
            } else if (prefManager.getRole()=="user"){
                val intentToHome =Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intentToHome)
            }
        }
    }
}
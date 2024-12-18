package com.example.ghiblipedia.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ghiblipedia.LoginActivity
import com.example.ghiblipedia.databinding.ActivityRegisterBinding
import com.example.ghiblipedia.Model.User.User
import com.example.ghiblipedia.Network.ApiClient
import retrofit2.Call
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            loginText.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
            regButton.setOnClickListener {
                val name = binding.RegisterUsername.text.toString()
                val email = binding.Email.text.toString()
                val password = binding.RegPassword.text.toString()
                val confirmPassword = binding.ConfirmPassword.text.toString()

                if (validateForm(name, email, password, confirmPassword)) {
                    val apiService = ApiClient.getInstance()
                    val response = apiService.getAllUsers()
                    regButton.isEnabled = false

                    response.enqueue(object : retrofit2.Callback<List<User>> {
                        override fun onResponse(
                            call: Call<List<User>>,
                            response: Response<List<User>>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                val users = response.body()
                                val existingUser = users?.find { it.username == name }
                                val existingEmail = users?.find { it.email == email }
                                if (existingUser != null || existingEmail != null) {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Username or email already exists, please login or register with another name",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val user = User(
                                        id = null,
                                        username = name,
                                        email = email,
                                        password = password,
                                        role = "user"
                                    )
                                    createUser(user)
                                }
                            } else {
                                regButton.isEnabled = true // Re-enable button
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Failed to fetch users",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            regButton.isEnabled = true
                            Toast.makeText(
                                this@RegisterActivity,
                                "Error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
        }
    }

    private fun validateForm(name: String, email: String, password: String, confirmPassword: String): Boolean {
        return if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                true
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                false
            }
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun createUser(user: User) {
        val apiService = ApiClient.getInstance()
        val call = apiService.createUser(user)

        call.enqueue(object : retrofit2.Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration Successful! Please login",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    val errorBody = response.errorBody().toString()
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration failed: ${response.message()}  \nDetails: $errorBody",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Failed to connect: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}

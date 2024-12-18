package com.example.ghiblipedia

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ghiblipedia.Auth.RegisterActivity
import com.example.ghiblipedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        with(binding){
            loginButton.setOnClickListener{
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            registerButton.setOnClickListener{
                startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
            }

        }
    }
}
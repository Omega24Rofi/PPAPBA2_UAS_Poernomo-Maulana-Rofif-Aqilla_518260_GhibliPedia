package com.example.ghiblipedia.User

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment as Frag
import com.example.ghiblipedia.R
import com.example.ghiblipedia.User.Fragment.HomeFragment
import com.example.ghiblipedia.User.Fragment.FavoriteFragment
import com.example.ghiblipedia.Fragment.ProfileFragment
import com.example.ghiblipedia.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        // Set default fragment
        replaceFragment(HomeFragment())

        // Set up bottom navigation
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home_fragment -> replaceFragment(HomeFragment())
                R.id.favorite_fragment -> replaceFragment(FavoriteFragment())
                R.id.profile_fragment -> replaceFragment(ProfileFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Frag) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }
}
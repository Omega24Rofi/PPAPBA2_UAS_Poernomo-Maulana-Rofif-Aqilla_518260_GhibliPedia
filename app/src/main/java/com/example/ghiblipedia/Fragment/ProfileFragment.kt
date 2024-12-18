package com.example.ghiblipedia.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ghiblipedia.Auth.PrefManager.PrefManager
import com.example.ghiblipedia.R
import com.example.ghiblipedia.databinding.FragmentProfileBinding
import com.example.ghiblipedia.databinding.ItemDialogBinding

class ProfileFragment : Fragment() {
    private lateinit var binding:  FragmentProfileBinding
    private lateinit var prefManager: PrefManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val prefManager = PrefManager.getInstance(requireContext())
        val username = prefManager.getUsername()
        val mail = prefManager.getEmail()

        with(binding){
            profileEmail.text = mail
            profileUsername.text = username

            logoutButton.setOnClickListener{
                showLogoutDialog()
            }

        }
        // Inflate the layout for this fragment
        return binding.root


    }

    private fun showLogoutDialog(){
        val builder = AlertDialog.Builder(requireActivity())
        val prefManager = PrefManager.getInstance(requireContext())
        val inflate = requireActivity().layoutInflater
        val binding = ItemDialogBinding.inflate(inflate)
        val dialog = builder.setView(binding.root)
            .setCancelable(false) // Make dialog non-cancelable by tapping outside
            .create()

        with(binding){
            dialogTitle.text = "Logout"
            dialogMessage.text = "Do You want to logout?"
            btnConfirm.text = "Yes"
            btnConfirm.setOnClickListener {
                prefManager.clear()
                requireActivity().finish()
            }
            btnCancel.text = "No"
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}

package com.example.posyandu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize views
        val history = view.findViewById(R.id.btnhistory) as CardView
        val dataakun = view.findViewById(R.id.btnakun) as CardView
        val keluar = view.findViewById(R.id.btnKeluar) as CardView

        // Set onClickListener for button history
        history.setOnClickListener {
            val intent = Intent(context, RiwayatActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener for button keluar (logout)
        keluar.setOnClickListener {
            // Call method to clear authentication data (e.g., token)
            clearAuthenticationData(requireContext())
            // Redirect user to login screen
            val intent = Intent(context, LoginregistActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

    private fun clearAuthenticationData(context: Context) {
        // Clear authentication data (e.g., token) from SharedPreferences
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}

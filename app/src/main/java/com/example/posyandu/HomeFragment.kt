package com.example.posyandu


import AdapterHome
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import org.json.JSONArray
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var layananRecyclerView: RecyclerView
    private lateinit var layananList: MutableList<dataLayanan>
    private lateinit var adapter: AdapterHome

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerView
        layananRecyclerView = view.findViewById(R.id.layananRecyclerView)
        layananRecyclerView.layoutManager = LinearLayoutManager(context)

        layananList = mutableListOf()

        // Fetch data from API
        fetchLayananData()

        return view
    }

    private fun fetchLayananData() {
        // Ambil token dari SharedPreferences
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("access_token", null)

        // Log the retrieved token
        Log.d("fetchLayananData", "Retrieved auth token: $authToken")

        if (authToken != null) {
            AndroidNetworking.get("https://e7ca-114-10-150-229.ngrok-free.app/api/all-antrian-tersedia")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $authToken") // Tambahkan header Authorization dengan token
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        try {
                            Log.d("fetchLayananData", "Response: $response")
                            val jsonObject = response.getJSONObject(0)
                            val data = jsonObject.getJSONObject("data")
                            val antrianList = data.getJSONArray("body")
                            for (i in 0 until antrianList.length()) {
                                val layananObject = antrianList.getJSONObject(i)
                                val id = layananObject.getInt("id")
                                val namaLayanan = layananObject.getString("nama_poli_tsd")
                                val kode = layananObject.getString("kode_poli_tsd")
                                val deskripsi = layananObject.getString("deskripsi")
                                val slug = layananObject.getString("slug")
                                val persyaratan = layananObject.getString("syarat")
                                val batasAntrian = layananObject.getInt("kuota")
                                val userId = layananObject.getInt("user_id")
                                val createdAt = layananObject.getString("created_at")
                                val updatedAt = layananObject.getString("updated_at")
                                layananList.add(
                                    dataLayanan(
                                        id, namaLayanan, kode, deskripsi, slug, persyaratan,
                                        batasAntrian, userId, createdAt, updatedAt
                                    )
                                )
                            }
                            adapter = AdapterHome(requireContext(), layananList) { id, namaLayanan ->
                                replaceFragment(AntrianFragment().apply {
                                    arguments = Bundle().apply {
                                        putInt("id", id)
                                        putString("nama_layanan", namaLayanan)
                                    }
                                })
                            }

                            layananRecyclerView.adapter = adapter
                        } catch (e: Exception) {
                            Log.e("fetchLayananData", "Error parsing response", e)
                        }
                    }

                    override fun onError(anError: ANError) {
                        Log.e("fetchLayananData", "Error fetching data", anError)
                    }
                })
        } else {
            // Handle case where authToken is null
            Log.e("fetchLayananData", "Auth token is null")
            // For example: prompt the user to log in again
        }
    }

    // Function to replace fragments
    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null)
            commit()
        }
    }
}

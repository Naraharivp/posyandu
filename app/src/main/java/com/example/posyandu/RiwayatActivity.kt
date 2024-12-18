package com.example.posyandu

import AdapterRiwayat
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RiwayatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var antrianAdapter: AdapterRiwayat
    private val antrianList = mutableListOf<dataRiwayat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        recyclerView = findViewById(R.id.recyclerViewAntrian)
        recyclerView.layoutManager = LinearLayoutManager(this)
        antrianAdapter = AdapterRiwayat(antrianList)
        recyclerView.adapter = antrianAdapter

        fetchAntrianHistory()
    }

    private fun fetchAntrianHistory() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("access_token", null)

        if (authToken != null) {
            AndroidNetworking.get("https://e7ca-114-10-150-229.ngrok-free.app/api/riwayat")
                .addHeaders("Authorization", "Bearer $authToken")
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            val bodyArray = response.getJSONArray("body")
                            for (i in 0 until bodyArray.length()) {
                                val antrianObject = bodyArray.getJSONObject(i)
                                val antrian = dataRiwayat(
                                    tanggal = antrianObject.getString("tanggal_ngantri"),
                                    namaLengkap = antrianObject.getString("nama_pelanggan"),
                                    kode = antrianObject.getString("kode_antrian")
                                )
                                antrianList.add(antrian)
                            }
                            antrianAdapter.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            Log.e("fetchAntrianHistory", "Error parsing response", e)
                        }
                    }

                    override fun onError(anError: ANError) {
                        Log.e("fetchAntrianHistory", "Error: ${anError.errorDetail}", anError)
                        if (anError.errorBody != null) {
                            Log.e("fetchAntrianHistory", "Error Body: ${anError.errorBody}")
                        }
                        Toast.makeText(this@RiwayatActivity, "Error fetching data: ${anError.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "Token tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show()
        }
    }
}

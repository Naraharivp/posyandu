package com.example.posyandu


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class CekKuota : AppCompatActivity() {
    private lateinit var tvNamaPelanggan: TextView
    private lateinit var tvKodeAntrian: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cek_kuota)



        // Panggil fungsi untuk mengambil data dari API
        fetchData()
    }

    private fun fetchData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("access_token", null)

        // Mengambil nilai ID_LAYANAN dari SharedPreferences
        val idLayanan = sharedPreferences.getInt("ID_LAYANAN", -1).toString()
        Log.d("CekKuota", "ID_LAYANAN: $idLayanan") // Log nilai ID_LAYANAN

        // Periksa jika ID_LAYANAN tidak null sebelum melakukan permintaan jaringan
        if (idLayanan != "-1") {
            AndroidNetworking.get("https://e7ca-114-10-150-229.ngrok-free.app/api/antrian-layanan")
                .addQueryParameter("id", idLayanan)
                .addHeaders("Authorization", "Bearer $authToken")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            val bodyArray = response.getJSONArray("body")
                            val dataContainer = findViewById<LinearLayout>(R.id.dataContainer)
                            dataContainer.removeAllViews() // Membersihkan tampilan sebelum menambahkan data baru

                            for (i in 0 until bodyArray.length()) {
                                val item = bodyArray.getJSONObject(i)
                                val namaPelanggan = item.getString("nama_pelanggan")
                                val kodeAntrian = item.getString("kode_antrian")

                                // Membuat TextView untuk setiap entri data
                                val textView = TextView(this@CekKuota)
                                textView.text = " $kodeAntrian,  = $namaPelanggan"
                                textView.textSize = 16f
                                textView.setTextColor(ContextCompat.getColor(this@CekKuota, R.color.black))

                                // Menambahkan TextView ke container
                                dataContainer.addView(textView)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onError(error: ANError) {
                        // Handle error
                        Log.e("CekKuota", "Error: ${error.message}")
                    }
                })
        }
    }
}

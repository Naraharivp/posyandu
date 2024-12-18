package com.example.posyandu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.datepicker.MaterialDatePicker
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AntrianFragment : Fragment() {

    private lateinit var form: LinearLayout
    private lateinit var tanggalAntrian: EditText
    private lateinit var namaLengkap: EditText
    private lateinit var alamat: EditText
    private lateinit var noHp: EditText
    private lateinit var btnAntri: Button
    private var idLayanan: Int = -1
    private var namaLayanan: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_antrian, container, false)

        // Initialize views
        form = view.findViewById(R.id.formANTRI)
        tanggalAntrian = view.findViewById(R.id.tanggalAntrianANTRI)
        namaLengkap = view.findViewById(R.id.namaLengkapANTRI)
        alamat = view.findViewById(R.id.alamatANTRI)
        noHp = view.findViewById(R.id.noHPANTRI)
        btnAntri = view.findViewById(R.id.btnANTRI)

        // Get arguments
        idLayanan = arguments?.getInt("id", -1) ?: -1
        namaLayanan = arguments?.getString("nama_layanan", "") ?: ""

        // Set onClickListener for ANTRI button
        btnAntri.setOnClickListener {
            antriLayanan()
        }

        // Set onClickListeners for tanggalAntrian EditTexts
        setDatePicker(tanggalAntrian)

        return view
    }

    private fun setDatePicker(editText: EditText) {
        editText.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal")
                .build()

            datePicker.show(childFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormat.format(Date(selection))
                editText.setText(date)
            }
        }
    }

    private fun antriLayanan() {
        val tanggal = tanggalAntrian.text.toString()
        val namaLengkap = namaLengkap.text.toString()
        val alamat = alamat.text.toString()
        val nomorhp = noHp.text.toString()

        // Pastikan semua data telah diisi
        if (tanggal.isNotEmpty() && namaLengkap.isNotEmpty() && alamat.isNotEmpty() && nomorhp.isNotEmpty()) {
            // Buat objek JSON atau sesuaikan dengan struktur permintaan API Anda
            val requestBody = createRequestBody(tanggal, namaLengkap, alamat, nomorhp)

            // Cetak body yang dikirim ke log
            Log.d("Request Body", requestBody)

            // Ambil token dari SharedPreferences
            val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val authToken = sharedPreferences.getString("access_token", null)

            if (authToken != null) {
                // Kirim permintaan menggunakan Fast Android Networking
                AndroidNetworking.post("https://e7ca-114-10-150-229.ngrok-free.app/api/ambil-antrian")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", "Bearer $authToken") // Tambahkan header Authorization dengan token
                    .addJSONObjectBody(JSONObject(requestBody))
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            // Handle response jika permintaan berhasil
                            Toast.makeText(requireContext(), "Permintaan berhasil", Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(anError: ANError) {
                            // Handle error jika permintaan gagal
                            Toast.makeText(requireContext(), "Permintaan gagal: ${anError.message}", Toast.LENGTH_SHORT).show()
                            Log.e("AntrianFragment", "Error: ${anError.message}", anError)
                        }
                    })
            } else {
                // Tampilkan pesan kesalahan jika token tidak ditemukan
                Toast.makeText(requireContext(), "Token tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Tampilkan pesan kesalahan jika ada data yang belum diisi
            Toast.makeText(requireContext(), "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createRequestBody(tanggal: String, namaLengkap: String, alamat: String, nomorhp: String): String {
        // Buat objek JSON atau sesuaikan dengan struktur permintaan API Anda
        val jsonObject = JSONObject()
        jsonObject.put("tanggal_ngantri", tanggal)
        jsonObject.put("nama_pelanggan", namaLengkap)
        jsonObject.put("alamat", alamat)
        jsonObject.put("nomor_hp", nomorhp)
        jsonObject.put("id_poli_tsd", idLayanan)

        return jsonObject.toString()
    }
}

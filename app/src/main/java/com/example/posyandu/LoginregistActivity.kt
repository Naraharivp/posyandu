package com.example.posyandu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject

class LoginregistActivity : AppCompatActivity() {

    private lateinit var loginLayout: LinearLayout
    private lateinit var registerLayout: LinearLayout
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button
    private lateinit var textToRegister: TextView
    private lateinit var textToLogin: TextView
    private lateinit var textPass: EditText
    private lateinit var textEmail: EditText
    private lateinit var editNameRegister: EditText
    private lateinit var editEmailRegister: EditText
    private lateinit var editPassRegister: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginregist)

        // Inisialisasi views
        loginLayout = findViewById(R.id.loginLayout)
        registerLayout = findViewById(R.id.registerLayout)
        buttonLogin = findViewById(R.id.btnLogin)
        buttonRegister = findViewById(R.id.btnDaftar)
        textToRegister = findViewById(R.id.Registrasi)
        textToLogin = findViewById(R.id.Logiiin)
        textPass = findViewById(R.id.edPassLogin)
        textEmail = findViewById(R.id.edEmailLogin)
        editNameRegister = findViewById(R.id.edNamaRegister)
        editEmailRegister = findViewById(R.id.edEmailRegister)
        editPassRegister = findViewById(R.id.edPassRegister)

        // Atur visibilitas awal
        loginLayout.visibility = LinearLayout.VISIBLE
        registerLayout.visibility = LinearLayout.GONE

        // Fungsikan text "Daftar"
        textToRegister.setOnClickListener {
            loginLayout.visibility = LinearLayout.GONE
            registerLayout.visibility = LinearLayout.VISIBLE
        }

        // Fungsikan text "Login"
        textToLogin.setOnClickListener {
            loginLayout.visibility = LinearLayout.VISIBLE
            registerLayout.visibility = LinearLayout.GONE
        }

        // Logika untuk tombol login
        buttonLogin.setOnClickListener {
            val email = textEmail.text.toString()
            val password = textPass.text.toString()
            loginUser(email, password)
        }

        // Logika untuk tombol daftar
        buttonRegister.setOnClickListener {
            val name = editNameRegister.text.toString()
            val email = editEmailRegister.text.toString()
            val password = editPassRegister.text.toString()
            registerUser(name, email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        AndroidNetworking.post("https://e7ca-114-10-150-229.ngrok-free.app/api/login")
            .addBodyParameter("email", email)
            .addBodyParameter("password", password)
            .setTag("login")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // Handle response
                    val token = response.getString("access_token")
                    saveToken(token)

                    val intent = Intent(this@LoginregistActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: finish the login activity
                }

                override fun onError(anError: ANError) {
                    // Handle error
                    Toast.makeText(this@LoginregistActivity, "Login failed: ${anError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun registerUser(name: String, email: String, password: String) {
        AndroidNetworking.post("https://e7ca-114-10-150-229.ngrok-free.app/api/register")
            .addBodyParameter("name", name)
            .addBodyParameter("email", email)
            .addBodyParameter("password", password)
            .setTag("register")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // Handle response
                    val token = response.getString("access_token")
                    saveToken(token)

                    val intent = Intent(this@LoginregistActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: finish the register activity
                }

                override fun onError(anError: ANError) {
                    // Handle error
                    Toast.makeText(this@LoginregistActivity, "Registration failed: ${anError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveToken(token: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", token)
        editor.apply()
    }

    companion object {
        fun getToken(context: Context): String? {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            return sharedPreferences.getString("access_token", null)
        }
    }
}

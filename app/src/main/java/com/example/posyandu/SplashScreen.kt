package com.example.posyandu


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {
    private lateinit var gambar : ImageView
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        gambar = findViewById(R.id.logoSplash)


        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this,LoginregistActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
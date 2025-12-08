package com.example.gestionenvios

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestionenvios.databinding.ActivityEnvioPaquetesPantallaBinding
import com.example.gestionenvios.databinding.ActivityRecepcionPantallaBinding

class EnvioPaquetesPantalla : AppCompatActivity() {
    private lateinit var binding: ActivityEnvioPaquetesPantallaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityEnvioPaquetesPantallaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickAtras(view : View) {
        finish()
    }
}
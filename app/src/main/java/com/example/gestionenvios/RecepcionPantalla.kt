package com.example.gestionenvios

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.gestionenvios.databinding.ActivityRecepcionPantallaBinding

class RecepcionPantalla : AppCompatActivity() {
    private lateinit var binding: ActivityRecepcionPantallaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityRecepcionPantallaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickAtras(view : View) {
        finish()
    }
}

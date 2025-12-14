package com.example.gestionenvios

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import com.example.gestionenvios.databinding.ActivityRecepcionPantallaBinding

class RecepcionPantalla : AppCompatActivity() {
    private lateinit var binding: ActivityRecepcionPantallaBinding

    private var listaDatos: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityRecepcionPantallaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaDatos = intent.getStringArrayListExtra("datos")

        pantallaOrigen()
    }

    fun pantallaOrigen() {
        val pantalla = intent.getStringExtra("pantalla")

        when(pantalla) {
            "paquete" -> mensajePaquete()
        }
    }

    fun mensajePaquete() {
        binding.txt2.text = ""
        binding.tvNumero.text = ""

        var cadena = ""

        listaDatos?.forEach { texto ->
            cadena += texto + "\n"
        }

        binding.txt1.text = cadena

    }

    fun onClickAtras(view : View) {
        finish()
    }
}

package com.example.gestionenvios

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import com.example.gestionenvios.databinding.ActivityRecepcionPantallaBinding

class RecepcionPantalla : AppCompatActivity() {
    private lateinit var binding: ActivityRecepcionPantallaBinding

    private var listaDatosPaquete: ArrayList<String>? = null
    private var listaDatosCorreoCertificado: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityRecepcionPantallaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaDatosPaquete = intent.getStringArrayListExtra("datosPaquete")
        listaDatosCorreoCertificado = intent.getStringArrayListExtra("datosCorreoCertificado")

        pantallaOrigen()
    }

    fun pantallaOrigen() {
        val pantalla = intent.getStringExtra("pantalla")

        when(pantalla) {
            "paquete" -> mensajePaquete()
            "correoCertificado" -> mensajeCorreoCertificado()
        }
    }

    fun mensajePaquete() {
        binding.txt2.text = ""
        binding.tvNumero.text = ""

        var cadena = ""

        listaDatosPaquete?.forEach { texto ->
            cadena += texto + "\n"
        }

        binding.txt1.text = cadena

    }

    fun mensajeCorreoCertificado() {
        binding.txt2.text = ""
        binding.tvNumero.text = ""

        var cadena = ""

        listaDatosCorreoCertificado?.forEach { texto ->
            cadena += texto + "\n"
        }

        binding.txt1.text = cadena

    }

    fun onClickAtras(view : View) {
        finish()
    }
}

package com.example.gestionenvios

import DialogoError
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestionenvios.databinding.ActivityCorreoCertificadoPantallaBinding
import com.example.gestionenvios.databinding.ActivityEnvioPaquetesPantallaBinding

class CorreoCertificadoPantalla : AppCompatActivity() {
    private lateinit var binding: ActivityCorreoCertificadoPantallaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityCorreoCertificadoPantallaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerDatos()
    }

    fun onClickEnviar(view : View) {

    }

    fun onClickCancelar(view : View) {
        finish()
    }
    fun onClickAtras(view : View) {
        finish()
    }

    fun spinnerDatos() {
        val datosSpinner = arrayOf("Pequeño", "Mediano", "Grande")

        val adaptadorSpinner = ArrayAdapter(
            this,
            R.layout.spinner_selected_item,
            datosSpinner
        )

        adaptadorSpinner.setDropDownViewResource(
            R.layout.spinner_dropdown_item
        )

        binding.spinnerTamanio.adapter = adaptadorSpinner

        binding.spinnerTamanio.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val elemento = parent?.getItemAtPosition(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }


}
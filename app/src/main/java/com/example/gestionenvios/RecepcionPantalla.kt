package com.example.gestionenvios

import DialogoError
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.gestionenvios.databinding.ActivityRecepcionPantallaBinding
import kotlinx.coroutines.launch

class RecepcionPantalla : AppCompatActivity() {
    private lateinit var binding: ActivityRecepcionPantallaBinding

    private var listaDatosPaquete: ArrayList<String>? = null
    private var listaDatosCorreoCertificado: ArrayList<String>? = null
    private var contador = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityRecepcionPantallaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaDatosPaquete = intent.getStringArrayListExtra("datosPaquete")
        listaDatosCorreoCertificado = intent.getStringArrayListExtra("datosCorreoCertificado")

        pantallaOrigen()
        comprobarAsegurado()
    }


    fun comprobarAsegurado() {
        listaDatosPaquete?.forEach { datos ->
            if(datos == "Asegurado") {
                mostrarDialogAsegurado()
            }
        }
    }

    fun mostrarDialogAsegurado() {
        val dialogo = DialogoError()

        // cambiar título y mensaje del dialog
        val args = Bundle()
        args.putString("TITULO", "Recepción confirmada")
        args.putString("MENSAJE", "Envío asegurado \nrecibido correctamente")
        dialogo.arguments = args

        dialogo.show(supportFragmentManager, null)
    }

    fun mensajeMain() {

        // coger el número del contador de preferencias
        lifecycleScope.launch {
            applicationContext.dataStore.data.collect { preferences ->
                contador = preferences[CONTADOR_CORREOS] ?: 0
                binding.tvNumero.text = contador.toString()
            }
        }
    }

    fun pantallaOrigen() {
        val pantalla = intent.getStringExtra("pantalla")

        when(pantalla) {
            "paquete" -> mensajePaquete()
            "correoCertificado" -> mensajeCorreoCertificado()
            "main" -> mensajeMain()
        }
    }

    fun mensajePaquete() {
        binding.txt2.visibility = View.GONE
        binding.tvNumero.visibility = View.GONE
        binding.txt1.visibility = View.VISIBLE

        // cambiar el tamaño a la tipografía
        binding.txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)

        var cadena = ""

        listaDatosPaquete?.forEach { texto ->
            cadena += texto + "\n"
        }

        binding.txt1.text = cadena

    }

    fun mensajeCorreoCertificado() {
        mostrarDialogAsegurado()
        binding.txt2.visibility = View.GONE
        binding.tvNumero.visibility = View.GONE
        binding.txt1.visibility = View.VISIBLE

        // cambiar el tamaño a la tipografía
        binding.txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)

        var cadena = ""

        listaDatosCorreoCertificado?.forEach { texto ->
            cadena += texto + "\n"
        }

        binding.txt1.text = cadena

    }

    fun onClickAtras(view : View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

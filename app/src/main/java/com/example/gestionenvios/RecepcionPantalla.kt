package com.example.gestionenvios

import DialogoError
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import com.example.gestionenvios.databinding.ActivityRecepcionPantallaBinding

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
        contador = intent.getIntExtra("contador", 0)

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
        binding.tvNumero.text = contador.toString()
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
        binding.txt2.text = ""
        binding.tvNumero.text = ""

        var cadena = ""

        listaDatosPaquete?.forEach { texto ->
            cadena += texto + "\n"
        }

        binding.txt1.text = cadena

    }

    fun mensajeCorreoCertificado() {
        mostrarDialogAsegurado()
        binding.txt2.text = ""
        binding.tvNumero.text = ""

        var cadena = ""

        listaDatosCorreoCertificado?.forEach { texto ->
            cadena += texto + "\n"
        }

        binding.txt1.text = cadena

    }

    fun onClickAtras(view : View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("contador", contador)
        startActivity(intent)
    }
}

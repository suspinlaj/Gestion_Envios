package com.example.gestionenvios

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.gestionenvios.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Crea un hueco en memoria para el gestor de animaciones para iniciarlo cuando la pantalla esté lista
    private lateinit var animationManager: FallingAnimationManager
    private var contador = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // para que la pantalla sea completa, sin las zonas de botones y barra de tareas
        WindowCompat.setDecorFitsSystemWindows(window, false)
        contador = intent.getIntExtra("contador", 0)

        setSupportActionBar(binding.toolbar)
        animacionPortada()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.Recepcion -> {
                val intent = Intent(this, RecepcionPantalla::class.java)
                intent.putExtra("pantalla", "main")
                intent.putExtra("contador", contador)
                startActivity(intent)
                return true
            }
            R.id.EnvioPaquetes -> {
                val intent = Intent(this, EnvioPaquetesPantalla::class.java)
                startActivity(intent)
                return true
            }
            R.id.CorreoCertificado -> {
                val intent = Intent(this, CorreoCertificadoPantalla::class.java)
                startActivity(intent)
                return true
            }
            else -> return false
        }
    }

    private fun animacionPortada() {

        // imágenes que queremos usar en la animacion
        val listaImagenes = listOf(
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4
        )

        // Instanciamos la clase pasando las dependencias necesarias para que funcione.
        animationManager = FallingAnimationManager(
            container = binding.animationContainer,
            scope = lifecycleScope,
            imagenes = listaImagenes
        )
        // Conectar el manager al ciclo de vida de la pantalla
        // la Activity avisa automáticamente al manager cuando cambia de estado.
        lifecycle.addObserver(animationManager)
    }

}
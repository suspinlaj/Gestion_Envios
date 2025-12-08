package com.example.gestionenvios

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.gestionenvios.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Crea un hueco en memoria para el gestor de animaciones para iniciarlo cuando la pantalla esté lista
    private lateinit var animationManager: FallingAnimationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)


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
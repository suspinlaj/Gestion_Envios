package com.example.gestionenvios

import DialogoError
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestionenvios.databinding.ActivityEnvioPaquetesPantallaBinding
import com.example.gestionenvios.databinding.ActivityRecepcionPantallaBinding

class EnvioPaquetesPantalla : AppCompatActivity() {
    private lateinit var binding: ActivityEnvioPaquetesPantallaBinding
    val CHANNEL_ID = "mi_canal_principal"
    private var listaCamposIncorrectos: MutableList<String> = mutableListOf()
    private lateinit var listaDatos: ArrayList<String>
    private var contador = 0


    // 1. Prepara el "lanzador" para pedir el permiso
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso CONCEDIDO.
            } else {
                // Permiso DENEGADO.
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityEnvioPaquetesPantallaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
    }

    fun comprobarCampos() {

        listaCamposIncorrectos = mutableListOf()

        if(binding.tvRemitente.text.isEmpty()) {
            listaCamposIncorrectos.add("Remitente")
        }

        if(binding.tvDestino.text.isEmpty()) {
            listaCamposIncorrectos.add("Destino")
        }

        if(binding.tvDimensiones.text.isNotEmpty()) {
            if (!validarDimension()) {
                listaCamposIncorrectos.add("Dimensiones")
            }
        }else {
            listaCamposIncorrectos.add("Dimensiones")
        }

        if(binding.tvPeso.text.isNotEmpty()) {
            val peso = binding.tvPeso.text.toString().toInt()
            if (peso > 500.0) {
                listaCamposIncorrectos.add("Peso")
            }
        }else {
            listaCamposIncorrectos.add("Peso")
        }
    }

    fun validarDimension(): Boolean {

        val dimensionEntry = binding.tvDimensiones.text.toString().trim()

        val partes = dimensionEntry.split("x")

        if (partes.size != 3) {
            return false
        }

        val ancho = partes[0].toIntOrNull() ?: return false
        val alto = partes[1].toIntOrNull() ?: return false
        val largo = partes[2].toIntOrNull() ?: return false

        // dimensiones minimas
        val minAncho = 10
        val minAlto = 5
        val minLargo = 10

        // dimensiones maximas
        val maxAncho = 200
        val maxAlto = 150
        val maxLargo = 300

        if(ancho < minAncho || alto < minAlto || largo < minLargo) {
            return false
        }
        if(ancho > maxAncho || alto > maxAlto || largo > maxLargo) {
            return false
        }

        return true
    }

    fun onClickEnviar(view : View) {
        comprobarCampos()
        if(listaCamposIncorrectos.isNotEmpty()){
            // Crear diálogo
          val dialogo = DialogoError()

            // mensaje que sale
            val args = Bundle()

            var cadena = ""

            listaCamposIncorrectos.forEach { texto ->
                cadena += texto + "\n"
            }
            args.putString("TITULO", "Campos incorrectos")
            args.putString("MENSAJE", cadena)

            dialogo.arguments = args

            // Mostrar diálogo
            dialogo.show(supportFragmentManager, null)

        } else {

            listaDatos = arrayListOf(binding.tvRemitente.text.toString(),
                binding.tvDestino.text.toString(),
                binding.tvDimensiones.text.toString(),
                binding.tvPeso.text.toString())

            if(binding.chbxAsegurado.isChecked) {
                listaDatos.add("Asegurado")
            }else {
                listaDatos.add("No Asegurado")
            }

            askForNotificationPermission()
            sendNotification()

        }
    }

    fun onClickCancelar(view : View) {
        finish()
    }

    fun onClickAtras(view : View) {
        finish()
    }

    private fun createNotificationChannel() {
        // Los canales solo son necesarios para API 26 (Oreo) y superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal Principal"
            val descriptionText = "Notificaciones generales de la app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            // 1. Define el canal
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // 2. Registra el canal en el sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    // 2. Función de ayuda para comprobar y pedir el permiso
    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Comprueba si el permiso YA está concedido
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                // No tienes permiso, pídelo
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun sendNotification() {
        // --- 1. Primero, comprueba permisos (Android 13+) ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            // Si no tenemos permiso, pedimos y salimos de la función
            askForNotificationPermission()
            return // No podemos continuar sin permiso
        }
        // --- 2. Define el Intent de navegación ---
        val intent = Intent(this, RecepcionPantalla::class.java).apply {
            // añadir extras
            putExtra("datosPaquete", listaDatos)
            putExtra("pantalla", "paquete")
            putExtra("contador", contador)
        }

        // --- 3. Crea la "Pila" (Back Stack) con TaskStackBuilder ---
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        // --- 4. Construye la Notificación ---
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.img1) // ¡Obligatorio!
            .setContentTitle("¡Nuevo Mensaje!")
            .setContentText("Información sobre su paquete.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // --- 5. Muestra la Notificación ---
        with(NotificationManagerCompat.from(this)) {
            val notificationId = 1
            notify(notificationId, builder.build())
        }
    }

}
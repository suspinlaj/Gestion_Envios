package com.example.gestionenvios

import DialogoError
import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.gestionenvios.databinding.ActivityCorreoCertificadoPantallaBinding

class CorreoCertificadoPantalla : AppCompatActivity() {

    private lateinit var binding: ActivityCorreoCertificadoPantallaBinding
    private var listaCamposIncorrectos: MutableList<String> = mutableListOf()
    private lateinit var listaDatos: ArrayList<String>
    val CHANNEL_ID = "mi_canal_principal"


    private lateinit var cardView: CardView
    private lateinit var tituloCardView: TextView
    private lateinit var descripcionCardView: TextView

    // Variable para controlar el temporizador de desaparición automática
    private var runnableOcultar: Runnable? = null

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

        binding = ActivityCorreoCertificadoPantallaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cardView = binding.CardView
        tituloCardView = binding.tituloCardView
        descripcionCardView = binding.descripcionCardView

        // CardView oculto al inicio
        cardView.visibility = View.GONE

        spinnerDatos()
    }

    // Limpiar el temporizador para evitar errores
    override fun onDestroy() {
        super.onDestroy()
        runnableOcultar?.let { binding.root.removeCallbacks(it) }
    }

    private fun informarTamanios() {
        val tamanioSeleccionado = binding.spinnerTamanio.selectedItem?.toString()

        when (tamanioSeleccionado) {
            "Pequeño" -> {
                tituloCardView.text = "Tamaño Pequeño"
                descripcionCardView.text = "10x17"
            }
            "Mediano" -> {
                tituloCardView.text = "Tamaño Mediano"
                descripcionCardView.text = "20x30"
            }
            "Grande" -> {
                tituloCardView.text = "Tamaño Grande"
                descripcionCardView.text = "40x60"
            }
        }
    }

    fun comprobarCampos() {
        listaCamposIncorrectos = mutableListOf()

        if (binding.tvRemitente2.text.isEmpty()) {
            listaCamposIncorrectos.add("Remitente")
        }

        if (binding.tvDestino2.text.isEmpty()) {
            listaCamposIncorrectos.add("Destino")
        }

        if(binding.spinnerTamanio.selectedItem.toString() == "Seleccionar Tamaño") {
            listaCamposIncorrectos.add("Tamaño")
        }
    }

    fun onClickEnviar(view: View) {
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

            args.putString("MENSAJE", cadena)

            dialogo.arguments = args

            // Mostrar diálogo
            dialogo.show(supportFragmentManager, null)

        } else {

            listaDatos = arrayListOf(binding.tvRemitente2.text.toString(),
                binding.tvDestino2.text.toString(),
                binding.spinnerTamanio.selectedItem.toString())

            askForNotificationPermission()
            sendNotification()

        }
    }

    fun onClickCancelar(view: View) {
        finish()
    }

    fun onClickAtras(view: View) {
        finish()
    }

    private fun ocultarCardViewConAnimacion() {
        // Cancelar animación anterior
        cardView.animate().cancel()

        if (cardView.visibility == View.GONE) return

        cardView.animate()
            .alpha(0f)
            .setDuration(400) // tiempo que tarda en desaparecer
            .withEndAction {
                cardView.visibility = View.GONE
                cardView.alpha = 1f // Restaurar alpha para la próxima vez
            }
            .start()
    }

    private fun mostrarCardViewConAnimacion() {
        // Cancelar animación anterior
        cardView.animate().cancel()

        // mostrar la vista si estaba oculta
        if (cardView.visibility != View.VISIBLE) {
            cardView.alpha = 0f
            cardView.visibility = View.VISIBLE
        }

        cardView.animate()
            .alpha(1f)
            .setDuration(500) // tiempo que tarde en mostrarse
            .withEndAction(null)
            .start()
    }

    private fun spinnerDatos() {
        val datosSpinner = arrayOf("Seleccionar Tamaño", "Pequeño", "Mediano", "Grande")

        val adaptadorSpinner = object : ArrayAdapter<String>(
            this,
            R.layout.spinner_selected_item,
            datosSpinner
        ) {
            // opción "Seleccionar Tamaño" en gris
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(Color.GRAY)
                }
                return view
            }
        }

        adaptadorSpinner.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerTamanio.adapter = adaptadorSpinner

        binding.spinnerTamanio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                // 1. cancelar cualquier temporizador
                runnableOcultar?.let { binding.root.removeCallbacks(it) }

                if (position == 0) {
                    // ocultar el cardview si se seleccionar "Seleccionar Tamaño"
                    ocultarCardViewConAnimacion()
                } else {
                    informarTamanios()
                    mostrarCardViewConAnimacion()

                    // Crear temporizador para ocultar automáticamente
                    runnableOcultar = Runnable {
                        ocultarCardViewConAnimacion()
                    }

                    // Ejecutar  temporizador
                    binding.root.postDelayed(runnableOcultar, 3000)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

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
            putExtra("datosCorreoCertificado", listaDatos)
            putExtra("pantalla", "correoCertificado")
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
            .setContentText("Has recibido una nueva actualización.")
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
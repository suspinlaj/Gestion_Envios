package com.example.gestionenvios
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

// Clase Kotlin para la animación de la portada
class FallingAnimationManager(
    // Dónde se ponen las imágenes (FrameLayout en el XML)
    private val container: ViewGroup,
    // Define cuánto tiempo puede vivir la animación
    // asegura que la animación no siga corriendo si la pantalla ya no existe.
    private val scope: CoroutineScope,
    // Las imágenes que voy a usar
    private val imagenes: List<Int>

    // permite que saber cuando la pantalla se enciendo o apaga (owner)
) : DefaultLifecycleObserver {

    // para parar la animación manualmente
    private var generatorJob: Job? = null

    //amaño fijo de la imagen guardado en memoria para que la animación vaya
    // más fluida y no gaste batería calculando lo mismo mil veces.
    private var itemPixelSize: Int = 0

    // asegurar que itemPixelSize tenga un valor válido sí o sí
    init {
        // Calcular tamaño una sola vez al iniciar la clase
        val context = container.context
        try {
            itemPixelSize = context.resources.getDimensionPixelSize(R.dimen.item_size)
        } catch (e: Exception) {
            itemPixelSize = (80 * context.resources.displayMetrics.density).toInt()
        }
    }

    // metodo para iniciar la animación
    fun start() {
        if (generatorJob?.isActive == true) return // Si ya está corriendo, no hace nada.

        // Arranca la animacion y guarda el control en la variable generatorJob
        // para que luego se pueda parar con stop()
        generatorJob = scope.launch {
            while (true) { // Bucle infinito
                delay(3000) // CAda x segundos caen
                crearImagenAnimacion() // Crea una imagen
            }
        }
    }

    // detiene el bucle de la animación
    fun stop() {
        generatorJob?.cancel()
    }

    // owner hace referencia a la MainActivity

    //Activa la creación de imágenes justo cuando se empieza a ver la pantalla.
    // Se dispara solo cuando la pantalla aparece por el owner
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        start()
    }

    // Se detiene el metodo cuando se deja de ver la aplicacion.
    // Se dispara solo cuando la pantalla se oculta por el owner
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        stop()
    }


    // METODO PARA CREAR LA IMAGEN
    // Crea una imagen nueva, la hace caer  y la borra de la memoria automáticamente al terminar su recorrido.
    private fun crearImagenAnimacion() {
        // Verificaciones de seguridad
        if (container.width == 0 || itemPixelSize == 0) return

        val context = container.context

        // Crea la Vista
        val imageView = ImageView(context).apply {
            // Tamaño del cuadrado
            layoutParams = FrameLayout.LayoutParams(itemPixelSize, itemPixelSize)
            // Imagen al azar
            setImageResource(imagenes.random())

            // Crea el posicionamiento random de la imagen
            // número al azar entre 0 y el ancho de la pantalla
            val posicionImagenRandom = Random.nextInt(container.width - itemPixelSize)
            translationX = posicionImagenRandom.toFloat()

            // Posición NEGATIVA (-itemPixelSize).
            // coloca la imagen justo encima del borde superior de la pantalla para que sea invisible al principio.
            translationY = -itemPixelSize.toFloat()
        }
        // Sin esto la imagen existe en memoria pero no se ve
        container.addView(imageView)

        // Configurar  como se mueven las animaciones
        // destino de la imagen (final de la pantalla)
        val endY = container.height.toFloat()
        // duración de la animación aleatoria
        val duration = Random.nextLong(8000, 12000)  // BAJAR O AUMENTAR VELOCIDAD

        //SI QUIERO LA MISMA VELOCIDAD PARA TODAS LAS IMAGENES
        //val duration = 6000L

        // hace que la imagen se mueva hacia abajo hasta llegar al suelo.
        val fallAnimator = ObjectAnimator.ofFloat(imageView, "translationY", endY).apply {
            this.duration = duration // Tiempo aleatorio
            this.interpolator = LinearInterpolator() // Velocidad constante (sin acelerar)
        }


        // Ejecutar todo lo anterior
        AnimatorSet().apply {
            play(fallAnimator) // Ejecuta caída
            addListener(object : Animator.AnimatorListener {
                // Métodos obligatorios de la interfaz
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}

                // Cuando la imagen termina de caer, se borra, si no se cierra la app tendrá miles
                // de imagenes invisibles en el fondo y se volverá lenta
                override fun onAnimationEnd(animation: Animator) {
                    container.removeView(imageView)
                }
            })
            start()
        }
    }
}
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.gestionenvios.R

// crear dialogo personalizado
class DialogoError : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        //ponerle al dialog el layout con el diseño personalizado
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_personalizado, null)

        // poner mi diseño del layout en las variables
        val tvTitulo = view.findViewById<TextView>(R.id.tvTituloDialog)
        val tvMensaje = view.findViewById<TextView>(R.id.tvMensaje)
        val btnAceptar = view.findViewById<Button>(R.id.btnAceptar)

        // mensaje que se punto en la pantalla
        tvTitulo.text = arguments?.getString("TITULO")
        tvMensaje.text = arguments?.getString("MENSAJE")

        // cerrar dialog al dar click al boton
        btnAceptar.setOnClickListener {
            dismiss()
        }

        dialog.setContentView(view)

        // poner fondo transparente para que se vean las esquinas redondeadas
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }
}

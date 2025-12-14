import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogoError : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val mensajeError = arguments?.getString("MENSAJE")

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(mensajeError)
            .setPositiveButton("Ok", null)
            .create()
    }
}
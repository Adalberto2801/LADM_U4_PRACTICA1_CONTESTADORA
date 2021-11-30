package mx.tecnm.tepic.lamd_u4_prctica1_autocontestador

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class llamadas(p: Context) {
    var nombre = ""
    var telefono = ""
    var baseRemota = FirebaseFirestore.getInstance()
    var pnt = p


    fun insertalistablanca() {
        var datosInsertar = hashMapOf(
            "nombre" to nombre,
            "telefono" to telefono
        )
        baseRemota.collection("listablanca")
            .document(telefono)
            .set(datosInsertar)
            .addOnSuccessListener {
                alerta("Insertado listablanca_firestore")
            }
            .addOnFailureListener {
                mensaje("Error: ${it.message!!}")
            }
    }



    fun insertalistanegra() {
        var datosInsertar = hashMapOf(
            "nombre" to nombre,
            "telefono" to telefono
        )
        baseRemota.collection("listanegra")
            .document(telefono)
            .set(datosInsertar)
            .addOnSuccessListener {
                alerta("Insertado listanegra_firestore")
            }
            .addOnFailureListener {
                mensaje("Error: ${it.message!!}")
            }
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(pnt).setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK"){s,i->}
            .show()
    }

    fun borrar(idElegido:String){
        baseRemota.collection("listanegra")
            .document(idElegido)
            .delete()
            .addOnSuccessListener {
                alerta("SE ELIMINO CON EXITO")
            }
            .addOnFailureListener {
                alerta("ERROR: ${it.message!!}")
            }
    }

    private fun alerta(s: String) {
        Toast.makeText(pnt,s, Toast.LENGTH_LONG).show()
    }
}

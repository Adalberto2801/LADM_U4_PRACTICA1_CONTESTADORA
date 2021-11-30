package mx.tecnm.tepic.lamd_u4_prctica1_autocontestador

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var listaID = ArrayList<String>()
    var baseRemota = FirebaseFirestore.getInstance()
    val agregarl = llamadas(this)
    var datalista = ArrayList<String>()
override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val PERMISSION_ALL = 1
    val PERMISSIONS = arrayOf(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.ANSWER_PHONE_CALLS,
        Manifest.permission.SEND_SMS
    )

    if (!hasPermissions(this, *PERMISSIONS)) {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
    }

    findViewById<Button>(R.id.Lista_Blanca).setOnClickListener{
        var telefono = llamadas(this)
        AlertDialog.Builder(this)
            .setTitle("")
            .setMessage("Desea insertar o ver")
            .setPositiveButton("Insertar_listablanca"){d,i->
                telefono.nombre = findViewById<EditText>(R.id.nombre).text.toString()
                telefono.telefono = findViewById<EditText>(R.id.telefono).text.toString()
                telefono.insertalistablanca()
                findViewById<EditText>(R.id.nombre).setText("")
                findViewById<EditText>(R.id.telefono).setText("")
                d.dismiss()
            }
            .setNegativeButton("Ver_listablanca"){d,i->
                consulta_listablanca()
                d.dismiss()
            }
            .setNeutralButton("cancelar"){d,i->
                d.cancel()
            }
            .show()
    }
    findViewById<Button>(R.id.Lista_Negra).setOnClickListener{
        var telefono = llamadas(this)
        AlertDialog.Builder(this)
            .setTitle("")
            .setMessage("Desea insertar o ver")
            .setPositiveButton("Insertar_listanegra"){d,i->
                telefono.nombre = findViewById<EditText>(R.id.nombre).text.toString()
                telefono.telefono = findViewById<EditText>(R.id.telefono).text.toString()
                telefono.insertalistanegra()
                findViewById<EditText>(R.id.nombre).setText("")
                findViewById<EditText>(R.id.telefono).setText("")
                d.dismiss()
            }
            .setNegativeButton("Ver_listanegra"){d,i->
                consulta_listanegra()
                d.dismiss()
            }
            .setNeutralButton("cancelar"){d,i->
                d.cancel()
            }
            .show()
    }
}
    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
    private fun consulta_listablanca() {
        baseRemota.collection("listablanca")
            .addSnapshotListener { querySnapshot, error ->
                if(error !=null){
                    //mensaje(error.message!!)
                    return@addSnapshotListener
                }//if
                datalista.clear()
                listaID.clear()
                for(document in querySnapshot!!){
                    var cadena = "${document.getString("nombre")} ------ ${document.get("telefono")}"
                    datalista.add(cadena)
                    listaID.add(document.id.toString())
                }
                Lista_View.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, datalista)
                Lista_View.setOnItemClickListener { adapterView, view, posicion, l ->
                    eliminar(posicion)
                }
            }
    }
    private fun consulta_listanegra() {
        baseRemota.collection("listanegra")
            .addSnapshotListener { querySnapshot, error ->
                if(error !=null){
                    //mensaje(error.message!!)
                    return@addSnapshotListener
                }//if
                datalista.clear()
                listaID.clear()
                for(document in querySnapshot!!){
                    var cadena = "${document.getString("nombre")} ------ ${document.get("telefono")}"
                    datalista.add(cadena)
                    listaID.add(document.id.toString())
                }
                Lista_View.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, datalista)
                Lista_View.setOnItemClickListener { adapterView, view, posicion, l ->
                    eliminar(posicion)
                }
            }
    }
    fun eliminar(posicion: Int) {
        var idElegido = listaID.get(posicion)
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage("ESTA SEGURO QUE DESEA ELIMINAR \n${datalista.get(posicion)}?")
            .setPositiveButton("ELIMINAR") { d, i ->
                agregarl.borrar(idElegido)
            }
            .show()
    }//eliminar

}


package mx.tecnm.tepic.lamd_u4_prctica1_autocontestador

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class Recive_c: BroadcastReceiver() {
    var cel: String ? = null
    override fun onReceive(p0: Context?, p1: Intent) {
        var estadollamada = false
        if (p1.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            estadollamada = true
        }
        if (p1.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){
            cel  = p1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).toString()
            if (!estadollamada) {
                var baseRemota = FirebaseFirestore.getInstance()
                if (!cel.equals("null")){

                    baseRemota.collection("listablanca").document(cel.toString())
                        .addSnapshotListener { value, error ->

                            if (value!!.getString("telefono") != null) {
                                Toast.makeText(p0,
                                    "se envio lista blanca ${value!!.getString("telefono")}",
                                    Toast.LENGTH_LONG).show()
                                SMS(false,cel.toString())
                            }else{
                                Toast.makeText(p0,
                                    "No se encontro ${cel.toString()} en lista blanca",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    baseRemota.collection("listanegra").document(cel.toString())
                        .addSnapshotListener { value, error ->

                            if (value!!.getString("telefono") != null) {
                                Toast.makeText(p0,
                                    "se envio lista negra ${value!!.getString("telefono")}",
                                    Toast.LENGTH_LONG).show()
                                SMS(true,cel.toString())
                            }else{
                                Toast.makeText(p0,
                                    "No se encontro ${cel.toString()} en lista negra",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }
    }

    private fun SMS(lista:Boolean,telefono:String) {
        if (lista){
            SmsManager.getDefault().sendTextMessage(telefono,null,
                "No molestar",null,null)
        }else{
            SmsManager.getDefault().sendTextMessage(telefono,null,
                "Marque despues",null,null)
        }
    }

}
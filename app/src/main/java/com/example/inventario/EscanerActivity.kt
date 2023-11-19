package com.example.inventario
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.inventario.databinding.ActivityEscanerBinding
import com.example.inventario.databinding.ActivityMainBinding
import com.google.zxing.integration.android.IntentIntegrator
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.DocumentSnapshot

class EscanerActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding:ActivityEscanerBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding=ActivityEscanerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnScanner.setOnClickListener{
            initScanner()
        }
    }

    private fun initScanner(){
        val integrator = IntentIntegrator (this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escaneando código")
        //integrator.setTorchEnabled(true) //flash cámara
        integrator.setBeepEnabled(true) //sonido
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            } else {
                showProductInfoDialog(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showProductInfoDialog(productId: String) {
        val docRef = db.collection("producto").whereEqualTo("codigo_qr", productId)
        docRef.get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    // Obtener los campos del producto
                    val document = documents.documents[0]
                    val nombre = document.getString("nombre") ?: "Nombre no disponible"
                    val ancho = document.getString("ancho") ?: "Ancho no disponible"
                    val alto = document.getString("alto") ?: "Alto no disponible"
                    val tipoTela = document.getString("tipo_tela") ?: "Tipo de tela no disponible"
                    val precio = document.getString("precio") ?: "Precio no disponible"
                    val stockIdeal = document.getString("stock_ideal") ?: "Stock ideal no disponible"
                    val stockActual = document.getString("stock_actual") ?: "Stock actual no disponible"

                    // contenido del dialogo
                    val message = """
                        Nombre: $nombre
                        Ancho: $ancho
                        Alto: $alto
                        Tipo de tela: $tipoTela
                        Precio: $precio
                        Stock ideal: $stockIdeal
                        Stock actual: $stockActual
                    """.trimIndent()

                    // Crear y mostrar un diálogo emergente con la información del producto
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Detalles del Producto")
                    builder.setMessage(message)
                    builder.setPositiveButton("Aceptar") { dialog, _ ->

                        dialog.dismiss()
                    }

                    val dialog = builder.create()
                    dialog.show()
                } else {
                    Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al recuperar la información del producto", Toast.LENGTH_SHORT).show()
            }
    }
}
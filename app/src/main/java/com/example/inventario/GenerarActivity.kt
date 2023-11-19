package com.example.inventario

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import com.example.inventario.databinding.ActivityGenerarBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.ByteArrayOutputStream
import android.util.Base64

class GenerarActivity : AppCompatActivity() {


    lateinit var ivCodigoQR: ImageView
    private lateinit var binding: ActivityGenerarBinding
    private var db = FirebaseFirestore.getInstance()
    private lateinit var adapterInventario: AdapterInventario
    private lateinit var productoLista: ArrayList<ItemInventario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productoLista = ArrayList()
        adapterInventario = AdapterInventario(productoLista)

        initComponents()
    }

    fun initComponents() {
        ivCodigoQR = findViewById(R.id.codigo_qr)


        guardarProducto()
        codeQr()
        btnAtras()
        btnImage()
    }

    fun guardarProducto() {
        binding.btnGenerar.setOnClickListener {
            try {
                // Genera el código QR y lo convierte a base64 para almacenar en la base
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap(
                    binding.etNombreGen.text.toString(),
                    BarcodeFormat.QR_CODE,
                    750,
                    750
                )
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)

                // Crea un objeto producto con toda la información
                val producto = hashMapOf(
                    "nombre" to binding.etNombreGen.text.toString(),
                    "ancho" to binding.etAncho.text.toString(),
                    "alto" to binding.etAlto.text.toString(),
                    "tipo_tela" to binding.etTipoTela.text.toString(),
                    "precio" to binding.etPrecioGen.text.toString(),
                    "stock_actual" to binding.etStockActualGen.text.toString(),
                    "stock_ideal" to binding.etStockIdealGen.text.toString(),
                    "codigo_qr" to base64Image
                )

                // Guardar el producto en Firebase
                db.collection("producto")
                    .add(producto)
                    .addOnSuccessListener { documentReference ->
                        // Producto Guardado
                        Toast.makeText(this, "Producto guardado exitosamente", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        // error al guardar en Firebase
                        Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun btnAtras() {
        binding.imgBtnAtras.setOnClickListener {
            val intentAtras = Intent(this, OpcionesActivity::class.java)
            startActivity(intentAtras)
        }

    }

    fun codeQr() {
        binding.btnGenerarQR.setOnClickListener(View.OnClickListener {
            try {


                var barcodeEncoder: BarcodeEncoder = BarcodeEncoder()
                var bitmap: Bitmap = barcodeEncoder.encodeBitmap(


                    binding.etNombreGen.text.toString(),
                    BarcodeFormat.QR_CODE,
                    750,
                    750

                )
                ivCodigoQR.setImageBitmap(bitmap)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }
    fun btnImage(){
        val pickMedia=registerForActivityResult(PickVisualMedia()){ uri->

            if (uri!=null){
                binding.imagenProducto.setImageURI(uri)
            }else{
                //error
                Toast.makeText(this, "Saliendo de galeria", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnImagen.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
    }
}

package com.example.inventario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.widget.TextView
import android.os.Handler
import android.widget.Adapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import com.example.inventario.databinding.ActivityEscanerBinding
import com.example.inventario.databinding.ActivityGenerarBinding
import com.example.inventario.databinding.ActivityInventarioBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class InventarioActivity : AppCompatActivity() {


    private lateinit var textViewFecha: TextView
    private val handler = Handler()
    private lateinit var adapterInventario:AdapterInventario
    lateinit var recycler:RecyclerView
    private lateinit var binding: ActivityInventarioBinding
    val db= FirebaseFirestore.getInstance()
    private lateinit var productoLista:ArrayList<ItemInventario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityInventarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        textViewFecha = findViewById(R.id.textViewFecha)
        recycler=findViewById(R.id.rvInventario)


        actualizarFechaAutomaticamente()
        recycler()
        atras()

        //btn enviar datos

    }

    private fun actualizarFechaAutomaticamente() {

        val calendar = Calendar.getInstance()


        val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())


        textViewFecha.text = formatoFecha.format(calendar.time)


        handler.postDelayed(object : Runnable {
            override fun run() {

                calendar.timeInMillis = System.currentTimeMillis()
                textViewFecha.text = formatoFecha.format(calendar.time)


                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacksAndMessages(null)
    }
    private fun recycler(){
        productoLista = ArrayList()
        adapterInventario = AdapterInventario(productoLista)
        recycler.adapter = adapterInventario
        recycler.layoutManager = LinearLayoutManager(this)

        db.collection("producto")
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    val wallItem = document.toObject(ItemInventario::class.java)
                    val documentID = document.id
                    wallItem.id = documentID
                    wallItem.Nombre = document["nombre"].toString()
                    wallItem.Precio = document["precio"].toString()
                    wallItem.Tipo_tela = document["tipo_tela"].toString()
                    wallItem.Ancho = document["ancho"].toString()
                    wallItem.Alto = document["alto"].toString()
                    productoLista.add(wallItem)
                }

                adapterInventario.notifyDataSetChanged()
            }

        adapterInventario.setOnDeleteClickListener(object : AdapterInventario.OnDeleteClickListener {
            override fun onDeleteClick(position: Int) {
                // Lógica para eliminar el elemento de Firestore
                val itemIdToDelete = productoLista[position].id // Asegúrate de tener un campo 'id' en tu modelo
                eliminarElementoFirestore(itemIdToDelete)

                // Elimina el elemento del adaptador
                adapterInventario.removeItem(position)
            }
        })
    }

    private fun eliminarElementoFirestore(id: String) {
        // Elimina el documento de Firestore según el ID
        db.collection("producto")
            .document(id)
            .delete()
            .addOnSuccessListener {
                // Éxito al eliminar, puedes realizar alguna acción adicional si es necesario
                Toast.makeText(this, "Elemento eliminado correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Manejo de errores en caso de que la eliminación falle
                Toast.makeText(this, "Error al eliminar el elemento", Toast.LENGTH_SHORT).show()
            }
    }

    private fun atras(){
        binding.imgBtnAtrasInv.setOnClickListener {
            val intentAtras= Intent(this, OpcionesActivity::class.java)
            startActivity(intentAtras)
        }

    }

}
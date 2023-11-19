package com.example.inventario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class OpcionesActivity : AppCompatActivity() {
    private lateinit var btnInventario:Button
    private lateinit var btnEscanerqr:Button
    private lateinit var btnGenerarqr:Button
    private lateinit var imageView: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opciones)
        imageView = findViewById(R.id.imgLogoInv)



        main()

    }
    fun main(){
        btnInventario=findViewById(R.id.btnInventario)
        btnGenerarqr=findViewById(R.id.btnGeneAnadir)
        btnEscanerqr=findViewById(R.id.btnScannerMain)



        btnInventario.setOnClickListener {
            val intent = Intent(this, InventarioActivity::class.java)
            startActivity(intent)
        }
        btnEscanerqr.setOnClickListener {
            val intent2 = Intent(this, EscanerActivity::class.java)
            startActivity(intent2)
        }

        btnGenerarqr.setOnClickListener {
            val intent3 = Intent(this, GenerarActivity::class.java)
            startActivity(intent3)
        }

    }
}
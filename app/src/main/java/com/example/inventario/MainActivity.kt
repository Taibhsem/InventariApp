package com.example.inventario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.example.inventario.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registrar()

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnIniciarSesion.setOnClickListener {
            val email = binding.etIngresoUsuario.text.toString()
            val password = binding.etIngresoContraseA.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this, OpcionesActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "USUARIO NO REGISTRADO", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun registrar(){
        binding.btnRegUsuario.setOnClickListener {
            val intent = Intent(this, AnadirUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}
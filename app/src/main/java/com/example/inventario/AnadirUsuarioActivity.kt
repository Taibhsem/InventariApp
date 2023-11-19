package com.example.inventario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.inventario.databinding.ActivityAnadirUsuarioBinding
import com.google.firebase.auth.FirebaseAuth

class AnadirUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnadirUsuarioBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnadirUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        autenticar()
        atras()

    }

    fun autenticar(){

        binding.btnConfirmoAnadir.setOnClickListener{
            val nombre = binding.etAnadirNombre.text.toString()
            val email = binding.etAnadirCorreo.text.toString()
            val telefono = binding.etAnadirTelefono.text.toString()
            val asigna = binding.etAnadirAsigna.text.toString()
            val password = binding.etAnadirContraNueva.text.toString()
            val confirmPassword = binding.etAnadirConfContra.text.toString()

            if (nombre.isNotEmpty() && email.isNotEmpty() && telefono.isNotEmpty() && asigna.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Contraseña no coincide", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "los campos no pueden estar vacios", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun atras(){
        binding.btnAtras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
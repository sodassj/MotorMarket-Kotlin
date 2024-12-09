package com.david.maldonado.motormarket

import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.david.maldonado.motormarket.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val nombre = binding.nombreEditText.text.toString()

            // Agregamos un Log para verificar que el nombre es capturado correctamente
            Log.d("RegisterActivity", "Nombre ingresado: $nombre")

            if (email.isNotEmpty() && password.isNotEmpty() && nombre.isNotEmpty()) {
                registerUser(email, password, nombre) // Pasar el nombre a la función
            } else {
                Toast.makeText(this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvBackToLogin.setOnClickListener {
            // Regresa a la actividad de login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(email: String, password: String, nombre: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso, almacenar datos adicionales en Firebase Realtime Database
                    val user = auth.currentUser
                    val userData = hashMapOf(
                        "nombre" to nombre,  // Guardamos el nombre ingresado
                        "email" to user?.email,
                        "uid" to user?.uid
                    )

                    // Guardar los datos del usuario en Firebase Realtime Database
                    val database = FirebaseDatabase.getInstance()
                    val usuariosReference = database.getReference("usuarios")
                    user?.uid?.let { userId ->
                        usuariosReference.child(userId).setValue(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registro exitoso para $nombre", Toast.LENGTH_SHORT).show()
                                // Crear el Intent para redirigir al inicio de sesión
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)  // Inicia la actividad de inicio de sesión
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.e("RegisterActivity", "Error al guardar datos en Realtime Database: ${e.message}")
                                Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Error de registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

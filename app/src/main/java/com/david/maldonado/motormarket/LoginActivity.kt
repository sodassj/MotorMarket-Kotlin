package com.david.maldonado.motormarket

import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.david.maldonado.motormarket.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Listener para el logo
        binding.logoImageView.setOnClickListener {
            val intent = Intent(this, CarListActivity::class.java)
            startActivity(intent)
        }

        // Listener para el botón de inicio de sesión
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Verificación de que los campos no estén vacíos
            if (email.isNotEmpty() && password.isNotEmpty()) {

                // Validar formato de correo electrónico
                if (!email.contains("@")) {
                    Toast.makeText(this, "Por favor, ingresa un correo válido", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Validar longitud de la contraseña
                if (password.length < 8) {
                    Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Si todo está bien, intenta hacer login
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener para el enlace que redirige a la actividad de registro
        binding.tvRegisterLink.setOnClickListener {
            // Inicia la actividad de registro
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Función para realizar el inicio de sesión
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    // Obtener datos del usuario desde la base de datos de Firebase
                    val database = FirebaseDatabase.getInstance()
                    val usuariosReference = database.getReference("usuarios")

                    // Buscamos el nombre del usuario usando el UID del usuario autenticado
                    user?.uid?.let { userId ->
                        usuariosReference.child(userId).get().addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                // Si se encuentra el usuario, obtener el nombre almacenado
                                val nombre = snapshot.child("nombre").value.toString()

                                // Hacer algo con el nombre obtenido
                                Log.d("FirebaseDB", "Nombre del usuario: $nombre")

                                // Mostrar mensaje de bienvenida con el nombre
                                Toast.makeText(this, "Bienvenido, $nombre", Toast.LENGTH_SHORT).show()

                                // Continuar con la navegación
                                val intent = Intent(this, CarListActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Log.e("FirebaseDB", "Usuario no encontrado en la base de datos")
                            }
                        }.addOnFailureListener { e ->
                            Log.e("FirebaseDB", "Error al obtener datos del usuario: ${e.message}")
                        }
                    }
                } else {
                    Log.e("Firebase", "Login fallido: ${task.exception?.message}")
                    Toast.makeText(
                        this,
                        "Login fallido: ${task.exception?.message ?: "Error desconocido"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}

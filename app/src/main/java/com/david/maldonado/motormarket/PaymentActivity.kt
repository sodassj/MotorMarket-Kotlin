package com.david.maldonado.motormarket

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.david.maldonado.motormarket.databinding.ActivityPaymentBinding
import com.google.firebase.auth.FirebaseAuth

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el coche pasado desde la actividad anterior
        val car = intent.getSerializableExtra("car") as? Car
        car?.let {
            // Mostrar la información del coche
            binding.carBrandTextView.text = it.brand
            binding.carModelTextView.text = it.model
            binding.carPriceTextView.text = "$${it.price}"

            // Cargar la imagen del coche de acuerdo al recurso que pasa el objeto Car
            binding.carImageView.setImageResource(it.imageResId) // Esto asigna la imagen dinámica
        } ?: run {
            // Si no se pasa información del coche, mostrar error y finalizar actividad
            Toast.makeText(this, "Error al cargar la información del coche.", Toast.LENGTH_SHORT)
                .show()
            finish()
        }

        // Configurar el botón de pago
        binding.payButton.setOnClickListener {
            // Simular el pago con PayPal
            simulatePayPalPayment()
        }
    }

    private fun simulatePayPalPayment() {
        val user = FirebaseAuth.getInstance().currentUser

        // Verificar si el usuario está autenticado
        if (user == null) {
            // Mostrar un mensaje y redirigir a la pantalla de inicio de sesión
            Toast.makeText(
                this,
                "Debes registrarte o iniciar sesión para realizar una compra.",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return // Salir del método para evitar que el código continúe
        }

        // Si el usuario está autenticado, proceder con el pago
        binding.payButton.isEnabled = false
        binding.payButton.text = "Procesando..."

        Toast.makeText(this, "Redirigiendo a PayPal...", Toast.LENGTH_SHORT).show()

        binding.progressBar.visibility = View.VISIBLE

        Handler().postDelayed({
            binding.progressBar.visibility = View.GONE

            Toast.makeText(this, "Pago procesado con PayPal exitosamente.", Toast.LENGTH_LONG)
                .show()

            binding.payButton.isEnabled = true
            binding.payButton.text = "Pagar"

            finish()
        }, 3000)
    }
}

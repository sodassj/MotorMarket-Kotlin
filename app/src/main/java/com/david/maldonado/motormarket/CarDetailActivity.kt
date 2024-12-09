package com.david.maldonado.motormarket

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.david.maldonado.motormarket.databinding.ActivityCarDetailBinding

class CarDetailActivity : AppCompatActivity() {

    private val REQUEST_CALL_PERMISSION = 1

    private lateinit var binding: ActivityCarDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el coche pasado desde la actividad anterior
        val car = intent.getSerializableExtra("car") as? Car
        car?.let {
            // Mostrar la información del coche
            binding.brandTextView.text = it.brand
            binding.modelTextView.text = it.model
            binding.priceTextView.text = "$${it.price}"
            binding.descriptionTextView.text = it.description
            binding.carImageView.setImageResource(it.imageResId)

            // Configurar el botón de contacto
            binding.contactButton.setOnClickListener { _ ->
                showContactOptions(it.contactEmail, it.contactPhone)
            }

            // Configurar el botón de pago
            binding.payButton.setOnClickListener {
                // Abrir la actividad de pago
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("car", car) // Pasamos el objeto car a la siguiente actividad
                startActivity(intent)
            }
        }
    }

    // Mostrar un cuadro de diálogo con opciones para contactar al propietario
    private fun showContactOptions(email: String, phone: String) {
        val options = arrayOf("Enviar correo", "Llamar")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Contactar al propietario")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> sendEmail(email) // Enviar correo
                1 -> makeCall(phone)  // Realizar llamada
            }
        }
        builder.show()
    }

    // Enviar un correo electrónico
    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, "Interesado en el vehículo")
            putExtra(Intent.EXTRA_TEXT, "Hola, estoy interesado en el vehículo publicado.")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, "Enviar correo usando"))
        } else {
            Toast.makeText(this, "No hay aplicaciones de correo instaladas.", Toast.LENGTH_SHORT).show()
        }
    }

    // Realizar una llamada telefónica
    private fun makeCall(phone: String) {
        // Limpiar el número de teléfono, eliminando espacios y guiones
        val sanitizedPhone = phone.replace(" ", "").replace("-", "")

        // Verificar si el número es válido
        if (sanitizedPhone.isEmpty()) {
            Toast.makeText(this, "Número de teléfono inválido.", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar si el permiso ha sido otorgado
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permiso si no se ha otorgado
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
        } else {
            // Crear un intento para realizar la llamada
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$sanitizedPhone") // Usar el número limpio
            }

            // Verificar si la aplicación para realizar llamadas está disponible
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No se puede realizar la llamada.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Manejar la respuesta de la solicitud de permiso
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso otorgado, realizar la llamada
                Toast.makeText(this, "Permiso para llamar concedido.", Toast.LENGTH_SHORT).show()
            } else {
                // Permiso denegado
                Toast.makeText(this, "No se puede realizar la llamada sin el permiso.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

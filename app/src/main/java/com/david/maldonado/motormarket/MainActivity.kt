package com.david.maldonado.motormarket

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar si el usuario está autenticado usando SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false) // Valor por defecto: false

        if (!isLoggedIn) {
            // Si el usuario no está autenticado, redirige al LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Termina MainActivity para evitar que el usuario regrese
            return
        }

        // Si el usuario está autenticado, muestra el diseño de MainActivity con DrawerLayout
        setContentView(R.layout.activity_main)

        // Configuración del DrawerLayout y la Toolbar
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navView)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        // Asegúrate de que la Toolbar tenga el ícono de la hamburguesa
        toolbar.setNavigationIcon(R.drawable.ic_menu)  // Aquí deberías tener el ícono de menú en tu carpeta drawable

        // Configurar el NavigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigation(menuItem)
            true
        }

        // Abrir el Drawer con el icono de la hamburguesa
        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }
    }

    private fun handleNavigation(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.nav_promotions -> {
                val intent = Intent(this, PromotionsActivity::class.java)
                startActivity(intent)
            }
            // Agregar más casos para otras opciones del menú si es necesario
        }

        // Cerrar el Drawer después de la selección
        drawerLayout.close()
    }
}

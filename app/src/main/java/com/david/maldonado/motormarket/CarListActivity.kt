package com.david.maldonado.motormarket

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.david.maldonado.motormarket.databinding.ActivityCarListBinding

class CarListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarListBinding

    // Lista de coches
    private val carList = listOf(
        Car("Toyota", "Corolla", 2020, 20000.0, "Un sedán confiable con gran economía de combustible.", R.drawable.car_toyota, "toyota@dealer.com", "992-836-8771"),
        Car("Ford", "Focus", 2019, 18000.0, "Compacto y deportivo, ideal para la ciudad.", R.drawable.car_ford, "ford@dealer.com", "987-654-3210"),
        Car("Honda", "Civic", 2021, 22000.0, "Un coche elegante y eficiente con un diseño moderno.", R.drawable.car_honda, "honda@dealer.com", "456-789-1230"),
        Car("Chevrolet", "Malibu", 2022, 25000.0, "Sedán de lujo con características avanzadas de seguridad.", R.drawable.car_chevrolet, "chevrolet@dealer.com", "321-654-9870"),
        Car("BMW", "X5", 2021, 50000.0, "SUV de alto rendimiento y lujo, perfecto para viajes largos.", R.drawable.car_bmw, "bmw@dealer.com", "789-123-4560"),
        Car("Audi", "A4", 2020, 35000.0, "Sedán premium con una conducción suave y un interior elegante.", R.drawable.car_audi, "audi@dealer.com", "654-321-9870"),
        Car("Mercedes-Benz", "C-Class", 2021, 45000.0, "Vehículo de lujo con tecnología avanzada y un diseño atractivo.", R.drawable.car_mercedes, "mercedes@dealer.com", "123-789-4560"),
        Car("Nissan", "Altima", 2020, 24000.0, "Un sedán espacioso con una excelente relación calidad-precio.", R.drawable.car_nissan, "nissan@dealer.com", "789-654-1230"),
        Car("Volkswagen", "Golf", 2019, 22000.0, "Hatchback versátil, ideal para quienes buscan economía y rendimiento.", R.drawable.car_volkswagen, "vw@dealer.com", "321-123-4567"),
        Car("Mazda", "CX-5", 2022, 28000.0, "SUV compacta con un diseño atractivo y tecnología avanzada.", R.drawable.car_mazda, "mazda@dealer.com", "654-987-3210")
    )

    // Lista filtrada de coches
    private var filteredCarList = carList

    // Declarar el adaptador fuera de onCreate
    private lateinit var carAdapter: CarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar RecyclerView con LinearLayoutManager
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar el adaptador aquí
        carAdapter = CarAdapter(filteredCarList) { car ->
            // Al hacer clic en un coche, pasa a la actividad de detalles
            val intent = Intent(this, CarDetailActivity::class.java)
            intent.putExtra("car", car)  // Pasar el objeto Car
            startActivity(intent)
        }

        // Asignar el adaptador al RecyclerView
        binding.recyclerView.adapter = carAdapter

        // Configurar los filtros usando binding (en lugar de findViewById)
        binding.filterButton.setOnClickListener {
            val brandFilter = binding.brandEditText.text.toString().trim()
            val modelFilter = binding.modelEditText.text.toString().trim()
            val yearFilter = binding.yearEditText.text.toString().trim()

            // Aplicar el filtro
            filteredCarList = carList.filter { car ->
                (brandFilter.isEmpty() || car.brand.contains(brandFilter, ignoreCase = true)) &&
                        (modelFilter.isEmpty() || car.model.contains(modelFilter, ignoreCase = true)) &&
                        (yearFilter.isEmpty() || car.year.toString() == yearFilter)
            }

            // Actualizar la lista filtrada en el RecyclerView
            carAdapter.updateList(filteredCarList)
        }

        binding.resetFilterButton.setOnClickListener {
            // Restablecer la lista de coches al estado original
            filteredCarList = carList

            // Actualizar la lista en el RecyclerView
            carAdapter.updateList(filteredCarList)
        }
    }
}

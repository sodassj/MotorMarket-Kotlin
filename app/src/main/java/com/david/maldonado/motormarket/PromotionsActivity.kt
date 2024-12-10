package com.david.maldonado.motormarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.david.maldonado.motormarket.databinding.ActivityPromotionsBinding

class PromotionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPromotionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lista = ArrayList<ImagenModel>()
        lista.add(ImagenModel(R.drawable.car_bmw, text = "Fondo 1"))
        lista.add(ImagenModel(R.drawable.car_audi, text = "Fondo 2"))
        lista.add(ImagenModel(R.drawable.car_toyota, text = "Fondo 3"))
        lista.add(ImagenModel(R.drawable.car_chevrolet, text = "Fondo 4"))

        val adaptador = ImagenAdapter(lista)

        binding.recycler.apply {
            this.adapter = adaptador
            set3DItem(false)
            setAlpha(false)
            setFlat(true)
            setInfinite(true)
        }

    }
}
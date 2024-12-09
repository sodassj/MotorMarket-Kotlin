package com.david.maldonado.motormarket

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.view.View
import android.widget.ImageView
import com.david.maldonado.motormarket.databinding.ItemCarBinding

class CarAdapter(private var carList: List<Car>, private val onCarClick: (Car) -> Unit) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    private val favoriteCars = mutableSetOf<Car>() // Almacena los carros favoritos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.bind(car)
    }

    override fun getItemCount(): Int = carList.size

    fun updateList(newCarList: List<Car>) {
        carList = newCarList
        notifyDataSetChanged()
    }

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val brandTextView: TextView = itemView.findViewById(R.id.brandTextView)
        private val modelTextView: TextView = itemView.findViewById(R.id.modelTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val carImageView: ImageView = itemView.findViewById(R.id.carImageView)
        private val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon)

        init {
            itemView.setOnClickListener {
                val car = carList[adapterPosition]
                onCarClick(car)
            }

            favoriteIcon.setOnClickListener {
                val car = carList[adapterPosition]
                toggleFavorite(car)
                updateFavoriteIcon(car)
            }
        }

        fun bind(car: Car) {
            brandTextView.text = car.brand
            modelTextView.text = car.model
            priceTextView.text = String.format("$%.2f", car.price)
            descriptionTextView.text = car.description
            carImageView.setImageResource(car.imageResId)

            // Actualiza el ícono según el estado de favoritos
            updateFavoriteIcon(car)
        }

        private fun toggleFavorite(car: Car) {
            if (favoriteCars.contains(car)) {
                favoriteCars.remove(car)
            } else {
                favoriteCars.add(car)
            }
        }

        private fun updateFavoriteIcon(car: Car) {
            if (favoriteCars.contains(car)) {
                favoriteIcon.setImageResource(R.drawable.ic_favorite) // Ícono lleno
            } else {
                favoriteIcon.setImageResource(R.drawable.ic_favorite_border) // Ícono vacío
            }
        }
    }
}

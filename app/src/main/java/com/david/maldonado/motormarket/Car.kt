package com.david.maldonado.motormarket

import java.io.Serializable

data class Car(
    val brand: String,
    val model: String,
    val year: Int,
    val price: Double,
    val description: String,
    val imageResId: Int,
    val contactEmail: String,
    val contactPhone: String
) : Serializable



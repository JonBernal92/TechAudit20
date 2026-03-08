package com.example.techaudit20.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Interfaz que define los puntos de conexión (endpoints) de la API
interface ApiService {

    // Simulación de envío de laboratorios a la nube
    @POST("laboratorios/sync")
    suspend fun syncLaboratorios(@Body laboratorios: List<Laboratorio>): Response<Unit>
}
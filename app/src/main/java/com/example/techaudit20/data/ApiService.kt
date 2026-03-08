package com.example.techaudit20.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Interfaz que define los puntos de conexión (endpoints) de la API
interface ApiService {
    @POST("Laboratorio") // Debe coincidir con el nombre del recurso en MockAPI, sino da error
    suspend fun syncLaboratorios(@Body laboratorios: List<Laboratorio>): Response<Unit>
}
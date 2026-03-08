package com.example.techaudit20.data

import kotlinx.coroutines.flow.Flow

class TechAuditRepository(private val dao: TechAuditDao) {

    val allLaboratorios: Flow<List<Laboratorio>> = dao.getAllLaboratorios()

    suspend fun insertLaboratorio(laboratorio: Laboratorio) {
        dao.insertLaboratorio(laboratorio)
    }

    fun getEquiposByLab(labId: Int): Flow<List<Equipo>> {
        return dao.getEquiposByLaboratorio(labId)
    }

    suspend fun insertEquipo(equipo: Equipo) = dao.insertEquipo(equipo)

    // Sincronización Cloud: Envía datos locales al servidor mediante Retrofit
    suspend fun syncWithCloud(laboratorios: List<Laboratorio>): Boolean {
        return try {
            val response = RetrofitClient.instance.syncLaboratorios(laboratorios)
            response.isSuccessful
        } catch (e: Exception) {
            false // Error de red o tiempo de espera
        }
    }
}
package com.example.techaudit20.data

import kotlinx.coroutines.flow.Flow

class TechAuditRepository(private val dao: TechAuditDao) {

    // Exposición de datos reactivos (Flow)
    val allLaboratorios: Flow<List<Laboratorio>> = dao.getAllLaboratorios()

    // Gestión de Laboratorios
    suspend fun insertLaboratorio(laboratorio: Laboratorio) = dao.insertLaboratorio(laboratorio)

    suspend fun updateLaboratorio(laboratorio: Laboratorio) = dao.updateLaboratorio(laboratorio)

    suspend fun deleteLaboratorio(laboratorio: Laboratorio) = dao.deleteLaboratorio(laboratorio)

    // Gestión de Equipos
    fun getEquiposByLab(labId: Int): Flow<List<Equipo>> = dao.getEquiposByLaboratorio(labId)

    suspend fun insertEquipo(equipo: Equipo) = dao.insertEquipo(equipo)

    suspend fun updateEquipo(equipo: Equipo) = dao.updateEquipo(equipo)

    suspend fun deleteEquipo(equipo: Equipo) = dao.deleteEquipo(equipo)

    // Sincronización remota (Módulo 3)
    suspend fun syncWithCloud(laboratorios: List<Laboratorio>): Boolean {
        return try {
            val response = RetrofitClient.instance.syncLaboratorios(laboratorios)
            response.isSuccessful
        } catch (e: Exception) {
            false // Fallo por timeout o falta de red
        }
    }
}
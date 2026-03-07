package com.example.techaudit20.data

import kotlinx.coroutines.flow.Flow

class TechAuditRepository(private val dao: TechAuditDao) {

    // Listado de laboratorios (Módulo 1)
    val allLaboratorios: Flow<List<Laboratorio>> = dao.getAllLaboratorios()

    suspend fun insertLaboratorio(laboratorio: Laboratorio) {
        dao.insertLaboratorio(laboratorio)
    }

    // Listado de equipos por laboratorio (Módulo 2)
    fun getEquiposByLab(labId: Int): Flow<List<Equipo>> {
        return dao.getEquiposByLaboratorio(labId)
    }

    suspend fun insertEquipo(equipo: Equipo) {
        dao.insertEquipo(equipo)
    }

    suspend fun deleteEquipo(equipo: Equipo) {
        dao.deleteEquipo(equipo)
    }
}
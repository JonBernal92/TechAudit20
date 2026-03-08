package com.example.techaudit20.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TechAuditDao {
    // --- Operaciones para Laboratorios ---
    @Query("SELECT * FROM laboratorio_table ORDER BY nombre ASC")
    fun getAllLaboratorios(): Flow<List<Laboratorio>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLaboratorio(laboratorio: Laboratorio)

    @Update
    suspend fun updateLaboratorio(laboratorio: Laboratorio)

    @Delete
    suspend fun deleteLaboratorio(laboratorio: Laboratorio)

    // --- Operaciones para Equipos ---
    @Query("SELECT * FROM equipo_table WHERE laboratorioId = :labId")
    fun getEquiposByLaboratorio(labId: Int): Flow<List<Equipo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEquipo(equipo: Equipo)

    @Update
    suspend fun updateEquipo(equipo: Equipo)

    @Delete
    suspend fun deleteEquipo(equipo: Equipo)
}
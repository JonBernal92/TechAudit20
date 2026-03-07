package com.example.techaudit20.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TechAuditDao {
    // Operaciones para Laboratorios (Módulo 1)
    @Query("SELECT * FROM laboratorio_table ORDER BY nombre ASC")
    fun getAllLaboratorios(): Flow<List<Laboratorio>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertLaboratorio(laboratorio: Laboratorio)

    // Operaciones para Equipos (Módulo 2)
    @Query("SELECT * FROM equipo_table WHERE laboratorioId = :labId")
    fun getEquiposByLaboratorio(labId: Int): Flow<List<Equipo>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertEquipo(equipo: Equipo)

    @Delete
    suspend fun deleteEquipo(equipo: Equipo)
}
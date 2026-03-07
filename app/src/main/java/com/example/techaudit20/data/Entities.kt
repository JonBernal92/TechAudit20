package com.example.techaudit20.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Tabla 1: Laboratorio (Padre)
@Entity(tableName = "laboratorio_table")
data class Laboratorio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val edificio: String
)

// Tabla 2: Equipo (Hijo) con Relación 1:N
@Entity(
    tableName = "equipo_table",
    foreignKeys = [
        ForeignKey(
            entity = Laboratorio::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Equipo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val estado: String,
    val laboratorioId: Int
)
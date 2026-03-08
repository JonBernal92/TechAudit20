package com.example.techaudit20.ui

import androidx.lifecycle.*
import com.example.techaudit20.data.*
import kotlinx.coroutines.launch

class TechAuditViewModel(private val repository: TechAuditRepository) : ViewModel() {

    // Observador reactivo para la UI (Módulo 1)
    val allLaboratorios: LiveData<List<Laboratorio>> = repository.allLaboratorios.asLiveData()

    // --- Funciones CRUD para Laboratorios ---
    fun insertLaboratorio(nombre: String, edificio: String) = viewModelScope.launch {
        repository.insertLaboratorio(Laboratorio(nombre = nombre, edificio = edificio))
    }

    fun updateLaboratorio(laboratorio: Laboratorio) = viewModelScope.launch {
        repository.updateLaboratorio(laboratorio)
    }

    fun deleteLaboratorio(laboratorio: Laboratorio) = viewModelScope.launch {
        repository.deleteLaboratorio(laboratorio)
    }

    // --- Funciones CRUD para Equipos (Módulo 2) ---
    fun getEquipos(labId: Int): LiveData<List<Equipo>> {
        return repository.getEquiposByLab(labId).asLiveData()
    }

    fun insertEquipo(nombre: String, estado: String, labId: Int) = viewModelScope.launch {
        repository.insertEquipo(Equipo(nombre = nombre, estado = estado, laboratorioId = labId))
    }

    fun updateEquipo(equipo: Equipo) = viewModelScope.launch {
        repository.updateEquipo(equipo)
    }

    fun deleteEquipo(equipo: Equipo) = viewModelScope.launch {
        repository.deleteEquipo(equipo)
    }

    /**
     * Módulo 3: Sincronización Remota
     * Acción: Lee el estado local de Room (Snapshot) y lo transmite mediante Retrofit.
     */
    fun syncData(onResult: (Boolean) -> Unit) = viewModelScope.launch {
        val listaLaboratorios = allLaboratorios.value ?: emptyList()

        if (listaLaboratorios.isNotEmpty()) {
            val success = repository.syncWithCloud(listaLaboratorios)
            onResult(success)
        } else {
            onResult(false)
        }
    }
}

// Factoría para inyección de dependencias en el ViewModel
class TechAuditViewModelFactory(private val repository: TechAuditRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TechAuditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TechAuditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
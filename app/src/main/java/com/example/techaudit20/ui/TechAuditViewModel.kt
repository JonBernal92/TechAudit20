package com.example.techaudit20.ui

import androidx.lifecycle.*
import com.example.techaudit20.data.*
import kotlinx.coroutines.launch

class TechAuditViewModel(private val repository: TechAuditRepository) : ViewModel() {

    // Observador de Laboratorios (Módulo 1)
    val allLaboratorios: LiveData<List<Laboratorio>> = repository.allLaboratorios.asLiveData()

    fun insertLaboratorio(nombre: String, edificio: String) = viewModelScope.launch {
        repository.insertLaboratorio(Laboratorio(nombre = nombre, edificio = edificio))
    }

    // Observador de Equipos (Módulo 2)
    fun getEquipos(labId: Int): LiveData<List<Equipo>> {
        return repository.getEquiposByLab(labId).asLiveData()
    }

    fun insertEquipo(nombre: String, estado: String, labId: Int) = viewModelScope.launch {
        repository.insertEquipo(Equipo(nombre = nombre, estado = estado, laboratorioId = labId))
    }
}

// Factoría para instanciar el ViewModel con el repositorio
class TechAuditViewModelFactory(private val repository: TechAuditRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TechAuditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TechAuditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
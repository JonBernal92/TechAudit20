package com.example.techaudit20.ui

import androidx.lifecycle.*
import com.example.techaudit20.data.*
import kotlinx.coroutines.launch

class TechAuditViewModel(private val repository: TechAuditRepository) : ViewModel() {

    // Listado observable de laboratorios (Módulo 1)
    val allLaboratorios: LiveData<List<Laboratorio>> = repository.allLaboratorios.asLiveData()

    fun insertLaboratorio(nombre: String, edificio: String) = viewModelScope.launch {
        repository.insertLaboratorio(Laboratorio(nombre = nombre, edificio = edificio))
    }

    // Listado observable de equipos por laboratorio (Módulo 2)
    fun getEquipos(labId: Int): LiveData<List<Equipo>> {
        return repository.getEquiposByLab(labId).asLiveData()
    }

    fun insertEquipo(nombre: String, estado: String, labId: Int) = viewModelScope.launch {
        repository.insertEquipo(Equipo(nombre = nombre, estado = estado, laboratorioId = labId))
    }

    // Módulo 3: Lee datos de Room y los envía a la API
    fun syncData(onResult: (Boolean) -> Unit) = viewModelScope.launch {
        // 1. Leemos los datos locales de Room a través del repositorio
        val listaLaboratorios = allLaboratorios.value ?: emptyList()

        if (listaLaboratorios.isNotEmpty()) {
            // 2. Enviamos la lista recuperada a la nube mediante Retrofit
            val success = repository.syncWithCloud(listaLaboratorios)
            onResult(success)
        } else {
            onResult(false) // No hay datos para enviar
        }
    }
}

class TechAuditViewModelFactory(private val repository: TechAuditRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TechAuditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TechAuditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
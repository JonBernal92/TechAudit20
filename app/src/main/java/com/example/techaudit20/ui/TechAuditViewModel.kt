package com.example.techaudit20.ui

import androidx.lifecycle.*
import com.example.techaudit20.data.*
import kotlinx.coroutines.launch

class TechAuditViewModel(private val repository: TechAuditRepository) : ViewModel() {

    // Conversión de Flow (Capa de datos) a LiveData (Capa de UI) para observación reactiva
    val allLaboratorios: LiveData<List<Laboratorio>> = repository.allLaboratorios.asLiveData()

    // Ejecución de tareas asíncronas en el Scope del ViewModel para evitar Memory Leaks
    fun insertLaboratorio(nombre: String, edificio: String) = viewModelScope.launch {
        repository.insertLaboratorio(Laboratorio(nombre = nombre, edificio = edificio))
    }

    // Consulta relacional 1:N filtrada por Clave Foránea (FK)
    fun getEquipos(labId: Int): LiveData<List<Equipo>> {
        return repository.getEquiposByLab(labId).asLiveData()
    }

    fun insertEquipo(nombre: String, estado: String, labId: Int) = viewModelScope.launch {
        repository.insertEquipo(Equipo(nombre = nombre, estado = estado, laboratorioId = labId))
    }

    /**
     * Módulo 3: Lógica de Negocio para Sincronización
     * Acción: Recupera el estado actual de Room y delega el envío a la capa de red (Retrofit).
     */
    fun syncData(onResult: (Boolean) -> Unit) = viewModelScope.launch {
        // Captura del "Snapshot" actual de los datos en memoria
        val listaLaboratorios = allLaboratorios.value ?: emptyList()

        if (listaLaboratorios.isNotEmpty()) {
            // Operación de red ejecutada en un hilo de E/S (IO Dispatcher) a través del Repositorio
            val success = repository.syncWithCloud(listaLaboratorios)
            onResult(success)
        } else {
            onResult(false)
        }
    }
}

// Boilerplate necesario para la creación de ViewModels con constructores parametrizados
class TechAuditViewModelFactory(private val repository: TechAuditRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TechAuditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TechAuditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
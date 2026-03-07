package com.example.techaudit20

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.ui.LabAdapter
import com.example.techaudit20.ui.TechAuditViewModel
import com.example.techaudit20.ui.TechAuditViewModelFactory

class MainActivity : AppCompatActivity() {

    // Inicialización del ViewModel usando la factoría y la instancia de la aplicación
    private val viewModel: TechAuditViewModel by viewModels {
        TechAuditViewModelFactory((application as TechAuditApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias a los componentes de la interfaz (UI)
        val etNombre = findViewById<EditText>(R.id.etNombreLab)
        val etEdificio = findViewById<EditText>(R.id.etEdificioLab)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarLab)
        val recyclerView = findViewById<RecyclerView>(R.id.rvLaboratorios)

        // Configuración del RecyclerView con su Adapter
        val adapter = LabAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observador de LiveData: actualiza la lista automáticamente cuando cambian los datos en Room
        viewModel.allLaboratorios.observe(this) { labs ->
            labs?.let { adapter.submitList(it) }
        }

        // Lógica del botón para insertar un nuevo laboratorio
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val edificio = etEdificio.text.toString()

            if (nombre.isNotEmpty() && edificio.isNotEmpty()) {
                viewModel.insertLaboratorio(nombre, edificio)
                etNombre.text.clear()
                etEdificio.text.clear()
                Toast.makeText(this, "Laboratorio Guardado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
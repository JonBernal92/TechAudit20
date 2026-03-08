package com.example.techaudit20

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.ui.LabAdapter
import com.example.techaudit20.ui.TechAuditViewModel
import com.example.techaudit20.ui.TechAuditViewModelFactory

class MainActivity : AppCompatActivity() {

    // Inyección de dependencia del ViewModel mediante el delegado viewModels y la factoría personalizada
    private val viewModel: TechAuditViewModel by viewModels {
        TechAuditViewModelFactory((application as TechAuditApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de componentes de la Interfaz de Usuario (UI)
        val etNombre = findViewById<EditText>(R.id.etNombreLab)
        val etEdificio = findViewById<EditText>(R.id.etEdificioLab)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarLab)
        val btnSync = findViewById<Button>(R.id.btnSyncCloud)
        val progressBar = findViewById<ProgressBar>(R.id.pbLoading)
        val recyclerView = findViewById<RecyclerView>(R.id.rvLaboratorios)

        // Configuración del RecyclerView con un lambda para gestionar eventos de clic (Navegación)
        val adapter = LabAdapter { laboratorio ->
            val intent = Intent(this, EquiposActivity::class.java).apply {
                putExtra("LAB_ID", laboratorio.id)
                putExtra("LAB_NOMBRE", laboratorio.nombre)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Patrón Observer: La UI reacciona automáticamente a cambios en la fuente de datos (SSOT)
        viewModel.allLaboratorios.observe(this) { labs ->
            labs?.let { adapter.submitList(it) }
        }

        // Persistencia local: Inserción de datos a través del ViewModel para no bloquear el Main Thread
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val edificio = etEdificio.text.toString()
            if (nombre.isNotEmpty() && edificio.isNotEmpty()) {
                viewModel.insertLaboratorio(nombre, edificio)
                etNombre.text.clear()
                etEdificio.text.clear()
                Toast.makeText(this, "Laboratorio Guardado", Toast.LENGTH_SHORT).show()
            }
        }

        // Módulo 3: Lógica de Sincronización remota con gestión de estados de carga (UX)
        btnSync.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            btnSync.isEnabled = false

            viewModel.syncData { success ->
                // Callback de retorno para actualizar la UI según el resultado de la red
                progressBar.visibility = View.GONE
                btnSync.isEnabled = true

                if (success) {
                    Toast.makeText(this, "Sincronización Exitosa con MockAPI", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error: No se pudo sincronizar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
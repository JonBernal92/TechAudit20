package com.example.techaudit20

import android.content.Intent
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

    private val viewModel: TechAuditViewModel by viewModels {
        TechAuditViewModelFactory((application as TechAuditApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNombre = findViewById<EditText>(R.id.etNombreLab)
        val etEdificio = findViewById<EditText>(R.id.etEdificioLab)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarLab)
        val btnSync = findViewById<Button>(R.id.btnSyncCloud)
        val recyclerView = findViewById<RecyclerView>(R.id.rvLaboratorios)

        val adapter = LabAdapter { laboratorio ->
            val intent = Intent(this, EquiposActivity::class.java).apply {
                putExtra("LAB_ID", laboratorio.id)
                putExtra("LAB_NOMBRE", laboratorio.nombre)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.allLaboratorios.observe(this) { labs ->
            labs?.let { adapter.submitList(it) }
        }

        // Lógica para guardar localmente
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

        // Módulo 3: Lógica para sincronizar con la nube
        btnSync.setOnClickListener {
            Toast.makeText(this, "Sincronizando...", Toast.LENGTH_SHORT).show()
            viewModel.syncData { success ->
                if (success) {
                    Toast.makeText(this, "Sincronización Exitosa", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
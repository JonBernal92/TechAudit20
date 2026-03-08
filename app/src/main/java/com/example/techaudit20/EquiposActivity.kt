package com.example.techaudit20

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.ui.EquipoAdapter
import com.example.techaudit20.ui.TechAuditViewModel
import com.example.techaudit20.ui.TechAuditViewModelFactory

class EquiposActivity : AppCompatActivity() {

    private val viewModel: TechAuditViewModel by viewModels {
        TechAuditViewModelFactory((application as TechAuditApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipos)

        val labId = intent.getIntExtra("LAB_ID", -1)
        val labNombre = intent.getStringExtra("LAB_NOMBRE") ?: "Laboratorio"

        findViewById<TextView>(R.id.tvLabNombreDetalle).text = "Equipos de: $labNombre"

        // Configuración del RecyclerView para Equipos
        val recyclerView = findViewById<RecyclerView>(R.id.rvEquipos)
        val adapter = EquipoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observamos los equipos filtrados por el ID del laboratorio actual
        viewModel.getEquipos(labId).observe(this) { equipos ->
            equipos?.let { adapter.submitList(it) }
        }

        val etNombre = findViewById<EditText>(R.id.etNombreEquipo)
        val etEstado = findViewById<EditText>(R.id.etEstadoEquipo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarEquipo)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val estado = etEstado.text.toString()

            if (nombre.isNotEmpty() && estado.isNotEmpty() && labId != -1) {
                viewModel.insertEquipo(nombre, estado, labId)
                etNombre.text.clear()
                etEstado.text.clear()
                Toast.makeText(this, "Equipo añadido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
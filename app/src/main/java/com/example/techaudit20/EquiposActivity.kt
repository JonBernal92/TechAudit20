package com.example.techaudit20

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.data.Equipo
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

        val recyclerView = findViewById<RecyclerView>(R.id.rvEquipos)

        // Inicializamos el adapter con la función de gestión (clic largo)
        val adapter = EquipoAdapter { equipo ->
            mostrarOpcionesEquipo(equipo)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

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
            }
        }
    }

    private fun mostrarOpcionesEquipo(equipo: Equipo) {
        val opciones = arrayOf("Editar", "Eliminar")
        AlertDialog.Builder(this)
            .setTitle("Opciones de Equipo")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> abrirDialogoEdicion(equipo)
                    1 -> confirmarEliminacion(equipo)
                }
            }
            .show()
    }

    private fun abrirDialogoEdicion(equipo: Equipo) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Equipo")
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        val inputNombre = EditText(this).apply { setText(equipo.nombre) }
        val inputEstado = EditText(this).apply { setText(equipo.estado) }
        layout.addView(inputNombre); layout.addView(inputEstado)
        builder.setView(layout)

        builder.setPositiveButton("Actualizar") { _, _ ->
            viewModel.updateEquipo(equipo.copy(nombre = inputNombre.text.toString(), estado = inputEstado.text.toString()))
        }
        builder.setNegativeButton("Cancelar", null).show()
    }

    private fun confirmarEliminacion(equipo: Equipo) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar ${equipo.nombre}?")
            .setPositiveButton("Sí") { _, _ -> viewModel.deleteEquipo(equipo) }
            .setNegativeButton("No", null).show()
    }
}
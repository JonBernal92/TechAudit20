package com.example.techaudit20

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.data.Laboratorio
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
        val progressBar = findViewById<ProgressBar>(R.id.pbLoading)
        val recyclerView = findViewById<RecyclerView>(R.id.rvLaboratorios)

        // Configuración del adaptador con soporte para edición/eliminación mediante Long Click
        val adapter = LabAdapter(
            onItemClicked = { laboratorio ->
                val intent = Intent(this, EquiposActivity::class.java).apply {
                    putExtra("LAB_ID", laboratorio.id)
                    putExtra("LAB_NOMBRE", laboratorio.nombre)
                }
                startActivity(intent)
            },
            onLongClick = { laboratorio ->
                mostrarOpcionesLaboratorio(laboratorio)
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.allLaboratorios.observe(this) { labs ->
            labs?.let { adapter.submitList(it) }
        }

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

        btnSync.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            btnSync.isEnabled = false
            viewModel.syncData { success ->
                progressBar.visibility = View.GONE
                btnSync.isEnabled = true
                val mensaje = if (success) "Sincronización Exitosa con MockAPI" else "Error de sincronización"
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Gestión de Datos: Despliega opciones para modificar o eliminar registros de Room.
     */
    private fun mostrarOpcionesLaboratorio(lab: Laboratorio) {
        val opciones = arrayOf("Editar", "Eliminar")
        AlertDialog.Builder(this)
            .setTitle("Gestión de Laboratorio")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> abrirDialogoEdicion(lab)
                    1 -> confirmarEliminacion(lab)
                }
            }
            .show()
    }

    private fun abrirDialogoEdicion(lab: Laboratorio) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Laboratorio")

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val inputNombre = EditText(this).apply { setText(lab.nombre); hint = "Nombre" }
        val inputEdificio = EditText(this).apply { setText(lab.edificio); hint = "Edificio" }

        container.addView(inputNombre)
        container.addView(inputEdificio)
        builder.setView(container)

        builder.setPositiveButton("Actualizar") { _, _ ->
            val nNombre = inputNombre.text.toString()
            val nEdificio = inputEdificio.text.toString()
            if (nNombre.isNotEmpty() && nEdificio.isNotEmpty()) {
                viewModel.updateLaboratorio(lab.copy(nombre = nNombre, edificio = nEdificio))
                Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun confirmarEliminacion(lab: Laboratorio) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar registro")
            .setMessage("¿Está seguro de eliminar ${lab.nombre}? Se perderán los equipos asociados.")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteLaboratorio(lab)
                Toast.makeText(this, "Laboratorio eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
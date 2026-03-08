package com.example.techaudit20

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.techaudit20.ui.TechAuditViewModel
import com.example.techaudit20.ui.TechAuditViewModelFactory

class EquiposActivity : AppCompatActivity() {

    private val viewModel: TechAuditViewModel by viewModels {
        TechAuditViewModelFactory((application as TechAuditApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipos)

        // Recuperamos el ID y Nombre del laboratorio enviados desde MainActivity
        val labId = intent.getIntContext("LAB_ID", -1)
        val labNombre = intent.getStringExtra("LAB_NOMBRE")

        findViewById<TextView>(R.id.tvLabNombreDetalle).text = "Equipos de: $labNombre"

        val etNombre = findViewById<EditText>(R.id.etNombreEquipo)
        val etEstado = findViewById<EditText>(R.id.etEstadoEquipo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarEquipo)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val estado = etEstado.text.toString()

            if (nombre.isNotEmpty() && estado.isNotEmpty()) {
                // Guardamos el equipo vinculado al labId actual
                viewModel.insertEquipo(nombre, estado, labId)
                etNombre.text.clear()
                etEstado.text.clear()
            }
        }
    }
}
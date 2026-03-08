package com.example.techaudit20

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.techaudit20.ui.TechAuditViewModel
import com.example.techaudit20.ui.TechAuditViewModelFactory

class EquiposActivity : AppCompatActivity() {

    // Inicialización del ViewModel compartiendo el mismo repositorio
    private val viewModel: TechAuditViewModel by viewModels {
        TechAuditViewModelFactory((application as TechAuditApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipos)

        // CORRECCIÓN: Se usa getIntExtra para recuperar el ID del laboratorio
        // El -1 indica que si no recibe nada, el ID será inválido
        val labId = intent.getIntExtra("LAB_ID", -1)
        val labNombre = intent.getStringExtra("LAB_NOMBRE") ?: "Laboratorio"

        // Mostramos el nombre del laboratorio seleccionado en el encabezado
        findViewById<TextView>(R.id.tvLabNombreDetalle).text = "Equipos de: $labNombre"

        val etNombre = findViewById<EditText>(R.id.etNombreEquipo)
        val etEstado = findViewById<EditText>(R.id.etEstadoEquipo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarEquipo)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val estado = etEstado.text.toString()

            if (nombre.isNotEmpty() && estado.isNotEmpty() && labId != -1) {
                // Insertamos el equipo vinculado mediante la Clave Foránea (labId)
                viewModel.insertEquipo(nombre, estado, labId)

                etNombre.text.clear()
                etEstado.text.clear()
                Toast.makeText(this, "Equipo registrado con éxito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error: Datos incompletos o Lab ID inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}